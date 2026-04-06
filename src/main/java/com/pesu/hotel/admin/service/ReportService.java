package com.pesu.hotel.admin.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pesu.hotel.admin.dto.ReportResponse;

@Service
public class ReportService {

	public List<ReportResponse> getOccupancyReport() {
		return List.of(
				new ReportResponse("Occupancy Summary", 1, 1, BigDecimal.ZERO),
				new ReportResponse("Availability Summary", 0, 2, BigDecimal.ZERO));
	}

	public List<ReportResponse> getRevenueReport() {
		return List.of(
				new ReportResponse("Revenue Summary", 2, 0, new BigDecimal("12800.00")));
	}

	public List<String> getPaymentLog() {
		return List.of(
				"PAY-1001 | SUCCESS | UPI",
				"PAY-1002 | SUCCESS | CREDIT_CARD");
	}
}
