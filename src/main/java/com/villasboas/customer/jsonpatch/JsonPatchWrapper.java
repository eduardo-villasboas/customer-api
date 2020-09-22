package com.villasboas.customer.jsonpatch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.villasboas.common.exception.ErrorWhenApplyPatchException;
import com.villasboas.customer.controller.usecase.CustomerDto;
import com.villasboas.customer.controller.usecase.JsonPatchAdapter;

public class JsonPatchWrapper implements JsonPatchAdapter {

	private final JsonPatch patch;
	private final ObjectMapper objectMapper;

	public JsonPatchWrapper(final JsonPatch patch, final ObjectMapper objectMapper) {
		this.patch = patch;
		this.objectMapper = objectMapper;
	}

	@Override
	public CustomerDto applyPatchToCustomer(CustomerDto customer) {
		try {
			final JsonNode patched = patch.apply(objectMapper.convertValue(customer, JsonNode.class));
			return objectMapper.treeToValue(patched, CustomerDto.class);
		} catch (JsonProcessingException | JsonPatchException e) {
			throw new ErrorWhenApplyPatchException(
					String.format("Error when apply patch %s to customer %s", patch.toString(), customer.toString()));
		}
	}

}
