package br.com.als.mymoney.api.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.als.mymoney.api.domain.model.Country;
import br.com.als.mymoney.api.domain.repositories.CountryRepository;
import br.com.als.mymoney.api.domain.services.exceptions.ObjectNotFoundException;

@Service
public class CountryService {

	@Autowired
	private CountryRepository repository;

	@Transactional(readOnly = true)
	public Country findById(Long id) {
		if (id == null)
			throw new ObjectNotFoundException("País não encontrado");

		Country obj = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("País não encontrado"));
		return obj;
	}

	@Transactional(readOnly = true)
	public Country findByBacen(Integer bacen) {
		if (bacen == null)
			throw new ObjectNotFoundException("País não encontrado");

		Country obj = repository.findByBacen(bacen)
				.orElseThrow(() -> new ObjectNotFoundException("País não encontrado"));
		return obj;
	}

	@Transactional(readOnly = true)
	public List<Country> findAll() {
		return repository.findAll();
	}
}
