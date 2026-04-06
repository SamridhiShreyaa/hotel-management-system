package com.pesu.hotel.common.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class DateUtils {

	private DateUtils() {
	}

	public static long daysBetween(LocalDate from, LocalDate to) {
		return ChronoUnit.DAYS.between(from, to);
	}
}
