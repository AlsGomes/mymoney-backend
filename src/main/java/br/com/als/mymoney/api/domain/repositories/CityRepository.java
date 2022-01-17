package br.com.als.mymoney.api.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.als.mymoney.api.domain.model.City;

public interface CityRepository extends JpaRepository<City, Long> {
	Optional<City> findById(Long id);
}
