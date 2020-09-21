package com.villasboas.customer.controller.usecase;

import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

import com.villasboas.customer.database.CustomerEntity;

public interface SpecificationFactory {

	Specification<CustomerEntity> factory(Optional<String> filter);

}
