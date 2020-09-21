package com.villasboas.customer.database;

import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.villasboas.customer.controller.usecase.CustomerDataAdapter;
import com.villasboas.customer.controller.usecase.CustomerDto;

@Service
class CustomerDataAdapterService implements CustomerDataAdapter {

	private final CustomerRepository customerRepository;
	private final Function<CustomerDto, CustomerEntity> dtoToEntityMapper; 

	CustomerDataAdapterService(final CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
		dtoToEntityMapper = customerDto -> {
			final CustomerEntity customerEntity = new CustomerEntity();
			customerEntity.setId(customerDto.getId());
			customerEntity.setBirthDate(customerDto.getBirthDate());
			customerEntity.setName(customerDto.getName());
			customerEntity.setCpf(customerDto.getCpf());
			return customerEntity;
		};
	}

	@Override
	public Page<CustomerDto> findAll(Specification<CustomerEntity> specification, Pageable pageRequest,
			Function<CustomerEntity, CustomerDto> converter) {
		return customerRepository.findAll(specification, pageRequest).map(converter);
	}

	@Override
	public void insert(CustomerDto customerDto) {
		final CustomerEntity customerEntityToSave = dtoToEntityMapper.apply(customerDto);
		customerRepository.save(customerEntityToSave);
	}

}
