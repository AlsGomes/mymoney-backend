package br.com.als.mymoney.api.domain.repositories.register;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.als.mymoney.api.domain.model.Register;
import br.com.als.mymoney.api.domain.repositories.filters.RegisterFilter;

public interface RegisterRepositoryQuery {

	Page<Register> search(RegisterFilter filter, Pageable pageable);
}
