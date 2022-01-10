package br.com.als.mymoney.api.domain.repositories.register;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.als.mymoney.api.domain.model.Register;
import br.com.als.mymoney.api.domain.model.RegisterFile;

public interface RegisterRepository extends JpaRepository<Register, Long>, RegisterRepositoryQuery {

	Optional<Register> findByCode(String code);

	List<Register> findByDueDateLessThanEqualAndPaymentDateIsNullOrderByType(LocalDate dueDate);

	@Query("from RegisterFile rf where rf.register.id = :registerId and rf.fileName = :fileName")
	Optional<RegisterFile> findRegisterFileByFileName(Long registerId, String fileName);
}
