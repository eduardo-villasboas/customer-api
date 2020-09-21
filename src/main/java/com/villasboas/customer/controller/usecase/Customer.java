package com.villasboas.customer.controller.usecase;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface Customer {

	Page<CustomerDto> findAll(Optional<String> filter, Pageable pageable);

	void insert(CustomerDto customerDto);

}
