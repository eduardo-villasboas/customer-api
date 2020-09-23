package com.villasboas.customer.database;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.villasboas.customer.usecase.Customer;
import com.villasboas.customer.usecase.CustomerBean;

class CustomerDataAdapterServiceTest {

	private CustomerDataAdapterService customerDataAdapterService;
	private CustomerRepository customerRepository;

	@BeforeEach
	void setup() {
		customerRepository = mock(CustomerRepository.class);

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

		when(customerRepository.findAll(safeSpecificationMatcher(), any(Pageable.class)))
				.thenReturn(new PageImpl<>(createListOfCustomers()));

		customerDataAdapterService = new CustomerDataAdapterService(customerRepository, specificationFactory);
	}

	private List<CustomerEntity> createListOfCustomers() {
		return Lists.newArrayList(createCustomer());
	}

	private CustomerEntity createCustomer() {
		return new CustomerEntity();
	}

	@Test
	void testWehFindThenMapCustomersToCustomerEntity() {

		final Function<Customer, CustomerBean> mapperFunction = e -> new CustomerBean();
		final PageRequest pageable = PageRequest.of(0, 2);

		final Page<CustomerBean> customersDto = customerDataAdapterService.findAll(Optional.empty(), pageable,
				mapperFunction);

		assertThat(customersDto).isNotNull();
		verify(customerRepository, times(1)).findAll(safeSpecificationMatcher(), eq(pageable));
	}

	@SuppressWarnings("unchecked")
	private Specification<CustomerEntity> safeSpecificationMatcher() {
		return any(Specification.class);
	}

	@Test
	void testWhenCallInsertThenMapToEntityAndSave() {

		final CustomerBean customerDto = new CustomerBean();
		final UUID randomUUID = UUID.randomUUID();
		customerDto.setId(randomUUID);
		customerDto.setName("Sol");
		customerDto.setCpf("14636100093");
		customerDto.setBirthDate(LocalDate.of(2016, 4, 15));

		customerDataAdapterService.insert(customerDto);

		final CustomerEntity expectedCustomerEntity = new CustomerEntity();
		expectedCustomerEntity.setId(customerDto.getId());
		expectedCustomerEntity.setName(customerDto.getName());
		expectedCustomerEntity.setCpf(customerDto.getCpf());
		expectedCustomerEntity.setBirthDate(customerDto.getBirthDate());

		verify(customerRepository, times(1)).save(eq(expectedCustomerEntity));
	}
}
