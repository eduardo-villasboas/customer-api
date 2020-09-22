package com.villasboas.customer.database;

import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationFactory {

	Specification<CustomerEntity> factory(Optional<String> filter);

}
