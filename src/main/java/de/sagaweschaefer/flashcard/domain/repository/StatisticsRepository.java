package de.sagaweschaefer.flashcard.domain.repository;

import de.sagaweschaefer.flashcard.model.FlashcardStatistics;

import java.util.Map;

public interface StatisticsRepository {
    Map<String, FlashcardStatistics> findAll();
    void saveAll(Map<String, FlashcardStatistics> statistics);
}

