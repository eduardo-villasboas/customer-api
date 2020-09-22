package com.villasboas.customer.controller.usecase;

import java.time.LocalDate;
import java.util.UUID;

public interface Customer {

	UUID getId();

	String getCpf();

	LocalDate getBirthDate();

	String getName();

}
