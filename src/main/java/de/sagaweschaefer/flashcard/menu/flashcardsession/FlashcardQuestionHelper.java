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
        }

        String prompt = "Deine Antwort";
        switch (card.getQuestionType()) {
            case TRUE_FALSE: prompt += " (Wahr (w) / Falsch (f))"; break;
            case NUMERIC: prompt += " (Zahl)"; break;
            default: prompt += " (Text)"; break;
        }
        
        String answer = MenuUtils.promptForString(prompt + ": ");
        return card.checkAnswer(answer);
    }

}
