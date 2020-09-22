package com.villasboas.customer.controller.usecase;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.villasboas.clock.Clock;

@Service
class CustomerCrudService implements CustomerCrud {

	private final CustomerDataAdapter customerDataAdapter;
	private final Function<Customer, CustomerDto> entityToDtoMapperFunction;
	private final Clock clock;

	CustomerCrudService(final CustomerDataAdapter customerDataAdapter,
			final Clock clock) {
		this.customerDataAdapter = customerDataAdapter;
		this.clock = clock;

		entityToDtoMapperFunction = (customerEntity) -> {
			CustomerDto customerDto = new CustomerDto();

			customerDto.setId(customerEntity.getId());
			customerDto.setCpf(customerEntity.getCpf());
			customerDto.setBirthDate(customerEntity.getBirthDate());
			customerDto.setName(customerEntity.getName());

			Long yearsOld = calculeYearsOld(clock.getUtcLocalDate(), customerEntity.getBirthDate());
			customerDto.setYearsOld(yearsOld.shortValue());
			return customerDto;
		};

	}

	private static Long calculeYearsOld(LocalDate utcLocalDateNow, LocalDate birthDate) {
		return ChronoUnit.YEARS.between(birthDate, utcLocalDateNow);
	}

	@Override
	public Page<CustomerDto> findAll(Optional<String> filter, Pageable pageable) {
		return customerDataAdapter.findAll(filter, pageable, entityToDtoMapperFunction);
	}

	@Override
	public void insert(CustomerDto customerDto) {
		customerDataAdapter.insert(customerDto);
	}

	@Override
	public CustomerDto findById(UUID id) {
		return customerDataAdapter.findById(id, entityToDtoMapperFunction);
	}

	@Override
	public void delete(UUID id) {
		customerDataAdapter.delete(id);
	}

	@Override
	public void update(CustomerDto customerDto) {
		customerDataAdapter.update(customerDto);
	}

	@Override
	public CustomerDto patch(final UUID id, final JsonPatchAdapter jsonPatchAdapter) {
		final CustomerDto customer = customerDataAdapter.findById(id, entityToDtoMapperFunction);
		final CustomerDto patchedCustomer = jsonPatchAdapter.applyPatchToCustomer(customer);
		update(patchedCustomer);
		patchedCustomer
				.setYearsOld(calculeYearsOld(clock.getUtcLocalDate(), patchedCustomer.getBirthDate()).shortValue());
		return patchedCustomer;
	}

}
