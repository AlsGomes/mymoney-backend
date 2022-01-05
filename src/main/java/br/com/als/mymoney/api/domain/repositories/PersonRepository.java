package br.com.als.mymoney.api.domain.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.als.mymoney.api.domain.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
	Optional<Person> findByCode(String code);

	Page<Person> findByNameContaining(String name, Pageable pageable);
}
