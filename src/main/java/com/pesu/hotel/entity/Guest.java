package com.pesu.hotel.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "guests")
@PrimaryKeyJoinColumn(name = "user_id")
public class Guest extends User {

	@Column(length = 255)
	private String address;

	@Column(length = 100)
	private String nationality;

	@Enumerated(EnumType.STRING)
	@Column(name = "id_proof_type", length = 50)
	private IdProofType idProofType;

	@Column(name = "id_proof_no", length = 100)
	private String idProofNo;

	@OneToMany(mappedBy = "guest", fetch = FetchType.LAZY)
	private List<Reservation> reservations = new ArrayList<>();

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public IdProofType getIdProofType() {
		return idProofType;
	}

	public void setIdProofType(IdProofType idProofType) {
		this.idProofType = idProofType;
	}

	public String getIdProofNo() {
		return idProofNo;
	}

	public void setIdProofNo(String idProofNo) {
		this.idProofNo = idProofNo;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
}
