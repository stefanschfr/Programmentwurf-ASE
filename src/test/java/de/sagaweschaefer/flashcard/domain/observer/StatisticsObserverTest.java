package de.sagaweschaefer.flashcard.domain.observer;

import de.sagaweschaefer.flashcard.domain.repository.SessionResultRepository;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.SessionResult;
import de.sagaweschaefer.flashcard.testdoubles.MockStatisticsRepository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsObserverTest {

    @Test
    void onQuestionAnswered_correct_updatesStatistics() {
        MockStatisticsRepository statsRepo = new MockStatisticsRepository();
        FakeSessionResultRepository sessionRepo = new FakeSessionResultRepository();
        StatisticsObserver observer = new StatisticsObserver(statsRepo, sessionRepo);
        observer.loadStatistics();

        statsRepo.verifyFindAllCalled(1);

        Flashcard card = new Flashcard("Q?", "A");
        observer.onQuestionAnswered(card, true, 2, false);

        assertEquals(1, observer.getStatisticsMap().get(card.getId()).getCorrectCount());
    }

    @Test
    void onQuestionAnswered_wrong_updatesStatistics() {
        MockStatisticsRepository statsRepo = new MockStatisticsRepository();
        FakeSessionResultRepository sessionRepo = new FakeSessionResultRepository();
        StatisticsObserver observer = new StatisticsObserver(statsRepo, sessionRepo);
        observer.loadStatistics();

        Flashcard card = new Flashcard("Q?", "A");
        observer.onQuestionAnswered(card, false, 0, false);

        assertEquals(1, observer.getStatisticsMap().get(card.getId()).getWrongCount());
    }

    @Test
    void onSessionFinished_savesStatisticsAndResult() {
        MockStatisticsRepository statsRepo = new MockStatisticsRepository();
        FakeSessionResultRepository sessionRepo = new FakeSessionResultRepository();
        StatisticsObserver observer = new StatisticsObserver(statsRepo, sessionRepo);
        observer.loadStatistics();

        observer.onSessionFinished("Test", 5, 10, 60000);

        assertEquals(1, statsRepo.getSaveCallCount());
        assertEquals(1, statsRepo.getFindAllCallCount());
        statsRepo.verifySaveCalled(1);
        assertNotNull(statsRepo.getData());
        assertEquals(1, sessionRepo.getData().size());
    }

    // Inner fake for session results
    static class FakeSessionResultRepository implements SessionResultRepository {
        private List<SessionResult> data = new ArrayList<>();

        @Override
        public List<SessionResult> findAll() {
            return new ArrayList<>(data);
        }

        @Override
        public void saveAll(List<SessionResult> results) {
            this.data = new ArrayList<>(results);
        }

        public List<SessionResult> getData() {
            return data;
        }
    }
}

