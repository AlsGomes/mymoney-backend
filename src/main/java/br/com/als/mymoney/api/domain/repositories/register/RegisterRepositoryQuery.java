package br.com.als.mymoney.api.domain.repositories.register;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.als.mymoney.api.domain.model.Register;
import br.com.als.mymoney.api.domain.model.dto.statistics.RegisterStatisticsByCategory;
import br.com.als.mymoney.api.domain.model.dto.statistics.RegisterStatisticsByDay;
import br.com.als.mymoney.api.domain.repositories.filters.RegisterFilter;

public interface RegisterRepositoryQuery {

	Page<Register> search(RegisterFilter filter, Pageable pageable);

	List<RegisterStatisticsByCategory> byCategory(LocalDate monthReference);

	List<RegisterStatisticsByDay> byDay(LocalDate monthReference);
}
