package br.com.als.mymoney.api.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.als.mymoney.api.domain.model.City;
import br.com.als.mymoney.api.domain.model.dto.CitySummary;
import br.com.als.mymoney.api.domain.repositories.CityRepository;
import br.com.als.mymoney.api.domain.services.exceptions.ObjectNotFoundException;

@Service
public class CityService {

	@Autowired
	private CityRepository repository;

	@Transactional(readOnly = true)
	public City findById(Long id) {
		if (id == null)
			throw new ObjectNotFoundException("Cidade não encontrada");

		City obj = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Cidade não encontrada"));
		return obj;
	}

	@Transactional(readOnly = true)
	public City findByIbge(Integer ibge) {
		if (ibge == null)
			throw new ObjectNotFoundException("Cidade não encontrada");

		City obj = repository.findByIbge(ibge).orElseThrow(() -> new ObjectNotFoundException("Cidade não encontrada"));
		return obj;
	}

	@Transactional(readOnly = true)
	public List<CitySummary> findByStateId(Long stateId) {
		if (stateId == null)
			throw new ObjectNotFoundException("Cidade não encontrada");

		var cities = repository.findByStateId(stateId);
		var citiesSummary = cities.stream().map(CitySummary::new).collect(Collectors.toList());

		return citiesSummary;
	}
}
