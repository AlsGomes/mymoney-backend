package br.com.als.mymoney.api.domain.services;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.als.mymoney.api.assemblers.RegisterAssembler;
import br.com.als.mymoney.api.controllers.utils.SimplePage;
import br.com.als.mymoney.api.domain.model.Category;
import br.com.als.mymoney.api.domain.model.Person;
import br.com.als.mymoney.api.domain.model.Register;
import br.com.als.mymoney.api.domain.model.RegisterFile;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTO;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTOInsert;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTOSummary;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTOUpdate;
import br.com.als.mymoney.api.domain.model.dto.RegisterFileDTO;
import br.com.als.mymoney.api.domain.model.dto.statistics.RegisterStatisticsByCategory;
import br.com.als.mymoney.api.domain.model.dto.statistics.RegisterStatisticsByDay;
import br.com.als.mymoney.api.domain.repositories.filters.RegisterFilter;
import br.com.als.mymoney.api.domain.repositories.register.RegisterRepository;
import br.com.als.mymoney.api.domain.services.exceptions.DomainException;
import br.com.als.mymoney.api.domain.services.exceptions.ObjectNotFoundException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class RegisterService {

	@Autowired
	private RegisterRepository repository;

	@Autowired
	private RegisterAssembler assembler;

	@Autowired
	private PersonService personService;

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private FileStorageService fileStorageService;

	public RegisterDTO findByCodeOrThrow(String code) {
		if (code == null)
			throw new ObjectNotFoundException("Lançamento não encontrado");

		Register obj = repository.findByCode(code)
				.orElseThrow(() -> new ObjectNotFoundException("Lancamento não encontrado"));

		RegisterDTO objDTO = new RegisterDTO(obj);
		return objDTO;
	}

	public Register findByCodeOrThrowAsRegister(String code) {
		if (code == null)
			throw new ObjectNotFoundException("Lançamento não encontrado");

		Register obj = repository.findByCode(code)
				.orElseThrow(() -> new ObjectNotFoundException("Lançamento não encontrado"));

		return obj;
	}

	public List<RegisterDTO> findAll() {
		List<Register> list = repository.findAll();
		List<RegisterDTO> listDTO = list.stream().map(RegisterDTO::new).collect(Collectors.toList());

		return listDTO;
	}

	public List<RegisterDTO> findOverdueRegisters() {
		List<Register> list = repository.findByDueDateLessThanEqualAndPaymentDateIsNullOrderByType(LocalDate.now());
		List<RegisterDTO> listDTO = list.stream().map(RegisterDTO::new).collect(Collectors.toList());
	
		return listDTO;
	}
	
	public RegisterFileDTO findRegisterFileByFileName(String registerCode, String registerFileCode) {
		var register = findByCodeOrThrowAsRegister(registerCode);
		var registerFile = repository.findRegisterFileByFileName(register.getId(), registerFileCode)
				.orElseThrow(() -> new ObjectNotFoundException("Arquivo do registro não encontrado"));
		var dto = new RegisterFileDTO(registerFile);

		return dto;
	}

	public SimplePage<RegisterDTO> search(RegisterFilter filter, Pageable pageable) {
		if (!filter.isValidDate())
			throw new DomainException("Vencimento Até, precisa ser maior do que Vencimento De");

		Page<Register> list = repository.search(filter, pageable);
		Page<RegisterDTO> listDTO = list.map(RegisterDTO::new);
		SimplePage<RegisterDTO> simplePage = new SimplePage<>(listDTO);

		return simplePage;
	}

	public SimplePage<RegisterDTOSummary> searchSummary(RegisterFilter filter, Pageable pageable) {
		if (!filter.isValidDate())
			throw new DomainException("Vencimento Até, precisa ser maior do que Vencimento De");

		Page<Register> list = repository.search(filter, pageable);
		Page<RegisterDTOSummary> listDTO = list.map(RegisterDTOSummary::new);
		SimplePage<RegisterDTOSummary> simplePage = new SimplePage<>(listDTO);

		return simplePage;
	}

	@Transactional
	public RegisterDTO saveNew(RegisterDTOInsert objDTO) {
		final var person = findPerson(objDTO.getPerson().getCode());
		if (!person.isActive())
			throw new DomainException(
					String.format("Não é possível cadastrar registros, pois, %s está inativo(a)", person.getName()),
					null);

		final var category = findCategory(objDTO.getCategory().getCode());
		final var newRegister = assembler.toRegister(objDTO);
		
		final boolean hasFiles = !objDTO.getFiles().isEmpty();

		if (hasFiles) {
			List<RegisterFile> files = objDTO.getFiles().stream()
					.map(this::getTemporaryFile)
					.collect(Collectors.toList());
			newRegister.setFiles(new ArrayList<>());
			newRegister.getFiles().addAll(files);
			newRegister.getFiles().forEach(rf -> rf.setRegister(newRegister));
		}

		newRegister.setPerson(person);
		newRegister.setCategory(category);
		
		var savedRegister = repository.save(newRegister);
		repository.flush();
		
		if (hasFiles)
			makePermanent(savedRegister.getFiles());

		var registerDTO = new RegisterDTO(savedRegister);
		return registerDTO;
	}

	@Transactional
	public RegisterDTO update(String code, RegisterDTOUpdate objDTO) {
		final var register = findByCodeOrThrowAsRegister(code);
		final var person = findPerson(objDTO.getPerson().getCode());
		final var category = findCategory(objDTO.getCategory().getCode());
		final var hasFiles = !objDTO.getFiles().isEmpty();
		
		assembler.toRegister(objDTO, register);

		List<RegisterFile> newFilesAsRegisterFile = null;
		List<RegisterFile> filesToDelete = null;
		
		if (hasFiles) {
			var newFiles = objDTO.getFiles().stream()
					.filter(f -> f.getCode() == null)
					.collect(Collectors.toList());
			
			var existingFilesCodes = objDTO.getFiles().stream()
					.filter(f -> f.getCode() != null)
					.map(f -> f.getCode())
					.collect(Collectors.toList());

			newFilesAsRegisterFile = newFiles.stream()
					.map(newFile -> getTemporaryFile(newFile.getFileName()))
					.collect(Collectors.toList());
			newFilesAsRegisterFile.forEach(rf -> rf.setRegister(register));
			
			if (register.getFiles() == null)
				register.setFiles(new ArrayList<>());

			filesToDelete = register.getFiles().stream()
					.filter(file -> !existingFilesCodes.contains(file.getCode()))
					.collect(Collectors.toList());
			
			for (var fileToDelete : filesToDelete) {
				register.getFiles().remove(fileToDelete);
			}

			register.getFiles().addAll(newFilesAsRegisterFile);
		} else {
			if (register.getFiles() != null)
				register.getFiles().clear();
		}

		register.setCategory(category);
		register.setPerson(person);

		var updatedRegister = repository.save(register);
		repository.flush();
		
		if (newFilesAsRegisterFile != null)
			makePermanent(newFilesAsRegisterFile);
		
		if (filesToDelete != null) {
			for (var fileToDelete : filesToDelete) {
				fileStorageService.deleteFile(register.getCode(), fileToDelete.getFileName());
			}
		}
		
		if (!hasFiles)
			fileStorageService.deleteFiles(code);

		var registerDTO = new RegisterDTO(updatedRegister);
		return registerDTO;
	}

	@Transactional
	public void delete(String code) {
		var register = findByCodeOrThrowAsRegister(code);
		repository.delete(register);	
		repository.flush();
		
		fileStorageService.deleteFiles(code);		
	}

	public List<RegisterStatisticsByCategory> statisticsByCategory(LocalDate date) {
		if (date == null)
			date = LocalDate.now();
		return repository.byCategory(date);
	}

	public List<RegisterStatisticsByDay> statisticsByDay(LocalDate date) {
		if (date == null)
			date = LocalDate.now();
		return repository.byDay(date);
	}

	public byte[] reportByPerson(LocalDate dateFrom, LocalDate dateUntil) throws Exception {
		if (dateFrom != null ^ dateUntil != null)
			throw new DomainException(
					"O parâmetro dateFrom e dateUntil são obrigatórios, ou, não envie nenhum dos dois para que seja usado o mês corrente");

		if (dateFrom == null && dateUntil == null) {
			var now = LocalDate.now();
			dateFrom = now.withDayOfMonth(1);
			dateUntil = now.withDayOfMonth(now.lengthOfMonth());
		}

		var list = repository.byPerson(dateFrom, dateUntil);

		var params = new HashMap<String, Object>();
		params.put("DT_INICIO", Date.valueOf(dateFrom));
		params.put("DT_FIM", Date.valueOf(dateUntil));
		params.put("REPORT", new Locale("pt", "BR"));

		var stream = this.getClass().getResourceAsStream("/reports/registers-by-person.jasper");

		JasperPrint jasperPrint = JasperFillManager.fillReport(stream, params, new JRBeanCollectionDataSource(list));

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

	private Person findPerson(String personCode) {
		Person obj = personService.findByCodeOrThrowAsPerson(personCode);
		return obj;
	}

	private Category findCategory(String code) {
		Category obj = categoryService.findByCodeOrThrowAsCategory(code);
		return obj;
	}

	private RegisterFile getTemporaryFile(String fileName) {		
		FileStorageService.File file = fileStorageService.getTemporaryInfo(fileName);
		var registerFile = new RegisterFile(file);
		return registerFile;		
	}

	private void makePermanent(List<RegisterFile> files) {
		for (var file : files) {
			fileStorageService.makePermanent(file.getRegister().getCode(), file.getFileName());
		}
	}
}
