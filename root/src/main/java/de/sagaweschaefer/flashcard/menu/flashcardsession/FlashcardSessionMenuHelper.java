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

        listSets();
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

    private void listSets() {
        System.out.println("\n--- Verfügbare Lernkartensets ---");
        for (int i = 0; i < flashcardSets.size(); i++) {
            System.out.println((i + 1) + ". " + flashcardSets.get(i).getName());
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
                    if (!currentWrongAnswers.contains(card)) {
                        currentWrongAnswers.add(card);
                        changed = true;
                    }
                }
            }
        }

        if (changed) {
            storage.saveWrongAnswers(currentWrongAnswers);
        }

        System.out.println("\n--- Session beendet ---");
        System.out.println("Ergebnis: " + correctCount + " von " + cards.size() + " richtig beantwortet.");
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
