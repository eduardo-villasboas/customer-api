package com.villasboas.customer.database;

import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.villasboas.customer.usecase.Criteria;

@Service
class SpecificationFactoryService implements SpecificationFactory {

	private static final int VALUE = 1;
	private static final int FIELD_NAME = 0;

	/*
	 * TODO: Retornar uma lista de CustomerEntitySpecifications nesse méthod para
	 * que possa ter mais de um filter por vez.
	 * 
	 */
	@Override
	public CustomerEntitySpecification factory(final Optional<String> filter) {
		if (filter.isEmpty()) {
			return null;
		}
		
		final String filterAsString = filter.get();
		final String[] fieldNameAndValue = filterAsString.split("=");
		final Criteria criteria;
		Preconditions.checkState(fieldNameAndValue.length == 2, "Error. Invalid filter {}", filterAsString);
		if ("name".equals(fieldNameAndValue[FIELD_NAME])) {
			criteria = new Criteria(Operation.Like, fieldNameAndValue[FIELD_NAME], fieldNameAndValue[VALUE]);
		} else if ("cpf".equals(fieldNameAndValue[FIELD_NAME])) {
			criteria = new Criteria(Operation.Equals, fieldNameAndValue[FIELD_NAME], fieldNameAndValue[VALUE]);
		} else {
			throw new IllegalStateException(String.format("Error. Invalid filter %s", filterAsString));
		}
		return new CustomerEntitySpecification(criteria);
	}
	
	public class CustomerEntitySpecification implements Specification<CustomerEntity> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -89231897017484031L;

		private final Criteria criteria;
		
		CustomerEntitySpecification(final Criteria criteriaList) {
			this.criteria = criteriaList;
		}
		
		@Override
		public Predicate toPredicate(Root<CustomerEntity> root, CriteriaQuery<?> query,
				CriteriaBuilder criteriaBuilder) {
			
			if (criteria.getOperation() == Operation.Like) {
				return criteriaBuilder.like(root.<String>get(criteria.getFieldTableName()), "%"+criteria.getValue()+"%");
			} else if (criteria.getOperation() == Operation.Equals) {
				return criteriaBuilder.equal(root.get(criteria.getFieldTableName()), criteria.getValue());
			} else {
				return null;
			}
		}

		public Criteria getCriteria() {
			return criteria;
		}
		
	}

}
