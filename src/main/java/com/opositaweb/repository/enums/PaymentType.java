package com.opositaweb.repository.enums;

public enum PaymentType {

	MONTHLY(9.95), HALF_YEAR(49.95), ONE_YEAR(79.95);

	private final double price;

	PaymentType(double price) {
		this.price = price;
	}

	public double getPrice() {
		return price;
	}

}