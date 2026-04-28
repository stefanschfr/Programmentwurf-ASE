package de.sagaweschaefer.flashcard.support;
import de.sagaweschaefer.flashcard.application.port.ExamSessionResultRepository;
import de.sagaweschaefer.flashcard.model.SessionResult;
import java.util.ArrayList;
import java.util.List;
public class InMemoryExamSessionResultRepository implements ExamSessionResultRepository {
    private List<SessionResult> results = new ArrayList<>();
    @Override
    public List<SessionResult> findAll() {
        return results;
    }
    @Override
    public void saveAll(List<SessionResult> results) {
        this.results = results;
    }
}
