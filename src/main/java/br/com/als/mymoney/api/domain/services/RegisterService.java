package br.com.als.mymoney.api.domain.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import br.com.als.mymoney.api.domain.assemblers.RegisterAssembler;
import br.com.als.mymoney.api.domain.controllers.utils.SimplePage;
import br.com.als.mymoney.api.domain.model.Category;
import br.com.als.mymoney.api.domain.model.Person;
import br.com.als.mymoney.api.domain.model.Register;
import br.com.als.mymoney.api.domain.model.RegisterFile;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTO;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTOInsert;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTOSummary;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTOUpdate;
import br.com.als.mymoney.api.domain.model.dto.statistics.RegisterStatisticsByCategory;
import br.com.als.mymoney.api.domain.model.dto.statistics.RegisterStatisticsByDay;
import br.com.als.mymoney.api.domain.repositories.RegisterRepository;
import br.com.als.mymoney.api.domain.repositories.filters.RegisterFilter;
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

	private Person findPerson(String personCode) {
		Person obj = personService.findByCodeOrThrowAsPerson(personCode);
		return obj;
	}

	private Category findCategory(String code) {
		Category obj = categoryService.findByCodeOrThrowAsCategory(code);
		return obj;
	}

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
				.orElseThrow(() -> new ObjectNotFoundException("Lancamento não encontrado"));

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

		if (!objDTO.getFiles().isEmpty()) {
			List<RegisterFile> files = objDTO.getFiles().stream().map(this::getTemporaryFile)
					.collect(Collectors.toList());
			newRegister.setFiles(new ArrayList<>());
			newRegister.getFiles().addAll(files);
			newRegister.getFiles().forEach(rf -> rf.setRegister(newRegister));
		}

		newRegister.setPerson(person);
		newRegister.setCategory(category);
		var savedRegister = repository.save(newRegister);
		
		makePermanent(savedRegister.getFiles());

		var registerDTO = new RegisterDTO(savedRegister);
		return registerDTO;
	}

	@Transactional
	public RegisterDTO update(String code, RegisterDTOUpdate objDTO) {
		final var register = findByCodeOrThrowAsRegister(code);
		final var person = findPerson(objDTO.getPerson().getCode());
		final var category = findCategory(objDTO.getCategory().getCode());

		assembler.toRegister(objDTO, register);

		if (!objDTO.getFiles().isEmpty()) {
			var newFiles = objDTO.getFiles().stream()
					.filter(f -> f.getCode() == null)
					.collect(Collectors.toList());
			
			var existingFilesCodes = objDTO.getFiles().stream()
					.filter(f -> f.getCode() != null)
					.map(f -> f.getCode())
					.collect(Collectors.toList());

			var newFilesAsRegisterFile = newFiles.stream()
					.map(newFile -> getTemporaryFile(newFile.getFileName()))
					.collect(Collectors.toList());
			newFilesAsRegisterFile.forEach(rf -> rf.setRegister(register));
			makePermanent(newFilesAsRegisterFile);

			if (register.getFiles() == null)
				register.setFiles(new ArrayList<>());

			var filesToDelete = register.getFiles().stream()
					.filter(file -> !existingFilesCodes.contains(file.getCode()))
					.collect(Collectors.toList());
			
			for (var fileToDelete : filesToDelete) {
				register.getFiles().remove(fileToDelete);
				deleteFile(register.getCode(), fileToDelete.getFileName());
			}

			register.getFiles().addAll(newFilesAsRegisterFile);
		} else {
			if (register.getFiles() != null)
				register.getFiles().clear();

			deleteFiles(code);
		}

		register.setCategory(category);
		register.setPerson(person);

		var updatedRegister = repository.save(register);		
		var registerDTO = new RegisterDTO(updatedRegister);
		return registerDTO;
	}

	@Transactional
	public void delete(String code) {
		var register = findByCodeOrThrowAsRegister(code);
		repository.delete(register);		
		deleteFiles(code);		
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

	private RegisterFile getTemporaryFile(String fileName) {
		var fullPath = "C:\\Users\\als_0\\Downloads\\docs\\" + fileName;
	
		try {
			var file = new File(fullPath);
			if (!file.exists())
				throw new FileNotFoundException();
	
			var code = file.getName().split("_")[0];
			var name = file.getName();
			var size = file.length();
			var extension = name.substring(name.lastIndexOf(".")).toLowerCase();			
			var contentType = "";
	
			switch (extension) {
			case ".pdf":
				contentType = MediaType.APPLICATION_PDF_VALUE;
				break;
			case ".png":
				contentType = MediaType.IMAGE_PNG_VALUE;
				break;
			case ".jpeg":
				contentType = MediaType.IMAGE_JPEG_VALUE;
				break;
			default:
				break;		
			}
	
			var registerFile = new RegisterFile();
			registerFile.setCode(code);
			registerFile.setContentType(contentType);
			registerFile.setFileName(name);
			registerFile.setSize(size);
			return registerFile;
	
		} catch (FileNotFoundException e) {
			throw new DomainException(String.format("Erro ao buscar o arquivo %s, enquanto tentava buscar", fullPath), e);
		}
	}

	private void makePermanent(List<RegisterFile> files) {
		var root = "C:\\Users\\als_0\\Downloads\\docs\\";
		for (var file : files) {
			var fullPath = root + file.getFileName();
			var tempFile = new File(fullPath);

			try {
				if (!tempFile.exists())
					throw new FileNotFoundException();
			} catch (FileNotFoundException e) {
				System.out.println("FileNotFoundException: ");
				e.printStackTrace();
				throw new DomainException(
						String.format("Erro ao buscar o arquivo %s, enquanto tentava tornar permanente", fullPath), e);
			}

			var outFolder = root + file.getRegister().getCode();
			try (InputStream in = new FileInputStream(tempFile)) {
				var outFile = new File(outFolder);
				if (!outFile.exists())
					outFile.mkdirs();

				try (OutputStream out = new FileOutputStream(outFolder + "\\" + file.getFileName())) {
					out.write(in.readAllBytes());
				} catch (IOException e) {
					e.printStackTrace();
					throw new DomainException(
							String.format("Erro ao buscar o arquivo %s, enquanto tentava tornar permanente", fullPath),
							e);
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new DomainException(
						String.format("Erro ao buscar o arquivo %s, enquanto tentava tornar permanente", fullPath), e);
			}
		}

		for (var file : files) {
			var fullPath = root + file.getFileName();
			var tempFile = new File(fullPath);
			tempFile.delete();
		}
	}

	private void deleteFiles(String code) {
		var folder = new File("C:\\Users\\als_0\\Downloads\\docs\\" + code);
	
		if (!folder.exists())
			return;						
		
		Arrays.asList(folder.listFiles()).stream().forEach(File::delete);
		folder.delete();
	}

	private void deleteFile(String code, String fileName) {
		var file = new File("C:\\Users\\als_0\\Downloads\\docs\\" + code + "\\" + fileName);
	
		try {
			if (!file.exists())
				throw new FileNotFoundException();
		} catch (FileNotFoundException e) {
			throw new DomainException("O arquivo %s não existe", e);
		}
				
		file.delete();
		
		if (file.getParentFile().listFiles().length == 0) {
			file.getParentFile().delete();
		}		
	}

	private void deleteFileIfExists(String code, String fileName) {
		var file = new File("C:\\Users\\als_0\\Downloads\\docs\\" + code + "\\" + fileName);

		if (!file.exists())
			return;

		file.delete();

		if (file.getParentFile().listFiles().length == 0) {
			file.getParentFile().delete();
		}
	}
}