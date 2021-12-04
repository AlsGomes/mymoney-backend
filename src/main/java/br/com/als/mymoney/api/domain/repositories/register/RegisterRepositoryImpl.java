package br.com.als.mymoney.api.domain.repositories.register;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.als.mymoney.api.domain.model.Register;
import br.com.als.mymoney.api.domain.repositories.filters.RegisterFilter;

public class RegisterRepositoryImpl implements RegisterRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<Register> search(RegisterFilter filter) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Register> criteria = builder.createQuery(Register.class);
		Root<Register> root = criteria.from(Register.class);

		Predicate[] predicates = createPredicates(root, builder, filter);

		criteria.where(predicates);

		TypedQuery<Register> query = manager.createQuery(criteria);
		return query.getResultList();
	}

	private Predicate[] createPredicates(Root<Register> root, CriteriaBuilder builder, RegisterFilter filter) {
		List<Predicate> predicates = new ArrayList<>();

		predicates.add(builder.equal(root.get("person"), filter.getPersonId()));

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

}
