package com.opositaweb.repository.entities;

import com.opositaweb.repository.enums.AnswerPoints;
import com.opositaweb.repository.enums.Option;
import lombok.Getter;

@Getter
public class ScoreCalculator {

	private static double totalScore;

	private static double checkAnswer(Option selectedOption, Question question) {
		return selectedOption.name().equals(question.getAnswer()) ? AnswerPoints.CORRECT.getPoints()
				: AnswerPoints.WRONG.getPoints();
	}

	private static void calculateScore(Option selectedOption, Question question) {
		if (selectedOption != null) {
			totalScore += checkAnswer(selectedOption, question);
		}
		else {
			totalScore += AnswerPoints.UNANSWERED.getPoints();
		}
	}

}
