package com.villasboas.customer.usecase;

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
import com.villasboas.customer.usecase.Customer;
import com.villasboas.customer.usecase.CustomerBean;
import com.villasboas.customer.usecase.CustomerDataAdapter;

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
	public Page<CustomerBean> findAll(Optional<String> filter, Pageable pageRequest,
			Function<Customer, CustomerBean> converter) {
		Page<CustomerEntity> customerPage = new PageImpl<>(customers);
		return customerPage.map(converter);
	}

	@Override
	public void insert(CustomerBean customerDto) {
		throw new UnsupportedOperationException("Method not implemented yet.");
	}

	@Override
	public CustomerBean findById(UUID id, Function<Customer, CustomerBean> entityToDtoMapperFunction) {
		throw new UnsupportedOperationException("Method not implemented yet.");
	}

	@Override
	public void delete(UUID id) {
		throw new UnsupportedOperationException("Method not implemented yet.");
	}

	@Override
	public void update(CustomerBean customerDto) {
		throw new UnsupportedOperationException("Method not implemented yet.");
	}

}
