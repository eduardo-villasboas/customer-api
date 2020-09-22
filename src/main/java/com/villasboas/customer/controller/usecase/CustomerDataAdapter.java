package com.villasboas.customer.controller.usecase;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerDataAdapter {

	Page<CustomerDto> findAll(Optional<String> filter, Pageable pageRequest,
			Function<Customer, CustomerDto> converter);

	void insert(CustomerDto customerDto);

	CustomerDto findById(UUID id, Function<Customer, CustomerDto> entityToDtoMapperFunction);

	void delete(UUID id);

	void update(CustomerDto customerDto);

}
