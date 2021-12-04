package br.com.als.mymoney.api.domain.assemblers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.als.mymoney.api.domain.model.Person;
import br.com.als.mymoney.api.domain.model.dto.PersonDTOInsert;
import br.com.als.mymoney.api.domain.model.dto.PersonDTOUpdate;

@Component
public class PersonAssembler {

	@Autowired
	private ModelMapper modelMapper;

	public Person toPerson(PersonDTOInsert objDTO) {
		var obj = modelMapper.map(objDTO, Person.class);
		return obj;
	}

	public void toPerson(PersonDTOUpdate objDTO, Person obj) {
		modelMapper.map(objDTO, obj);		
	}
}
