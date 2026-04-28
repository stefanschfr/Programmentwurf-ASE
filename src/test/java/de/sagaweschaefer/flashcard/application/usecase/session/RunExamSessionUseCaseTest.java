package de.sagaweschaefer.flashcard.application.usecase.session;

import de.sagaweschaefer.flashcard.application.session.PreparedSession;
import de.sagaweschaefer.flashcard.application.session.SessionMode;
import de.sagaweschaefer.flashcard.application.session.SessionSummary;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.support.InMemoryExamSessionResultRepository;
import de.sagaweschaefer.flashcard.support.InMemoryFlashcardStatisticsRepository;
import de.sagaweschaefer.flashcard.support.RecordingSessionInteraction;
import de.sagaweschaefer.flashcard.support.TestDataFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RunExamSessionUseCaseTest {
    @Test
    void savesStatisticsAndExamResultForCorrectAnswer() {
        Flashcard card = TestDataFactory.freeTextCard("Frage", "Antwort");
        InMemoryFlashcardStatisticsRepository statisticsRepository = new InMemoryFlashcardStatisticsRepository();
        InMemoryExamSessionResultRepository resultRepository = new InMemoryExamSessionResultRepository();
        RecordingSessionInteraction interaction = new RecordingSessionInteraction();
        interaction.addAnswer(true);
        RunExamSessionUseCase useCase = new RunExamSessionUseCase(statisticsRepository, resultRepository, interaction);
        PreparedSession preparedSession = new PreparedSession(SessionMode.EXAM, "Prüfung", List.of(card), 60_000);

        SessionSummary summary = useCase.execute(preparedSession);
        FlashcardStatistics statistics = statisticsRepository.findAll().get(card.getId());

        assertEquals(1, summary.getCorrectCount());
        assertEquals(1, statistics.getCorrectCount());
        assertEquals(1, resultRepository.findAll().size());
        assertTrue(interaction.getEvents().contains("exam-progress-1"));
    }

    @Test
    void marksExamAsTimedOutWhenNoTimeIsLeft() {
        Flashcard card = TestDataFactory.freeTextCard("Frage", "Antwort");
        InMemoryFlashcardStatisticsRepository statisticsRepository = new InMemoryFlashcardStatisticsRepository();
        InMemoryExamSessionResultRepository resultRepository = new InMemoryExamSessionResultRepository();
        RecordingSessionInteraction interaction = new RecordingSessionInteraction();
        RunExamSessionUseCase useCase = new RunExamSessionUseCase(statisticsRepository, resultRepository, interaction);
        PreparedSession preparedSession = new PreparedSession(SessionMode.EXAM, "Prüfung", List.of(card), 0);

        SessionSummary summary = useCase.execute(preparedSession);

        assertTrue(summary.isTimedOut());
        assertEquals(0, summary.getCorrectCount());
        assertEquals(0, interaction.getAskedQuestions());
        assertTrue(interaction.getEvents().contains("time-expired"));
        assertEquals(1, resultRepository.findAll().size());
    }
}

