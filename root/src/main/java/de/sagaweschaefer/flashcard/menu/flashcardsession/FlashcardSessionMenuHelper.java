package src.main.java.de.sagaweschaefer.flashcard.menu.flashcardsession;

import src.main.java.de.sagaweschaefer.flashcard.model.Flashcard;
import src.main.java.de.sagaweschaefer.flashcard.model.FlashcardSet;
import src.main.java.de.sagaweschaefer.flashcard.model.QuestionType;
import src.main.java.de.sagaweschaefer.flashcard.util.BinaryStorage;
import src.main.java.de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class FlashcardSessionMenuHelper {
    private final List<FlashcardSet> flashcardSets;
    private final BinaryStorage storage = new BinaryStorage();

    public FlashcardSessionMenuHelper() {
        this.flashcardSets = storage.loadFlashcardSets();
    }

    public void startSession() {
        if (flashcardSets.isEmpty()) {
            System.out.println("Es sind keine Lernkartensets verfügbar.");
            return;
        }

        MenuUtils.displayFlashcardSets(flashcardSets, "Verfügbare Lernkartensets");
        int choice = MenuUtils.promptForInt("Wähle ein Lernkartenset (Nummer): ") - 1;

        if (choice >= 0 && choice < flashcardSets.size()) {
            FlashcardSet set = flashcardSets.get(choice);
            runSession(set.getFlashcardSet(), set.getName(), false);
        } else {
            System.out.println("Ungültige Auswahl.");
        }
    }

    public void startWrongAnswersSession() {
        List<Flashcard> wrongAnswers = storage.loadWrongAnswers();
        if (wrongAnswers.isEmpty()) {
            System.out.println("Es sind keine falsch beantworteten Fragen gespeichert.");
            return;
        }

        runSession(wrongAnswers, "Falsch beantwortete Fragen", true);
    }

    public void startExamMode() {
        if (flashcardSets.isEmpty()) {
            System.out.println("Es sind keine Lernkartensets verfügbar.");
            return;
        }

        MenuUtils.displayFlashcardSets(flashcardSets, "Verfügbare Lernkartensets");
        int choice = MenuUtils.promptForInt("Wähle ein Lernkartenset für die Prüfung (Nummer): ") - 1;

        if (choice >= 0 && choice < flashcardSets.size()) {
            FlashcardSet set = flashcardSets.get(choice);
            List<Flashcard> allCards = new ArrayList<>(set.getFlashcardSet());
            
            if (allCards.size() < 10) {
                System.out.println("Das gewählte Set enthält nur " + allCards.size() + " Karten. Für den Prüfungsmodus sind mindestens 10 Karten erforderlich.");
                return;
            }

            Collections.shuffle(allCards);
            List<Flashcard> examCards = allCards.subList(0, 10);
            
            runExamSession(examCards, set.getName());
        } else {
            System.out.println("Ungültige Auswahl.");
        }
    }

    private void runSession(List<Flashcard> cardsToUse, String sessionName, boolean isWrongAnswersMode) {
        List<Flashcard> cards = new ArrayList<>(cardsToUse);
        if (cards.isEmpty()) {
            System.out.println("Keine Karten zum Lernen verfügbar.");
            return;
        }

        Collections.shuffle(cards);
        int correctCount = 0;
        List<Flashcard> currentWrongAnswers = storage.loadWrongAnswers();
        boolean changed = false;

        System.out.println("\n--- Session gestartet: " + sessionName + " ---");
        for (Flashcard card : cards) {
            if (askQuestion(card)) {
                System.out.println("Richtig!");
                correctCount++;
                if (isWrongAnswersMode) {
                    if (currentWrongAnswers.remove(card)) {
                        changed = true;
                    }
                }
            } else {
                System.out.println("Falsch! Die richtige Antwort war: " + getCorrectAnswerDisplay(card));
                if (!isWrongAnswersMode) {
                    changed |= updateWrongAnswers(currentWrongAnswers, card);
                }
            }
        }

        saveWrongAnswersIfChanged(currentWrongAnswers, changed);
        displaySessionResult(correctCount, cards.size(), 0);
    }

    private void runExamSession(List<Flashcard> examCards, String setName) {
        long startTime = System.currentTimeMillis();
        long limitMillis = 10 * 60 * 1000; // 10 Minuten
        int correctCount = 0;
        int totalQuestions = examCards.size();
        List<Flashcard> currentWrongAnswers = storage.loadWrongAnswers();
        boolean changed = false;

        System.out.println("\n--- Prüfung gestartet: " + setName + " ---");
        System.out.println("Anzahl Fragen: " + totalQuestions);
        System.out.println("Zeitlimit: 10 Minuten");

        for (int i = 0; i < examCards.size(); i++) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;
            
            if (elapsedTime >= limitMillis) {
                System.out.println("\n!!! ZEIT ABGELAUFEN !!!");
                break;
            }

            Flashcard card = examCards.get(i);
            System.out.println("\nFrage " + (i + 1) + " von " + totalQuestions);
            System.out.println("Verbleibende Zeit: " + formatTime(limitMillis - elapsedTime));

            if (askQuestion(card)) {
                System.out.println("Richtig!");
                correctCount++;
            } else {
                System.out.println("Falsch! Die richtige Antwort war: " + getCorrectAnswerDisplay(card));
                changed |= updateWrongAnswers(currentWrongAnswers, card);
            }
            
            // Prüfung nach der Beantwortung, falls die Beantwortung lange gedauert hat
            if (System.currentTimeMillis() - startTime >= limitMillis) {
                System.out.println("\n!!! ZEIT WÄHREND DER LETZTEN FRAGE ABGELAUFEN !!!");
                if (i < totalQuestions - 1) {
                    break;
                }
            }
        }

        saveWrongAnswersIfChanged(currentWrongAnswers, changed);
        System.out.println("\n--- Prüfung beendet ---");
        displaySessionResult(correctCount, totalQuestions, System.currentTimeMillis() - startTime);
    }

    private boolean updateWrongAnswers(List<Flashcard> currentWrongAnswers, Flashcard card) {
        if (!currentWrongAnswers.contains(card)) {
            currentWrongAnswers.add(card);
            return true;
        }
        return false;
    }

    private void saveWrongAnswersIfChanged(List<Flashcard> currentWrongAnswers, boolean changed) {
        if (changed) {
            storage.saveWrongAnswers(currentWrongAnswers);
        }
    }

    private void displaySessionResult(int correctCount, int totalCount, long durationMillis) {
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

    private double calculateGrade(double percentage) {
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

    private String formatTime(long millis) {
        return (millis / 60000) + "m " + ((millis % 60000) / 1000) + "s";
    }

    private boolean askQuestion(Flashcard card) {
        System.out.println("\nFrage: " + card.getQuestion());
        
        if (card.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
            List<String> options = card.getOptions();
            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + ". " + options.get(i));
            }
            String answer = MenuUtils.promptForString("Deine Antwort (Text): ");
            return answer.equalsIgnoreCase(card.getAnswerText());
        } else if (card.getQuestionType() == QuestionType.TRUE_FALSE) {
            String answer = MenuUtils.promptForString("Wahr (w) oder Falsch (f)? ");
            String normalizedAnswer = answer.toLowerCase().startsWith("w") ? "Wahr" : "Falsch";
            return normalizedAnswer.equalsIgnoreCase(card.getAnswerText());
        } else if (card.getQuestionType() == QuestionType.NUMERIC) {
            String input = MenuUtils.promptForString("Deine Antwort (Zahl): ");
            try {
                double val = Double.parseDouble(input);
                return Math.abs(val - card.getAnswerNum()) < 0.001;
            } catch (NumberFormatException e) {
                return false;
            }
        } else { // FREE_TEXT
            String answer = MenuUtils.promptForString("Deine Antwort: ");
            return answer.trim().equalsIgnoreCase(card.getAnswerText().trim());
        }
    }

    private String getCorrectAnswerDisplay(Flashcard card) {
        if (card.getQuestionType() == QuestionType.NUMERIC) {
            return String.valueOf(card.getAnswerNum());
        }
        return card.getAnswerText();
    }
}
