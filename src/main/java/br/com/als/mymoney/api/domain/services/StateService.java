package br.com.als.mymoney.api.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.als.mymoney.api.domain.model.State;
import br.com.als.mymoney.api.domain.model.dto.StateSummary;
import br.com.als.mymoney.api.domain.repositories.StateRepository;
import br.com.als.mymoney.api.domain.services.exceptions.ObjectNotFoundException;

@Service
public class StateService {

	@Autowired
	private StateRepository repository;

	@Transactional(readOnly = true)
	public State findById(Long id) {
		if (id == null)
			throw new ObjectNotFoundException("Estado não encontrado");

		State obj = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Estado não encontrado"));
		return obj;
	}

	@Transactional(readOnly = true)
	public State findByIbge(Integer ibge) {
		if (ibge == null)
			throw new ObjectNotFoundException("Estado não encontrado");

		State obj = repository.findByIbge(ibge).orElseThrow(() -> new ObjectNotFoundException("Estado não encontrado"));
		return obj;
	}

	@Transactional(readOnly = true)
	public List<StateSummary> findByCountryId(Long countryId) {
		if (countryId == null)
			throw new ObjectNotFoundException("Estado não encontrado");

		var states = repository.findByCountryId(countryId);
		var statesSummary = states.stream().map(StateSummary::new).collect(Collectors.toList());

		return statesSummary;
	}
}
