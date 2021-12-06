package br.com.als.mymoney.api.domain.services;

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

	public RegisterDTO findByCodeOrThrow(String personCode, String code) {
		if (code == null)
			throw new ObjectNotFoundException("Lançamento não encontrado");

		if (personCode == null)
			throw new ObjectNotFoundException("Código da Pessoa inválido");

		Person person = findPerson(personCode);

		Register obj = repository.findByCodeAndPerson(code, person)
				.orElseThrow(() -> new ObjectNotFoundException("Lancamento não encontrado"));

		RegisterDTO objDTO = disassembler.toRegisterDTO(obj);

		return objDTO;
	}

	public Register findByCodeOrThrowAsRegister(String personCode, String code) {
		if (code == null)
			throw new ObjectNotFoundException("Lançamento não encontrado");

		if (personCode == null)
			throw new ObjectNotFoundException("Código da Pessoa inválido");

		Person person = findPerson(personCode);

		Register obj = repository.findByCodeAndPerson(code, person)
				.orElseThrow(() -> new ObjectNotFoundException("Lancamento não encontrado"));

		return obj;
	}

	public List<RegisterDTO> findAll(String personCode) {
		if (personCode == null)
			throw new ObjectNotFoundException("Código da Pessoa inválido");

		Person obj = findPerson(personCode);

		List<Register> list = repository.findByPerson(obj);
		List<RegisterDTO> listDTO = list.stream().map(RegisterDTO::new).collect(Collectors.toList());

		return listDTO;
	}

	public SimplePage<RegisterDTO> search(String personCode, RegisterFilter filter, Pageable pageable) {
		if (personCode == null)
			throw new ObjectNotFoundException("Código da Pessoa inválido");

		if (!filter.isValidDate())
			throw new DomainException("Vencimento Até, precisa ser maior do que Vencimento De");

		var person = findPerson(personCode);

		filter.setPersonId(person.getId());

		Page<Register> list = repository.search(filter, pageable);
		Page<RegisterDTO> listDTO = list.map(RegisterDTO::new);
		SimplePage<RegisterDTO> simplePage = new SimplePage<>(listDTO); 

		return simplePage;
	}

	public RegisterDTO saveNew(String personCode, RegisterDTOInsert objDTO) {
		var person = findPerson(personCode);
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

	public void delete(String personCode, String code) {
		var register = findByCodeOrThrowAsRegister(personCode, code);
		repository.delete(register);
	}
}
