package com.villasboas.customer.controller.usecase;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerDataAdapter {

	Page<CustomerBean> findAll(Optional<String> filter, Pageable pageRequest,
			Function<Customer, CustomerBean> converter);

	void insert(CustomerBean customerDto);

	CustomerBean findById(UUID id, Function<Customer, CustomerBean> entityToDtoMapperFunction);

	void delete(UUID id);

	void update(CustomerBean customerDto);

}
