package de.sagaweschaefer.flashcard.support;

import de.sagaweschaefer.flashcard.application.port.FlashcardStatisticsRepository;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;

import java.util.HashMap;
import java.util.Map;

public class InMemoryFlashcardStatisticsRepository implements FlashcardStatisticsRepository {
    private Map<String, FlashcardStatistics> statistics = new HashMap<>();

    public InMemoryFlashcardStatisticsRepository() {
    }

    public InMemoryFlashcardStatisticsRepository(Map<String, FlashcardStatistics> statistics) {
        this.statistics = new HashMap<>(statistics);
    }

    @Override
    public Map<String, FlashcardStatistics> findAll() {
        return statistics;
    }

    @Override
    public void saveAll(Map<String, FlashcardStatistics> statistics) {
        this.statistics = statistics;
    }
}

