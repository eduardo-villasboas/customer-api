package com.villasboas.customer.usecase;

public interface JsonPatchAdapter {

	CustomerBean applyPatchToCustomer(CustomerBean customer);
	
}
