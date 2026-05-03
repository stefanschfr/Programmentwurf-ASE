package de.sagaweschaefer.flashcard.domain.strategy;

import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;

import java.util.List;
import java.util.Map;

public interface SessionStrategy {
    List<Flashcard> selectCards(List<FlashcardSet> sets, Map<String, FlashcardStatistics> statisticsMap);
    String getSessionName();
}

