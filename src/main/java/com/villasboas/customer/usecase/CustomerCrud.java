package com.villasboas.customer.usecase;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerCrud {

	Page<CustomerBean> findAll(Optional<String> filter, Pageable pageable);

	void insert(CustomerBean customerDto);

	CustomerBean findById(UUID id);

	void delete(UUID id);

	void update(CustomerBean customerDto);

	CustomerBean patch(UUID id, JsonPatchAdapter jsonPatchAdapter);

}
