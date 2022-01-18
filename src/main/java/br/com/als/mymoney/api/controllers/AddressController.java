package br.com.als.mymoney.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.als.mymoney.api.domain.model.City;
import br.com.als.mymoney.api.domain.model.State;
import br.com.als.mymoney.api.domain.model.dto.CitySummary;
import br.com.als.mymoney.api.domain.model.dto.StateSummary;
import br.com.als.mymoney.api.domain.services.CityService;
import br.com.als.mymoney.api.domain.services.StateService;

@RestController
@RequestMapping(path = "/address")
public class AddressController {

	@Autowired
	private CityService cityService;
	
	@Autowired
	private StateService stateService;

	private static final Long BRAZIL_ID = 1L;

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and hasAuthority('SCOPE_read')")
	@GetMapping("/states")
	public ResponseEntity<List<StateSummary>> findStates() {
		var states = stateService.findByCountryId(BRAZIL_ID);
		return ResponseEntity.ok().body(states);
	}

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and hasAuthority('SCOPE_read')")
	@GetMapping(path = "/states", params = "ibge")
	public ResponseEntity<State> findStateByIbge(Integer ibge) {
		var state = stateService.findByIbge(ibge);
		return ResponseEntity.ok().body(state);
	}

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and hasAuthority('SCOPE_read')")
	@GetMapping("/cities")
	public ResponseEntity<List<CitySummary>> findCities(Long stateId) {
		var cities = cityService.findByStateId(stateId);
		return ResponseEntity.ok().body(cities);
	}

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and hasAuthority('SCOPE_read')")
	@GetMapping(path = "/cities", params = "ibge")
	public ResponseEntity<City> findCityByIbge(Integer ibge) {
		var city = cityService.findByIbge(ibge);
		return ResponseEntity.ok().body(city);
	}
}
