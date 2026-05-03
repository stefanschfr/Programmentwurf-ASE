package de.sagaweschaefer.flashcard.domain.service;

@SuppressWarnings("unused")
public class GradeCalculator {

    public static final double BEST_GRADE = 1.0;

    public static final double WORST_GRADE = 6.0;

    private static final double THRESHOLD_BEST = 95.0;

    private static final double THRESHOLD_PASS = 50.0;

    private static final double THRESHOLD_FAIL_LOWER = 30.0;

    public double calculateGrade(double percentage) {
        if (percentage >= THRESHOLD_BEST) {
            return BEST_GRADE;
        }
        if (percentage <= 0.0) {
            return WORST_GRADE;
        }

        double grade;
        if (percentage >= THRESHOLD_PASS) {
            // 50 % -> 4.0, 95 % -> 1.0
            grade = 4.0 - (percentage - THRESHOLD_PASS) * (3.0 / (THRESHOLD_BEST - THRESHOLD_PASS));
        } else if (percentage >= THRESHOLD_FAIL_LOWER) {
            // 30 % -> 5.0, 50 % -> 4.0
            grade = 5.0 - (percentage - THRESHOLD_FAIL_LOWER) * (1.0 / (THRESHOLD_PASS - THRESHOLD_FAIL_LOWER));
        } else {
            // 0 % -> 6.0, 30 % -> 5.0
            grade = WORST_GRADE - percentage * (1.0 / THRESHOLD_FAIL_LOWER);
        }

        return roundToOneDecimal(grade);
    }

    public double calculateGrade(int correctCount, int totalCount) {
        if (totalCount <= 0) {
            return WORST_GRADE;
        }
        double percentage = (double) correctCount / totalCount * 100.0;
        return calculateGrade(percentage);
    }

    public boolean isPassing(double percentage) {
        return calculateGrade(percentage) <= 4.0;
    }

    private double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}

