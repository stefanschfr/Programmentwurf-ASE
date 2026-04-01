package de.sagaweschaefer.flashcard.menu.flashcardsession;

import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.QuestionType;
import de.sagaweschaefer.flashcard.util.MenuUtils;
import java.util.List;

public class FlashcardQuestionHelper {

    public static boolean askQuestion(Flashcard card) {
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
            String normalizedAnswer = (answer.toLowerCase().startsWith("w")) ? "Wahr" : "Falsch";
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

    public static String getCorrectAnswerDisplay(Flashcard card) {
        if (card.getQuestionType() == QuestionType.NUMERIC) {
            return String.valueOf(card.getAnswerNum());
        }
        return card.getAnswerText();
    }
}
