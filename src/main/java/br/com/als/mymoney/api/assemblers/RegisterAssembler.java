package br.com.als.mymoney.api.assemblers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.als.mymoney.api.domain.model.Register;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTOInsert;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTOUpdate;

@Component
public class RegisterAssembler {

	@Autowired
	private ModelMapper modelMapper;

	public Register toRegister(RegisterDTOInsert objDTO) {

		var register = modelMapper.map(objDTO, Register.class);
		return register;
	}

	public Register toRegister(RegisterDTOUpdate objDTO) {

		var register = modelMapper.map(objDTO, Register.class);
		return register;
	}

	public void toRegister(RegisterDTOUpdate objDTO, Register register) {
		modelMapper.map(objDTO, register);
	}
}
