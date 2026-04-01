package de.sagaweschaefer.flashcard.menu.flashcardsession;

public class FlashcardSessionStatistics {

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
        if (percentage >= 95) return 1.0;
        if (percentage <= 0) return 6.0;

        double grade;

        if (percentage >= 50) {
            grade = 4.0 - (percentage - 50) * (3.0 / 45.0); // 50->4.0, 95->1.0
        } else if (percentage >= 30) {
            grade = 5.0 - (percentage - 30) * (1.0 / 20.0); // 30->5.0, 50->4.0
        } else {
            grade = 6.0 - (percentage - 0) * (1.0 / 30.0); // 0->6.0, 30->5.0
        }

        return Math.round(grade * 10.0) / 10.0;
    }

    public static String formatTime(long millis) {
        return (millis / 60000) + "m " + ((millis % 60000) / 1000) + "s";
    }
}
