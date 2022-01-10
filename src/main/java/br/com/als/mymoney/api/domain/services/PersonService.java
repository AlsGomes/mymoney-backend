package br.com.als.mymoney.api.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.als.mymoney.api.assemblers.PersonAssembler;
import br.com.als.mymoney.api.controllers.utils.SimplePage;
import br.com.als.mymoney.api.disassemblers.PersonDisassembler;
import br.com.als.mymoney.api.domain.model.Address;
import br.com.als.mymoney.api.domain.model.Contact;
import br.com.als.mymoney.api.domain.model.Person;
import br.com.als.mymoney.api.domain.model.dto.PersonDTO;
import br.com.als.mymoney.api.domain.model.dto.PersonDTOInsert;
import br.com.als.mymoney.api.domain.model.dto.PersonDTOUpdate;
import br.com.als.mymoney.api.domain.model.dto.PersonDTOUpdateAddress;
import br.com.als.mymoney.api.domain.model.dto.PersonDTOUpdateContacts;
import br.com.als.mymoney.api.domain.repositories.PersonRepository;
import br.com.als.mymoney.api.domain.services.exceptions.ObjectNotFoundException;

@Service
public class PersonService {

	@Autowired
	private PersonRepository repository;

	@Autowired
	private PersonDisassembler disassembler;

	@Autowired
	private PersonAssembler assembler;

	public PersonDTO findByCodeOrThrow(String code) {
		if (code == null)
			throw new ObjectNotFoundException("Pessoa não encontrada");

		Person obj = repository.findByCode(code)
				.orElseThrow(() -> new ObjectNotFoundException("Pessoa não encontrada"));

		PersonDTO objDTO = new PersonDTO(obj);
		return objDTO;
	}

	public Person findByCodeOrThrowAsPerson(String code) {
		if (code == null)
			throw new ObjectNotFoundException("Pessoa não encontrada");

		Person obj = repository.findByCode(code)
				.orElseThrow(() -> new ObjectNotFoundException("Pessoa não encontrada"));

		return obj;
	}

	public List<PersonDTO> findAll() {
		var list = repository.findAll();
		List<PersonDTO> listDTO = list.stream().map(PersonDTO::new).collect(Collectors.toList());
		return listDTO;
	}

	public SimplePage<PersonDTO> search(String name, Pageable pageable) {
		Page<Person> list = repository.findByNameContaining(name, pageable);
		Page<PersonDTO> listDTO = list.map(PersonDTO::new);

		SimplePage<PersonDTO> simplePageDTO = new SimplePage<>(listDTO);
		return simplePageDTO;
	}

	@Transactional
	public PersonDTO saveNew(PersonDTOInsert objDTOInsert) {
		if (objDTOInsert.getActive() == null)
			objDTOInsert.setActive(true);

		final Person obj = assembler.toPerson(objDTOInsert);
		var newContacts = objDTOInsert.getContacts().stream()
				.map(contact -> new Contact(null, contact.getName(), contact.getEmail(), contact.getTelephone(), obj))
				.collect(Collectors.toList());
		obj.getContacts().addAll(newContacts);

		var savedPerson = repository.save(obj);
		PersonDTO objDTO = new PersonDTO(savedPerson);
		return objDTO;
	}

	@Transactional
	public PersonDTO update(PersonDTOUpdate objDTOUpdate) {
		Person obj = findByCodeOrThrowAsPerson(objDTOUpdate.getCode());

		assembler.toPerson(objDTOUpdate, obj);

		obj = repository.save(obj);
		PersonDTO objDTO = disassembler.toPersonDTO(obj);
		return objDTO;
	}

	@Transactional
	public PersonDTO updateAddress(PersonDTOUpdateAddress objDTOUpdate) {
		Person obj = findByCodeOrThrowAsPerson(objDTOUpdate.getCode());

		// Implementado com BeanUtils, pois, não é necessário criar DTOs para essa entidade Embedded
		// CopyProperties satifaz a necessidade
		if (objDTOUpdate.getAddress() == null) {
			obj.setAddress(null);
		} else if (obj.getAddress() == null) {
			obj.setAddress(new Address());
			BeanUtils.copyProperties(objDTOUpdate.getAddress(), obj.getAddress());
		} else {
			BeanUtils.copyProperties(objDTOUpdate.getAddress(), obj.getAddress());
		}

		Person updatedObj = repository.save(obj);
		PersonDTO objDTO = disassembler.toPersonDTO(updatedObj);
		return objDTO;
	}

	@Transactional
	public PersonDTO updateContacts(@Valid PersonDTOUpdateContacts objDTO, String code) {
		final Person obj = findByCodeOrThrowAsPerson(code);
		obj.getContacts().clear();
		var newContacts = objDTO.getContacts().stream()
				.map(contact -> new Contact(null, contact.getName(), contact.getEmail(), contact.getTelephone(), obj))
				.collect(Collectors.toList());
		obj.getContacts().addAll(newContacts);

		Person updatedObj = repository.save(obj);
		return new PersonDTO(updatedObj);
	}

	@Transactional
	public PersonDTO updateActive(String code, Boolean active) {
		Person obj = findByCodeOrThrowAsPerson(code);
		obj.setActive(active);
		obj = repository.save(obj);

		PersonDTO objDTO = disassembler.toPersonDTO(obj);
		return objDTO;
	}

	@Transactional
	public void deleteByCode(String code) {
		Person obj = findByCodeOrThrowAsPerson(code);
		repository.delete(obj);
	}
}
