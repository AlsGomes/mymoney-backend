package br.com.als.mymoney.api.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.als.mymoney.api.domain.model.State;

public interface StateRepository extends JpaRepository<State, Long> {
	Optional<State> findById(Long id);

	Optional<State> findByIbge(Integer ibge);

	List<State> findByCountryId(Long countryId);
}
