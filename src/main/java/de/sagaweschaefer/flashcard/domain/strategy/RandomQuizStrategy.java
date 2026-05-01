package de.sagaweschaefer.flashcard.domain.strategy;

import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RandomQuizStrategy implements SessionStrategy {

    @Override
    public List<Flashcard> selectCards(List<FlashcardSet> sets, Map<String, FlashcardStatistics> statisticsMap) {
        List<Flashcard> allCards = new ArrayList<>();
        for (FlashcardSet set : sets) {
            allCards.addAll(set.getFlashcards());
        }
        Collections.shuffle(allCards);
        return allCards;
    }

    @Override
    public String getSessionName() {
        return "Zufälliges Quiz";
    }
}

