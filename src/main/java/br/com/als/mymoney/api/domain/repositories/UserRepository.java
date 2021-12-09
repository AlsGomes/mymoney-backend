package br.com.als.mymoney.api.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.als.mymoney.api.domain.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByCode(String code);

	Optional<User> findByEmail(String email);
}
