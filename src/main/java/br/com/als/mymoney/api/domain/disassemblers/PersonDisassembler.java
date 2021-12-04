package br.com.als.mymoney.api.domain.disassemblers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.als.mymoney.api.domain.model.Person;
import br.com.als.mymoney.api.domain.model.dto.PersonDTO;

@Component
public class PersonDisassembler {

	@Autowired
	private ModelMapper modelMapper;

	public PersonDTO toPersonDTO(Person obj) {

		var objDTO = modelMapper.map(obj, PersonDTO.class);
		return objDTO;
	}
}
