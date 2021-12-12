package br.com.als.mymoney.api.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.als.mymoney.api.domain.assemblers.PersonAssembler;
import br.com.als.mymoney.api.domain.controllers.utils.SimplePage;
import br.com.als.mymoney.api.domain.disassemblers.PersonDisassembler;
import br.com.als.mymoney.api.domain.model.Address;
import br.com.als.mymoney.api.domain.model.Person;
import br.com.als.mymoney.api.domain.model.dto.PersonDTO;
import br.com.als.mymoney.api.domain.model.dto.PersonDTOInsert;
import br.com.als.mymoney.api.domain.model.dto.PersonDTOUpdate;
import br.com.als.mymoney.api.domain.model.dto.PersonDTOUpdateAddress;
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

		PersonDTO objDTO = disassembler.toPersonDTO(obj);
		BeanUtils.copyProperties(obj, objDTO);

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
		Page<PersonDTO> pageDTO = repository.findByNameContaining(name, pageable);
		SimplePage<PersonDTO> simplePageDTO = new SimplePage<>(pageDTO);				
		return simplePageDTO;
	}

	public PersonDTO saveNew(PersonDTOInsert objDTOInsert) {
		if (objDTOInsert.getActive() == null)
			objDTOInsert.setActive(true);

		Person obj = assembler.toPerson(objDTOInsert);
		obj = repository.save(obj);
		PersonDTO objDTO = disassembler.toPersonDTO(obj);
		return objDTO;
	}

	public PersonDTO update(PersonDTOUpdate objDTOUpdate) {
		Person obj = findByCodeOrThrowAsPerson(objDTOUpdate.getCode());

		assembler.toPerson(objDTOUpdate, obj);

		obj = repository.save(obj);
		PersonDTO objDTO = disassembler.toPersonDTO(obj);
		return objDTO;
	}

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

	public PersonDTO updateActive(String code, Boolean active) {
		Person obj = findByCodeOrThrowAsPerson(code);
		obj.setActive(active);
		obj = repository.save(obj);

		PersonDTO objDTO = disassembler.toPersonDTO(obj);
		return objDTO;
	}

	public void deleteByCode(String code) {
		Person obj = findByCodeOrThrowAsPerson(code);
		repository.delete(obj);
	}

}
