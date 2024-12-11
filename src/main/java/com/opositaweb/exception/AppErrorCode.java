package com.opositaweb.exception;

import lombok.Getter;

@Getter
public enum AppErrorCode {

	ERROR_PAYMENT_NOT_FOUND("Payment not found"),
	ERROR_PAYMENTPLAN_NOT_FOUND("PaymentPlan not found"),
	ERROR_CUSTOMER_NOT_FOUND("Customer not found"),

	ERROR_QUESTION_NOT_FOUND("Question not found"),
	ERROR_TEST_NOT_FOUND("Test not found"),
	ERROR_PDF_NOT_FOUND("PDF not found"),
	ERROR_SAVE("Error saving"),
	ERROR_UPDATE("Error updating"),
	ERROR_DELETE("Error deleting"),;

	private final String message;

	AppErrorCode(String message) {
		this.message = message;
	}

}
