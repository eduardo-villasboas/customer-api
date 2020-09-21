package com.villasboas.customer.database;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.villasboas.customer.controller.usecase.CustomerDto;

class CustomerDataAdapterServiceTest {

	private CustomerDataAdapterService customerDataAdapterService;
	private CustomerRepository customerRepository;
	
	@BeforeEach
	void setup() {
		customerRepository = mock(CustomerRepository.class);
		
		when(customerRepository.findAll(safeSpecificationMatcher(), any(Pageable.class))).thenReturn(new PageImpl<>(createListOfCustomers()));
		
		customerDataAdapterService = new CustomerDataAdapterService(customerRepository);
	}

	private List<CustomerEntity> createListOfCustomers() {
		return Lists.newArrayList(createCustomer());
	}
	
	private CustomerEntity createCustomer() {
		return new CustomerEntity();
	}

	@Test
	void testWehFindThenMapCustomersToCustomerEntity() {
		
		final Function<CustomerEntity, CustomerDto> mapperFunction = e -> new CustomerDto();
		final PageRequest pageable = PageRequest.of(0, 2);
		final Specification<CustomerEntity> spec = safeSpecificationMock();
		
		final Page<CustomerDto> customersDto = customerDataAdapterService.findAll(spec, pageable, mapperFunction );
		
		assertThat(customersDto).isNotNull();
		verify(customerRepository, times(1)).findAll(safeSpecificationMatcher(), eq(pageable));
	}

	@SuppressWarnings("unchecked")
	private Specification<CustomerEntity> safeSpecificationMock() {
		return mock(Specification.class);
	}

	
	@SuppressWarnings("unchecked")
	private Specification<CustomerEntity> safeSpecificationMatcher() {
		return any(Specification.class);
	}

	@Test
	void testWhenCallInsertThenMapToEntityAndSave() {
		
		final CustomerDto customerDto = new CustomerDto();
		final UUID randomUUID = UUID.randomUUID();
		customerDto.setId(randomUUID);
		customerDto.setName("Sol");
		customerDto.setCpf("14636100093");
		customerDto.setBirthDate(LocalDate.of(2016, 4, 15));

		customerDataAdapterService.insert(customerDto );
		
		final CustomerEntity expectedCustomerEntity = new CustomerEntity();
		expectedCustomerEntity.setId(customerDto.getId());
		expectedCustomerEntity.setName(customerDto.getName());
		expectedCustomerEntity.setCpf(customerDto.getCpf());
		expectedCustomerEntity.setBirthDate(customerDto.getBirthDate());
	
		verify(customerRepository, times(1)).save(eq(expectedCustomerEntity));
	}
}
