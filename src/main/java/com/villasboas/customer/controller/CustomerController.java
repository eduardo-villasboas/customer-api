package com.villasboas.customer.controller;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.villasboas.customer.jsonpatch.JsonPatchWrapper;
import com.villasboas.customer.usecase.CustomerBean;
import com.villasboas.customer.usecase.CustomerCrud;

@RestController
@RequestMapping("/api/customers")
@ResponseBody
class CustomerController {

	private final CustomerCrud customerCrud;
	private final ObjectMapper objectMapper;
	private final Function<CustomerDto, CustomerBean> dtoToBeanMapper;
	private final Function<CustomerBean, CustomerDto> beanToDtoMapper;

	public CustomerController(final CustomerCrud customer, final ObjectMapper objectMapper,
			final ModelMapper modelMapper) {
		this.customerCrud = customer;
		this.objectMapper = objectMapper;
		dtoToBeanMapper = (dto) -> {
			return modelMapper.map(dto, CustomerBean.class);
		};
		beanToDtoMapper = (bean) -> {
			return modelMapper.map(bean, CustomerDto.class);
		};
	}

	@PostMapping
	ResponseEntity<URI> insert(@RequestBody @Valid CustomerDto customerDto) {
		customerCrud.insert(dtoToBeanMapper.apply(customerDto));
		String uri = String.format("/api/customers/%s", customerDto.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(URI.create(uri));
	}

	@GetMapping
	ResponseEntity<Page<CustomerDto>> findAll(@RequestParam("filter") Optional<String> filter, Pageable pageable) {
		return ResponseEntity.ok(customerCrud.findAll(filter, pageable).map(beanToDtoMapper));
	}

	@GetMapping("/{id}")
	ResponseEntity<CustomerDto> findById(@PathVariable("id") UUID id) {
		return ResponseEntity.ok(beanToDtoMapper.apply(customerCrud.findById(id)));
	}

	@DeleteMapping("/{id}")
	ResponseEntity<?> delete(@PathVariable("id") UUID id) {
		customerCrud.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping
	ResponseEntity<?> update(@RequestBody @Valid CustomerDto customerDto) {
		customerCrud.update(dtoToBeanMapper.apply(customerDto));
		return ResponseEntity.noContent().build();
	}

	@PatchMapping(path = "/{id}")
	public ResponseEntity<CustomerDto> patch(@PathVariable("id") UUID id, @RequestBody JsonPatch patch) {
		return ResponseEntity
				.ok(beanToDtoMapper.apply(customerCrud.patch(id, new JsonPatchWrapper(patch, objectMapper))));
	}

}
