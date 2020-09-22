package com.villasboas.customer.controller.usecase;

public interface JsonPatchAdapter {

	CustomerDto applyPatchToCustomer(CustomerDto customer);
	
}
