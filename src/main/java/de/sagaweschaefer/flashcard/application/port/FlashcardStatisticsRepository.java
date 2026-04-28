package de.sagaweschaefer.flashcard.application.port;

import de.sagaweschaefer.flashcard.model.FlashcardStatistics;

import java.util.Map;

public interface FlashcardStatisticsRepository {
    Map<String, FlashcardStatistics> findAll();

    void saveAll(Map<String, FlashcardStatistics> statistics);
}

