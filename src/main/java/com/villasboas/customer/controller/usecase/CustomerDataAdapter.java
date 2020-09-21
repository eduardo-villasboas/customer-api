package com.villasboas.customer.controller.usecase;

import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.villasboas.customer.database.CustomerEntity;

public interface CustomerDataAdapter {

	Page<CustomerDto> findAll(Specification<CustomerEntity> specification, Pageable pageRequest,
			Function<CustomerEntity, CustomerDto> converter);

	void insert(CustomerDto customerDto);

}
