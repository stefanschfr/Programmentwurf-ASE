package de.sagaweschaefer.flashcard.support;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public final class TestDataFactory {
    private TestDataFactory() {
    }
    public static FlashcardSet flashcardSet(String name, Flashcard... flashcards) {
        FlashcardSet flashcardSet = new FlashcardSet(name);
        flashcardSet.setFlashcards(new ArrayList<>(List.of(flashcards)));
        return flashcardSet;
    }
    public static Flashcard freeTextCard(String question, String answer) {
        return new Flashcard(question, answer);
    }
    public static FlashcardStatistics statistics(String flashcardId, int level, int correctCount, int wrongCount, LocalDateTime lastCorrectAt) {
        FlashcardStatistics statistics = new FlashcardStatistics(flashcardId);
        statistics.setLevel(level);
        statistics.setCorrectCount(correctCount);
        statistics.setWrongCount(wrongCount);
        statistics.setLastCorrectAt(lastCorrectAt);
        return statistics;
    }
}
