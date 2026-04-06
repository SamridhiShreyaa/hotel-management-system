package com.pesu.hotel.payment.dto;

public class PaymentResponse {
	private boolean success;
	private String paymentStatus;
	private String transactionReference;
	private String message;

	public PaymentResponse() {
	}

	public PaymentResponse(boolean success, String paymentStatus, String transactionReference, String message) {
		this.success = success;
		this.paymentStatus = paymentStatus;
		this.transactionReference = transactionReference;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getTransactionReference() {
		return transactionReference;
	}

	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
