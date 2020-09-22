package com.villasboas.customer.controller.usecase;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.assertj.core.util.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.villasboas.customer.database.CustomerEntity;

public class CustomerTestDataAdapter implements CustomerDataAdapter {

	private final List<CustomerEntity> customers;

	public CustomerTestDataAdapter() {
		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setId(UUID.randomUUID());
		customerEntity.setName("Elijah");
		customerEntity.setCpf("26545587030");
		customerEntity.setBirthDate(LocalDate.of(2018, 1, 29));
		customers = Lists.newArrayList(customerEntity);
	}

	List<CustomerEntity> getCustomers() {
		return customers;
	}

	@Override
	public Page<CustomerDto> findAll(Optional<String> filter, Pageable pageRequest,
			Function<Customer, CustomerDto> converter) {
		Page<CustomerEntity> customerPage = new PageImpl<>(customers);
		return customerPage.map(converter);
	}

	@Override
	public void insert(CustomerDto customerDto) {
		throw new UnsupportedOperationException("Method not implemented yet.");
	}

	@Override
	public CustomerDto findById(UUID id, Function<Customer, CustomerDto> entityToDtoMapperFunction) {
		throw new UnsupportedOperationException("Method not implemented yet.");
	}

	@Override
	public void delete(UUID id) {
		throw new UnsupportedOperationException("Method not implemented yet.");
	}

	@Override
	public void update(CustomerDto customerDto) {
		throw new UnsupportedOperationException("Method not implemented yet.");
	}

}
