package com.villasboas.customer.database;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.villasboas.customer.controller.usecase.Criteria;

class SpecificationFactoryServiceTest {

	private SpecificationFactoryService specificationFactoryService;

	@BeforeEach
	void setup() {
		specificationFactoryService = new SpecificationFactoryService();
	}

	@Test
	void testWhenFactoryWithEmptyOptionalThenReturnsNull() {

		final var specification = specificationFactoryService.factory(Optional.empty());

		assertThat(specification).isNull();

	}

	@Test
	void testWhenFacotyWithNameThenCreateNameSpecificationOfLike() {

		final SpecificationFactoryService.CustomerEntitySpecification specification = specificationFactoryService
				.factory(Optional.of("name=Al"));

		assertThat(specification).isNotNull();
		assertThat(specification.getCriteria())
				.isEqualTo(new Criteria(Operation.Like, "name", "Al"));
	}

	@Test
	void testWhenFacotyWithCpfThenCreateCpfSpecficationOfEquals() {
		
		final SpecificationFactoryService.CustomerEntitySpecification specification = specificationFactoryService
				.factory(Optional.of("cpf=26545587030"));
		
		assertThat(specification).isNotNull();
		assertThat(specification.getCriteria())
		.isEqualTo(new Criteria(Operation.Equals, "cpf", "26545587030"));
	}

}
