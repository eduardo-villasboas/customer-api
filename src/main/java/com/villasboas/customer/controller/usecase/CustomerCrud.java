package com.villasboas.customer.controller.usecase;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerCrud {

	Page<CustomerDto> findAll(Optional<String> filter, Pageable pageable);

	void insert(CustomerDto customerDto);

	CustomerDto findById(UUID id);

	void delete(UUID id);

	void update(CustomerDto customerDto);

	CustomerDto patch(UUID id, JsonPatchAdapter jsonPatchAdapter);

}
