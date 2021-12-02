package br.com.als.mymoney.api.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public PersonDTO findByCodeOrThrow(String code) {
		if (code == null)
			throw new ObjectNotFoundException("Pessoa não encontrada");

		Person obj = repository.findByCode(code)
				.orElseThrow(() -> new ObjectNotFoundException("Pessoa não encontrada"));

		PersonDTO objDTO = new PersonDTO();
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

	public PersonDTO saveNew(PersonDTOInsert objDTOInsert) {
		if (objDTOInsert.getActive() == null)
			objDTOInsert.setActive(true);

		Person obj = new Person();
		BeanUtils.copyProperties(objDTOInsert, obj);
		Person newObj = repository.save(obj);
		PersonDTO objDTO = new PersonDTO();
		BeanUtils.copyProperties(newObj, objDTO);
		return objDTO;
	}

	public PersonDTO update(PersonDTOUpdate objDTOUpdate) {
		Person obj = findByCodeOrThrowAsPerson(objDTOUpdate.getCode());

		if (objDTOUpdate.getActive() == null)
			BeanUtils.copyProperties(objDTOUpdate, obj, "active");
		else {
			BeanUtils.copyProperties(objDTOUpdate, obj);
		}

		Person updatedObj = repository.save(obj);
		PersonDTO objDTO = new PersonDTO();
		BeanUtils.copyProperties(updatedObj, objDTO);
		return objDTO;
	}

	public PersonDTO updateAddress(PersonDTOUpdateAddress objDTOUpdate) {
		Person obj = findByCodeOrThrowAsPerson(objDTOUpdate.getCode());

		if (objDTOUpdate.getAddress() == null) {
			obj.setAddress(null);
		} else if (obj.getAddress() == null) {
			obj.setAddress(new Address());
			BeanUtils.copyProperties(objDTOUpdate.getAddress(), obj.getAddress());
		} else {
			BeanUtils.copyProperties(objDTOUpdate.getAddress(), obj.getAddress());
		}

		Person updatedObj = repository.save(obj);
		PersonDTO objDTO = new PersonDTO();
		BeanUtils.copyProperties(updatedObj, objDTO);
		return objDTO;
	}

	public PersonDTO updateActive(String code, Boolean active) {
		Person obj = findByCodeOrThrowAsPerson(code);
		obj.setActive(active);
		obj = repository.save(obj);
		
		PersonDTO objDTO = new PersonDTO();
		BeanUtils.copyProperties(obj, objDTO);
		return objDTO;
	}

	public void deleteByCode(String code) {
		Person obj = findByCodeOrThrowAsPerson(code);
		repository.delete(obj);
	}

}
