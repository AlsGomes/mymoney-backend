package br.com.als.mymoney.api.domain.repositories.register;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.als.mymoney.api.domain.model.Register;
import br.com.als.mymoney.api.domain.model.dto.statistics.RegisterStatisticsByCategory;
import br.com.als.mymoney.api.domain.model.dto.statistics.RegisterStatisticsByDay;
import br.com.als.mymoney.api.domain.repositories.filters.RegisterFilter;

public class RegisterRepositoryImpl implements RegisterRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<Register> search(RegisterFilter filter, Pageable pageable) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Register> criteria = builder.createQuery(Register.class);
		Root<Register> root = criteria.from(Register.class);

		Predicate[] predicates = createPredicates(root, builder, filter);

		criteria.where(predicates);

		TypedQuery<Register> query = manager.createQuery(criteria);
		addPaginationConstraints(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, totalRecords(filter));
	}

	@Override
	public List<RegisterStatisticsByCategory> byCategory(LocalDate monthReference) {
		var criteriaBuilder = manager.getCriteriaBuilder();
		var criteriaQuery = criteriaBuilder.createQuery(RegisterStatisticsByCategory.class);
		var root = criteriaQuery.from(Register.class);

		criteriaQuery.select(criteriaBuilder.construct(RegisterStatisticsByCategory.class,
				root.get("category"),
				criteriaBuilder.sum(root.get("value"))));

		var firstDay = monthReference.withDayOfMonth(1);
		var lastDay = monthReference.withDayOfMonth(monthReference.lengthOfMonth());
		criteriaQuery.where(
				criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), firstDay),
				criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), lastDay));

		criteriaQuery.groupBy(root.get("category"));

		return manager.createQuery(criteriaQuery).getResultList();
	}

	@Override
	public List<RegisterStatisticsByDay> byDay(LocalDate monthReference) {
		var criteriaBuilder = manager.getCriteriaBuilder();
		var criteriaQuery = criteriaBuilder.createQuery(RegisterStatisticsByDay.class);
		var root = criteriaQuery.from(Register.class);

		criteriaQuery.select(criteriaBuilder.construct(RegisterStatisticsByDay.class,
				root.get("type"),
				root.get("dueDate"),
				criteriaBuilder.sum(root.get("value"))));

		var firstDay = monthReference.withDayOfMonth(1);
		var lastDay = monthReference.withDayOfMonth(monthReference.lengthOfMonth());
		criteriaQuery.where(
				criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), firstDay),
				criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), lastDay));

		criteriaQuery.groupBy(
				root.get("type"),
				root.get("dueDate"));

		return manager.createQuery(criteriaQuery).getResultList();
	}

	private Predicate[] createPredicates(Root<Register> root, CriteriaBuilder builder, RegisterFilter filter) {
		List<Predicate> predicates = new ArrayList<>();

		if (filter.getDescription() != null && !filter.getDescription().trim().isEmpty()) {
			predicates.add(builder.like(root.get("description"), "%" + filter.getDescription() + "%"));
		}

		if (filter.getDueDateFrom() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("dueDate"), filter.getDueDateFrom()));
		}

		if (filter.getDueDateUntil() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("dueDate"), filter.getDueDateUntil()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void addPaginationConstraints(TypedQuery<Register> query, Pageable pageable) {
		int actualPage = pageable.getPageNumber();
		int pageSize = pageable.getPageSize();
		int firstResult = actualPage * pageSize;

		query.setFirstResult(firstResult);
		query.setMaxResults(pageSize);
	}

	private Long totalRecords(RegisterFilter filter) {
		var builder = manager.getCriteriaBuilder();
		var criteria = builder.createQuery(Long.class);
		var root = criteria.from(Register.class);

		var predicates = createPredicates(root, builder, filter);
		criteria.where(predicates);

		criteria.select(builder.count(root));

		return manager.createQuery(criteria).getSingleResult();
	}

}
