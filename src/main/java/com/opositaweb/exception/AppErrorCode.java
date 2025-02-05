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
	ERROR_DELETE("Error deleting"),
	ERROR_EMAIL("The email can not be sent successfully"),
	ERROR_SAVE_PDF("Error saving the PDF"),
	ERROR_SAVE_PDF_NAME("Error saving the PDF name"),
	ERROR_DELETE_PDF("Error deleting the PDF"),
	ERROR_INVALID_VERIFICATION_TOKEN("Invalid verification token"),
	;


	private final String message;

	AppErrorCode(String message) {
		this.message = message;
	}

}
