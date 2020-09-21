package com.villasboas.customer.controller.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.villasboas.clock.Clock;
import com.villasboas.customer.database.CustomerEntity;

class CustomerServiceTest {

	private CustomerService customerService;

	private CustomerTestDataAdapter customerDataAdapter;

	private Clock clock;

	@BeforeEach
	void setup() {
		clock = mock(Clock.class);
		LocalDate bithDateIn2020 = LocalDate.of(2020, 1, 29);
		when(clock.getUtcLocalDate()).thenReturn(bithDateIn2020);

		customerDataAdapter = spy(new CustomerTestDataAdapter());
		SpecificationFactory specificationFactory = mock(SpecificationFactory.class);
		when(specificationFactory.factory(any())).thenReturn(new Specification<CustomerEntity>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<CustomerEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				throw new UnsupportedOperationException("Method not implemented yet.");
			}
		});
		
		customerService = new CustomerService(customerDataAdapter, specificationFactory, clock);
	}

	@Test
	void testWhenFindAllThenCallRepositoryGetAllEntitiesAndMappToDtoWithYearCalculated() {

		final PageRequest pageable = PageRequest.of(0, 4);
		final Optional<String> filter = Optional.empty();
		
		final Page<CustomerDto> customersPage = customerService.findAll(filter, pageable);

		final List<CustomerDto> content = customersPage.getContent();
		assertThat(content).hasSize(1);

		final CustomerDto customerDto = content.get(0);
		final List<CustomerEntity> customerEntities = customerDataAdapter.getCustomers();
		final CustomerEntity customer = customerEntities.get(0);

		assertThat(customerDto.getId()).isEqualTo(customer.getId());
		assertThat(customerDto.getCpf()).isEqualTo(customer.getCpf());
		assertThat(customerDto.getBirthDate()).isEqualTo(customer.getBirthDate());
		assertThat(customerDto.getName()).isEqualTo(customer.getName());
		assertThat(customerDto.getYearsOld()).isEqualTo((short)2);

		verify(customerDataAdapter, times(1)).findAll(nullableSafeSpecificationMatcher(), eq(pageable), notNull());

	}
	
	@Test
	void testWhenFindAllThenBildAnSpecification() {
		final PageRequest pageable = PageRequest.of(1, 3);
		final Optional<String> filter = Optional.of("filter-value");
		
		customerService.findAll(filter, pageable);
		
		verify(customerDataAdapter, times(1)).findAll(notNull(), eq(pageable), notNull());
	}
	
	@SuppressWarnings("unchecked")
	private Specification<CustomerEntity> nullableSafeSpecificationMatcher() {
		return nullable(Specification.class);
	}

}
