package de.sagaweschaefer.flashcard.menu.flashcardsession;

import de.sagaweschaefer.flashcard.domain.service.GradeCalculator;

public class FlashcardSessionStatistics {

    private static final GradeCalculator GRADE_CALCULATOR = new GradeCalculator();

    public static void displaySessionResult(int correctCount, int totalCount, long durationMillis) {
        if (durationMillis == 0) {
            System.out.println("\n--- Session beendet ---");
        }
        System.out.println("Ergebnis: " + correctCount + " von " + totalCount + " richtig beantwortet.");

        if (totalCount > 0) {
            double percentage = (double) correctCount / totalCount * 100;
            System.out.printf("Prozentual richtig: %.2f%%\n", percentage);
            System.out.println("Erreichte Note: " + calculateGrade(percentage));
        }

        if (durationMillis > 0) {
            System.out.println("Benötigte Zeit: " + formatTime(durationMillis));
        }
    }

    public static double calculateGrade(double percentage) {
        return GRADE_CALCULATOR.calculateGrade(percentage);
    }

    public static String formatTime(long millis) {
        return (millis / 60000) + "m " + ((millis % 60000) / 1000) + "s";
    }
}
