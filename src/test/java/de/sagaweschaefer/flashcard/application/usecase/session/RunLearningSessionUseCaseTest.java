package de.sagaweschaefer.flashcard.application.usecase.session;

import de.sagaweschaefer.flashcard.application.session.PreparedSession;
import de.sagaweschaefer.flashcard.application.session.SessionMode;
import de.sagaweschaefer.flashcard.application.session.SessionSummary;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.support.InMemoryFlashcardStatisticsRepository;
import de.sagaweschaefer.flashcard.support.InMemoryLearningSessionResultRepository;
import de.sagaweschaefer.flashcard.support.RecordingSessionInteraction;
import de.sagaweschaefer.flashcard.support.TestDataFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RunLearningSessionUseCaseTest {
    @Test
    void savesStatisticsAndSessionResultForCorrectAnswer() {
        Flashcard card = TestDataFactory.freeTextCard("Frage", "Antwort");
        InMemoryFlashcardStatisticsRepository statisticsRepository = new InMemoryFlashcardStatisticsRepository();
        InMemoryLearningSessionResultRepository resultRepository = new InMemoryLearningSessionResultRepository();
        RecordingSessionInteraction interaction = new RecordingSessionInteraction();
        interaction.addAnswer(true);
        interaction.addRating(3);
        RunLearningSessionUseCase useCase = new RunLearningSessionUseCase(statisticsRepository, resultRepository, interaction);
        PreparedSession preparedSession = new PreparedSession(SessionMode.SET_LEARNING, "Lernen", List.of(card), 0);

        SessionSummary summary = useCase.execute(preparedSession);
        FlashcardStatistics statistics = statisticsRepository.findAll().get(card.getId());

        assertEquals(1, summary.getCorrectCount());
        assertFalse(summary.isTimedOut());
        assertEquals(1, statistics.getCorrectCount());
        assertEquals(1, statistics.getLevel());
        assertEquals(1, resultRepository.findAll().size());
        assertEquals(1, interaction.getRatingRequests());
    }

    @Test
    void storesWrongAnswerWithoutRequestingRating() {
        Flashcard card = TestDataFactory.freeTextCard("Frage", "Antwort");
        FlashcardStatistics existingStatistics = new FlashcardStatistics(card.getId());
        existingStatistics.setLevel(3);
        InMemoryFlashcardStatisticsRepository statisticsRepository = new InMemoryFlashcardStatisticsRepository(Map.of(card.getId(), existingStatistics));
        InMemoryLearningSessionResultRepository resultRepository = new InMemoryLearningSessionResultRepository();
        RecordingSessionInteraction interaction = new RecordingSessionInteraction();
        interaction.addAnswer(false);
        RunLearningSessionUseCase useCase = new RunLearningSessionUseCase(statisticsRepository, resultRepository, interaction);
        PreparedSession preparedSession = new PreparedSession(SessionMode.DUE_CARDS, "Due", List.of(card), 0);

        SessionSummary summary = useCase.execute(preparedSession);
        FlashcardStatistics statistics = statisticsRepository.findAll().get(card.getId());

        assertEquals(0, summary.getCorrectCount());
        assertEquals(1, statistics.getWrongCount());
        assertEquals(0, statistics.getLevel());
        assertEquals(0, interaction.getRatingRequests());
        assertEquals(1, resultRepository.findAll().size());
    }
}

