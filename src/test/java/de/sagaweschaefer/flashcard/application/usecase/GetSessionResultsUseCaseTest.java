package de.sagaweschaefer.flashcard.application.usecase;

import de.sagaweschaefer.flashcard.model.SessionResult;
import de.sagaweschaefer.flashcard.domain.repository.SessionResultRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetSessionResultsUseCaseTest {

    private static class FakeSessionResultRepository implements SessionResultRepository {
        private List<SessionResult> data;

        FakeSessionResultRepository(List<SessionResult> initial) {
            this.data = new ArrayList<>(initial);
        }

        @Override
        public List<SessionResult> findAll() {
            return new ArrayList<>(data);
        }

        @Override
        public void saveAll(List<SessionResult> results) {
            this.data = new ArrayList<>(results);
        }
    }

    @Test
    void execute_returnsNewestFirst() {
        List<SessionResult> data = List.of(
                new SessionResult("S1", 3, 5, 1000),
                new SessionResult("S2", 4, 5, 2000),
                new SessionResult("S3", 5, 5, 3000)
        );
        GetSessionResultsUseCase uc = new GetSessionResultsUseCase(new FakeSessionResultRepository(data));
        List<SessionResult> result = uc.execute(2);
        assertEquals(2, result.size());
        assertEquals("S3", result.get(0).getSessionName());
        assertEquals("S2", result.get(1).getSessionName());
    }

    @Test
    void execute_emptyRepo_returnsEmptyList() {
        GetSessionResultsUseCase uc = new GetSessionResultsUseCase(new FakeSessionResultRepository(List.of()));
        assertTrue(uc.execute(5).isEmpty());
    }

    @Test
    void execute_zeroLimit_returnsAll() {
        List<SessionResult> data = List.of(
                new SessionResult("S1", 1, 1, 100),
                new SessionResult("S2", 1, 1, 100)
        );
        GetSessionResultsUseCase uc = new GetSessionResultsUseCase(new FakeSessionResultRepository(data));
        assertEquals(2, uc.execute(0).size());
    }
}

