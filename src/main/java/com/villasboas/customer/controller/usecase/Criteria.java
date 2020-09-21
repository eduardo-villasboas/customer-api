package com.villasboas.customer.controller.usecase;

import java.util.Objects;

public class Criteria {

	private final Operation operation;
	private final String fieldTableName;
	private final String value;

	public Criteria(final Operation operation, final String fieldTableName, final String value) {
		this.operation = operation;
		this.fieldTableName = fieldTableName;
		this.value = value;
	}

	@Override
	public String toString() {
		return "Criteria [operation=" + operation + ", fieldTableName=" + fieldTableName + ", value=" + value + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(fieldTableName, operation, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Criteria other = (Criteria) obj;
		return Objects.equals(fieldTableName, other.fieldTableName) && operation == other.operation
				&& Objects.equals(value, other.value);
	}

	public Operation getOperation() {
		return operation;
	}

	public String getFieldTableName() {
		return fieldTableName;
	}

	public String getValue() {
		return value;
	}

}
