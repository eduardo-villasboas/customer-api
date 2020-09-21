package com.villasboas.clock;

import java.time.LocalDate;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

@Service
public class ClockService implements Clock {

	@Override
	public LocalDate getUtcLocalDate() {
		return LocalDate.now(ZoneOffset.UTC);
	}

}
