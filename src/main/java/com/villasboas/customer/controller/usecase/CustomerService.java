package com.villasboas.customer.controller.usecase;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.villasboas.clock.Clock;
import com.villasboas.customer.database.CustomerEntity;

@Service
class CustomerService implements Customer {

	private final CustomerDataAdapter customerDataAdapter;
	private final Function<CustomerEntity, CustomerDto> mapperFunction;
	private final SpecificationFactory specificationFactory;

	CustomerService(final CustomerDataAdapter customerDataAdapter, 
			SpecificationFactory specificationFactory, Clock clock) {
		this.customerDataAdapter = customerDataAdapter;
		this.specificationFactory = specificationFactory;

		mapperFunction = (customerEntity) -> {
			CustomerDto customerDto = new CustomerDto();

			customerDto.setId(customerEntity.getId());
			customerDto.setCpf(customerEntity.getCpf());
			customerDto.setBirthDate(customerEntity.getBirthDate());
			customerDto.setName(customerEntity.getName());

			Long yearsOld = calculeYearsOld(clock, customerEntity);
			customerDto.setYearsOld(yearsOld.shortValue());
			return customerDto;
		};

	}

	private static Long calculeYearsOld(Clock clock, CustomerEntity customerEntity) {
		LocalDate utcLocalDateNow = clock.getUtcLocalDate();
		LocalDate birthDate = customerEntity.getBirthDate();
		return ChronoUnit.YEARS.between(birthDate, utcLocalDateNow);
	}

	@Override
	public Page<CustomerDto> findAll(Optional<String> filter, Pageable pageable) {
		return customerDataAdapter.findAll(specificationFactory.factory(filter), pageable, mapperFunction);
	}

	@Override
	public void insert(CustomerDto customerDto) {
		customerDataAdapter.insert(customerDto);
	}

}
