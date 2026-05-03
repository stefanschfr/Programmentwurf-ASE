package de.sagaweschaefer.flashcard.domain.strategy;

import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DueCardsStrategy implements SessionStrategy {

    @Override
    public List<Flashcard> selectCards(List<FlashcardSet> sets, Map<String, FlashcardStatistics> statisticsMap) {
        List<Flashcard> dueCards = new ArrayList<>();
        for (FlashcardSet set : sets) {
            for (Flashcard card : set.getFlashcards()) {
                FlashcardStatistics stats = statisticsMap.get(card.getId());
                if (stats == null || stats.isDue()) {
                    dueCards.add(card);
                }
            }
        }
        return dueCards;
    }

    @Override
    public String getSessionName() {
        return "Fällige Lernkarten";
    }
}

