package com.pesu.hotel.admin.service;

import java.util.List;

import com.pesu.hotel.admin.dto.ReportResponse;
import com.pesu.hotel.admin.dto.RoomManageRequest;

public interface AdminService {
	List<RoomManageRequest> getRooms();

	List<String> getReservations();

	List<String> getGuests();

	List<ReportResponse> getReports();
}
