package de.sagaweschaefer.flashcard.testdoubles;

import de.sagaweschaefer.flashcard.domain.repository.StatisticsRepository;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;

import java.util.HashMap;
import java.util.Map;

public class MockStatisticsRepository implements StatisticsRepository {
    private Map<String, FlashcardStatistics> data = new HashMap<>();
    private int saveCallCount = 0;
    private int findAllCallCount = 0;

    @Override
    public Map<String, FlashcardStatistics> findAll() {
        findAllCallCount++;
        return new HashMap<>(data);
    }

    @Override
    public void saveAll(Map<String, FlashcardStatistics> statistics) {
        saveCallCount++;
        this.data = new HashMap<>(statistics);
    }

    public int getSaveCallCount() {
        return saveCallCount;
    }

    public int getFindAllCallCount() {
        return findAllCallCount;
    }

    public void verifyFindAllCalled(int expectedTimes) {
        if (findAllCallCount != expectedTimes) {
            throw new AssertionError("findAll() wurde " + findAllCallCount + " mal aufgerufen, erwartet: " + expectedTimes);
        }
    }

    public void verifySaveCalled(int expectedTimes) {
        if (saveCallCount != expectedTimes) {
            throw new AssertionError("saveAll() wurde " + saveCallCount + " mal aufgerufen, erwartet: " + expectedTimes);
        }
    }

    public Map<String, FlashcardStatistics> getData() {
        return data;
    }

    public void seedStatistic(String flashcardId, FlashcardStatistics stats) {
        data.put(flashcardId, stats);
    }
}

