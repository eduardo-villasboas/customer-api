package com.villasboas.customer.database;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.villasboas.common.exception.NotFoundEntityException;
import com.villasboas.customer.controller.usecase.Customer;
import com.villasboas.customer.controller.usecase.CustomerBean;
import com.villasboas.customer.controller.usecase.CustomerDataAdapter;

@Service
class CustomerDataAdapterService implements CustomerDataAdapter {

	private final CustomerRepository customerRepository;
	private final Function<CustomerBean, CustomerEntity> dtoToEntityMapper;
	private final SpecificationFactory specificationFactory;
	
	CustomerDataAdapterService(final CustomerRepository customerRepository, 
			final SpecificationFactory specificationFactory) {
		this.customerRepository = customerRepository;
		this.specificationFactory = specificationFactory;
		dtoToEntityMapper = createDtoToEntityMapper();
	}

	private static Function<CustomerBean, CustomerEntity> createDtoToEntityMapper() {
		return customerDto -> {
			final CustomerEntity customerEntity = new CustomerEntity();
			customerEntity.setId(customerDto.getId());
			customerEntity.setBirthDate(customerDto.getBirthDate());
			customerEntity.setName(customerDto.getName());
			customerEntity.setCpf(customerDto.getCpf());
			return customerEntity;
		};
	}

	@Override
	public Page<CustomerBean> findAll(Optional<String> filter, Pageable pageRequest,
			Function<Customer, CustomerBean> converter) {
		return customerRepository.findAll(specificationFactory.factory(filter), pageRequest).map(converter);
	}

	@Override
	public void insert(CustomerBean customerDto) {
		final CustomerEntity customerEntityToSave = dtoToEntityMapper.apply(customerDto);
		customerRepository.save(customerEntityToSave);
	}

	@Override
	public CustomerBean findById(UUID id, Function<Customer, CustomerBean> entityToDtoMapperFunction){
		final CustomerEntity customerEntity = customerRepository.findById(id)
				.orElseThrow(() -> new NotFoundEntityException("Entidade não encontrada."));
		return entityToDtoMapperFunction.apply(customerEntity);
	}

	@Override
	public void delete(UUID id) {
		customerRepository.deleteById(id);
	}

	@Override
	public void update(CustomerBean customerDto) {
		customerRepository.save(dtoToEntityMapper.apply(customerDto));
	}


}
