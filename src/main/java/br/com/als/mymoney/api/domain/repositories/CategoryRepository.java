package br.com.als.mymoney.api.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.als.mymoney.api.domain.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByCode(String code);
}
