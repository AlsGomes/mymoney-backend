package br.com.als.mymoney.api.domain.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import lombok.Getter;

@Getter
public class PersonDTOUpdateContacts {

	@Valid
	private final List<ContactDTOUpdate> contacts = new ArrayList<>();
}
