package com.opositaweb.repository.enums;

import lombok.Getter;

@Getter
public enum AnswerPoints {

	CORRECT(1.0), WRONG(-0.33), UNANSWERED(0.0);

	private final double points;

	AnswerPoints(double points) {
		this.points = points;
	}

	public double getPoints() {
		return points;
	}

}
