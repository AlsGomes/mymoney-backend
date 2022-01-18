package br.com.als.mymoney.api.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.als.mymoney.api.domain.model.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
	Optional<Country> findById(Long id);

	Optional<Country> findByBacen(Integer bacen);
}
