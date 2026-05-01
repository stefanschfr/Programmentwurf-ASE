package de.sagaweschaefer.flashcard.domain.strategy;

import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SessionStrategyTest {

    @Test
    void dueCardsStrategy_selectsOnlyDueCards() {
        FlashcardSet set = new FlashcardSet("Test");
        Flashcard card1 = new Flashcard("Q1?", "A1");
        Flashcard card2 = new Flashcard("Q2?", "A2");
        set.getFlashcards().add(card1);
        set.getFlashcards().add(card2);

        Map<String, FlashcardStatistics> statsMap = new HashMap<>();
        // card1 has stats with high level and recent correct -> not due
        FlashcardStatistics stats1 = new FlashcardStatistics(card1.getId());
        stats1.setLevel(6);
        stats1.incrementCorrect(); // sets lastCorrectAt to now
        statsMap.put(card1.getId(), stats1);
        // card2 has no stats -> due

        DueCardsStrategy strategy = new DueCardsStrategy();
        assertEquals("Fällige Lernkarten", strategy.getSessionName());
        List<Flashcard> selected = strategy.selectCards(List.of(set), statsMap);

        assertEquals(1, selected.size());
        assertEquals(card2.getId(), selected.getFirst().getId());
    }

    @Test
    void wrongAnswersStrategy_selectsOnlyLevelZero() {
        FlashcardSet set = new FlashcardSet("Test");
        Flashcard card1 = new Flashcard("Q1?", "A1");
        Flashcard card2 = new Flashcard("Q2?", "A2");
        set.getFlashcards().add(card1);
        set.getFlashcards().add(card2);

        Map<String, FlashcardStatistics> statsMap = new HashMap<>();
        FlashcardStatistics stats1 = new FlashcardStatistics(card1.getId());
        stats1.setLevel(0);
        statsMap.put(card1.getId(), stats1);
        FlashcardStatistics stats2 = new FlashcardStatistics(card2.getId());
        stats2.setLevel(3);
        statsMap.put(card2.getId(), stats2);

        WrongAnswersStrategy strategy = new WrongAnswersStrategy();
        List<Flashcard> selected = strategy.selectCards(List.of(set), statsMap);

        assertEquals(1, selected.size());
        assertEquals(card1.getId(), selected.getFirst().getId());
    }

    @Test
    void randomQuizStrategy_selectsAllCards() {
        FlashcardSet set = new FlashcardSet("Test");
        set.getFlashcards().add(new Flashcard("Q1?", "A1"));
        set.getFlashcards().add(new Flashcard("Q2?", "A2"));

        RandomQuizStrategy strategy = new RandomQuizStrategy();
        List<Flashcard> selected = strategy.selectCards(List.of(set), new HashMap<>());

        assertEquals(2, selected.size());
    }
}

