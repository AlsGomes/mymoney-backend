package br.com.als.mymoney.api.domain.assemblers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.als.mymoney.api.domain.model.Register;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTOInsert;

@Component
public class RegisterAssembler {

	@Autowired
	private ModelMapper modelMapper;

	public Register toRegister(RegisterDTOInsert objDTO) {

		var register = modelMapper.map(objDTO, Register.class);
		return register;
	}
}
