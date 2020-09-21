package com.villasboas.customer.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.villasboas.customer.controller.usecase.Customer;
import com.villasboas.customer.controller.usecase.CustomerDto;

@RestController
@RequestMapping("/api/customers")
@ResponseBody
class CustomerController {

	private final Customer customer;

	public CustomerController(final Customer customer) {
		this.customer = customer;
	}

	@PostMapping
	ResponseEntity<URI> insert(@RequestBody CustomerDto customerDto) {
		customer.insert(customerDto);
		String uri = String.format("/api/customers/%s", customerDto.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(URI.create(uri));
	}

	@GetMapping
	ResponseEntity<Page<CustomerDto>> findAll(@RequestParam("filter") Optional<String> filter, Pageable pageable) {
		return ResponseEntity.ok(customer.findAll(filter, pageable));
	}

}
