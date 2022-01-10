package br.com.als.mymoney.api.disassemblers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.als.mymoney.api.domain.model.Register;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTO;

@Component
public class RegisterDisassembler {

	@Autowired
	private ModelMapper modelMapper;

	public RegisterDTO toRegisterDTO(Register obj) {

		var objDTO = modelMapper.map(obj, RegisterDTO.class);
		return objDTO;
	}
}
