package br.com.als.mymoney.api.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.als.mymoney.api.domain.model.Person;
import br.com.als.mymoney.api.domain.model.Register;
import br.com.als.mymoney.api.domain.model.dto.CategoryDTO;
import br.com.als.mymoney.api.domain.model.dto.PersonDTO;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTO;
import br.com.als.mymoney.api.domain.repositories.RegisterRepository;
import br.com.als.mymoney.api.domain.services.exceptions.ObjectNotFoundException;

@Service
public class RegisterService {

	@Autowired
	private RegisterRepository repository;

	@Autowired
	private PersonService personService;

	public List<RegisterDTO> findAll() {
		var list = repository.findAll();
		List<RegisterDTO> listDTO = list.stream().map(RegisterDTO::new).collect(Collectors.toList());
		return listDTO;
	}

	public RegisterDTO findByCodeOrThrow(String code) {
		if (code == null)
			throw new ObjectNotFoundException("Lançamento não encontrado");

		Register obj = repository.findByCode(code)
				.orElseThrow(() -> new ObjectNotFoundException("Lancamento não encontrado"));

		RegisterDTO objDTO = new RegisterDTO();
		// TODO Trocar a implementação do BeanUtils pela implementação do ModelMapper
		objDTO.setCategory(new CategoryDTO(obj.getCategory()));
		objDTO.setPerson(new PersonDTO(obj.getPerson()));
		BeanUtils.copyProperties(obj, objDTO);

		return objDTO;
	}

	public Register findByCodeOrThrowAsRegister(String code) {
		if (code == null)
			throw new ObjectNotFoundException("Lancamento não encontrado");

		Register obj = repository.findByCode(code)
				.orElseThrow(() -> new ObjectNotFoundException("Lançamento não encontrado"));

		return obj;
	}

	public List<RegisterDTO> findByPersonOrThrow(String code) {
		if (code == null)
			throw new ObjectNotFoundException("Código da Pessoa inválido");

		Person obj = personService.findByCodeOrThrowAsPerson(code);

		List<Register> list = repository.findByPerson(obj);
		List<RegisterDTO> listDTO = list.stream().map(RegisterDTO::new).collect(Collectors.toList());

		return listDTO;
	}
}
