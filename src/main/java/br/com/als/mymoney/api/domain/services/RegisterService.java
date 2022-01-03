package br.com.als.mymoney.api.domain.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.als.mymoney.api.domain.assemblers.RegisterAssembler;
import br.com.als.mymoney.api.domain.controllers.utils.SimplePage;
import br.com.als.mymoney.api.domain.disassemblers.RegisterDisassembler;
import br.com.als.mymoney.api.domain.model.Category;
import br.com.als.mymoney.api.domain.model.Person;
import br.com.als.mymoney.api.domain.model.Register;
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

@Service
public class RegisterService {

	@Autowired
	private RegisterRepository repository;

	@Autowired
	private RegisterAssembler assembler;

	@Autowired
	private RegisterDisassembler disassembler;

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

		RegisterDTO objDTO = disassembler.toRegisterDTO(obj);

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

	public RegisterDTO saveNew(RegisterDTOInsert objDTO) {
		var person = findPerson(objDTO.getPerson().getCode());
		if (!person.isActive())
			throw new DomainException(
					String.format("Não é possível cadastrar registros, pois, %s está inativo(a)", person.getName()),
					null);

		var category = findCategory(objDTO.getCategory().getCode());
		var newRegister = assembler.toRegister(objDTO);

		newRegister.setPerson(person);
		newRegister.setCategory(category);
		newRegister = repository.save(newRegister);

		var registerDTO = new RegisterDTO(newRegister);
		return registerDTO;
	}

	public RegisterDTO update(String code, RegisterDTOUpdate objDTO) {
		var register = findByCodeOrThrowAsRegister(code);
		var person = findPerson(objDTO.getPerson().getCode());
		var category = findCategory(objDTO.getCategory().getCode());

		assembler.toRegister(objDTO, register);
		register.setCategory(category);
		register.setPerson(person);

		register = repository.save(register);
		var registerDTO = new RegisterDTO(register);
		return registerDTO;
	}

	public void delete(String code) {
		var register = findByCodeOrThrowAsRegister(code);
		repository.delete(register);
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
}