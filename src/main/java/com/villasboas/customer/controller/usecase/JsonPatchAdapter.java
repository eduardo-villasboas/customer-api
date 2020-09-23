package com.villasboas.customer.controller.usecase;

public interface JsonPatchAdapter {

	CustomerBean applyPatchToCustomer(CustomerBean customer);
	
}
