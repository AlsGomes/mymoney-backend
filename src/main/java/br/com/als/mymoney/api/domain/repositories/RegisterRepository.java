package br.com.als.mymoney.api.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.als.mymoney.api.domain.model.Register;
import br.com.als.mymoney.api.domain.repositories.register.RegisterRepositoryQuery;

public interface RegisterRepository extends JpaRepository<Register, Long>, RegisterRepositoryQuery {

	Optional<Register> findByCode(String code);
}
