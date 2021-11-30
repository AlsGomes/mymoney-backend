package br.com.als.mymoney.api.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.als.mymoney.api.domain.model.Person;
import br.com.als.mymoney.api.domain.model.dto.PersonDTO;
import br.com.als.mymoney.api.domain.model.dto.PersonDTOInsert;
import br.com.als.mymoney.api.domain.repositories.PersonRepository;
import br.com.als.mymoney.api.domain.services.exceptions.ObjectNotFoundException;

@Service
public class PersonService {

	@Autowired
	private PersonRepository repository;

	public PersonDTO findByIdOrThrow(String hashId) {
		Person obj = repository.findByCode(hashId)
				.orElseThrow(() -> new ObjectNotFoundException("Pessoa não encontrada"));

		PersonDTO objDTO = new PersonDTO();
		BeanUtils.copyProperties(obj, objDTO);

		return objDTO;
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

}
