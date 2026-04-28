package de.sagaweschaefer.flashcard.infrastructure.persistence;

import de.sagaweschaefer.flashcard.application.port.FlashcardStatisticsRepository;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.util.JsonStorage;

import java.util.Map;

public class JsonFlashcardStatisticsRepository implements FlashcardStatisticsRepository {
    private final JsonStorage storage;

    public JsonFlashcardStatisticsRepository(JsonStorage storage) {
        this.storage = storage;
    }

    @Override
    public Map<String, FlashcardStatistics> findAll() {
        return storage.loadStatistics();
    }

    @Override
    public void saveAll(Map<String, FlashcardStatistics> statistics) {
        storage.saveStatistics(statistics);
    }
}

