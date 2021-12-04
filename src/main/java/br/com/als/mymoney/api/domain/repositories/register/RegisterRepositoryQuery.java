package br.com.als.mymoney.api.domain.repositories.register;

import java.util.List;

import br.com.als.mymoney.api.domain.model.Register;
import br.com.als.mymoney.api.domain.repositories.filters.RegisterFilter;

public interface RegisterRepositoryQuery {

	List<Register> search(RegisterFilter filter);
}
