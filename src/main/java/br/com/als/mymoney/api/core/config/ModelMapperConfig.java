package br.com.als.mymoney.api.core.config;

import java.time.LocalDate;

import org.modelmapper.Condition;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.als.mymoney.api.domain.model.Person;
import br.com.als.mymoney.api.domain.model.Register;
import br.com.als.mymoney.api.domain.model.dto.PersonDTOUpdate;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTO;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTOInsert;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTOUpdate;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		var modelMapper = new ModelMapper();

		mapToAssembleRegister(modelMapper);
		mapToDisassembleRegister(modelMapper);
		mapToAssemblePerson(modelMapper);

		return modelMapper;
	}

	private void mapToAssembleRegister(ModelMapper modelMapper) {
		Converter<String, LocalDate> parseToDateConverter = ctx -> ctx.getSource() == null ? null
				: LocalDate.parse(ctx.getSource());
		
		var typeMapInsert = modelMapper.createTypeMap(RegisterDTOInsert.class, Register.class);
		typeMapInsert.addMappings(
				mapper -> mapper.using(parseToDateConverter).map(RegisterDTOInsert::getDueDate, Register::setDueDate));

		typeMapInsert.addMappings(mapper -> mapper.using(parseToDateConverter).map(RegisterDTOInsert::getPaymentDate,
				Register::setPaymentDate));

		var typeMapUpdate = modelMapper.createTypeMap(RegisterDTOUpdate.class, Register.class);
		typeMapUpdate.addMappings(
				mapper -> mapper.using(parseToDateConverter).map(RegisterDTOUpdate::getDueDate, Register::setDueDate));
		
		typeMapUpdate.addMappings(mapper -> mapper.using(parseToDateConverter).map(RegisterDTOUpdate::getPaymentDate,
				Register::setPaymentDate));
	}

	private void mapToDisassembleRegister(ModelMapper modelMapper) {
		var typeMap = modelMapper.createTypeMap(Register.class, RegisterDTO.class);

		Converter<LocalDate, String> parseToStringConverter = ctx -> ctx.getSource() == null ? null
				: ctx.getSource().toString();

		typeMap.addMappings(
				mapper -> mapper.using(parseToStringConverter).map(Register::getDueDate, RegisterDTO::setDueDate));

		typeMap.addMappings(mapper -> mapper.using(parseToStringConverter).map(Register::getPaymentDate,
				RegisterDTO::setPaymentDate));
	}

	private void mapToAssemblePerson(ModelMapper modelMapper) {
		var typeMap = modelMapper.createTypeMap(PersonDTOUpdate.class, Person.class);
		Condition<Boolean, Boolean> nullActiveProperty = ctx -> ctx.getSource() == null;
		typeMap.addMappings(
				mapper -> mapper.when(nullActiveProperty).skip(PersonDTOUpdate::isActive, Person::setActive));
	}

}
