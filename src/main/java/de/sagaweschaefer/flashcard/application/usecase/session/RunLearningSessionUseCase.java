package de.sagaweschaefer.flashcard.application.usecase.session;

import de.sagaweschaefer.flashcard.application.port.FlashcardStatisticsRepository;
import de.sagaweschaefer.flashcard.application.port.LearningSessionResultRepository;
import de.sagaweschaefer.flashcard.application.port.SessionInteraction;
import de.sagaweschaefer.flashcard.application.session.PreparedSession;
import de.sagaweschaefer.flashcard.application.session.SessionSummary;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardId;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.model.SessionResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RunLearningSessionUseCase {
    private final FlashcardStatisticsRepository flashcardStatisticsRepository;
    private final LearningSessionResultRepository learningSessionResultRepository;
    private final SessionInteraction sessionInteraction;

    public RunLearningSessionUseCase(FlashcardStatisticsRepository flashcardStatisticsRepository,
                                     LearningSessionResultRepository learningSessionResultRepository,
                                     SessionInteraction sessionInteraction) {
        this.flashcardStatisticsRepository = flashcardStatisticsRepository;
        this.learningSessionResultRepository = learningSessionResultRepository;
        this.sessionInteraction = sessionInteraction;
    }

    public SessionSummary execute(PreparedSession preparedSession) {
        if (preparedSession.getFlashcards().isEmpty()) {
            throw new IllegalArgumentException("Keine Karten zum Lernen verfügbar.");
        }

        List<Flashcard> cards = new ArrayList<>(preparedSession.getFlashcards());
        Collections.shuffle(cards);
        Map<String, FlashcardStatistics> statisticsMap = flashcardStatisticsRepository.findAll();
        long startTime = System.currentTimeMillis();
        int correctCount = 0;

        sessionInteraction.showSessionStarted(preparedSession);

        for (Flashcard card : cards) {
            if (processCard(card, statisticsMap)) {
                correctCount++;
            }
        }

        flashcardStatisticsRepository.saveAll(statisticsMap);
        long durationMillis = System.currentTimeMillis() - startTime;
        saveResult(preparedSession.getSessionName(), correctCount, cards.size(), durationMillis);

        SessionSummary summary = new SessionSummary(
                preparedSession.getSessionName(),
                preparedSession.getMode(),
                correctCount,
                cards.size(),
                durationMillis,
                false
        );
        sessionInteraction.showSessionFinished(summary);
        return summary;
    }

    private boolean processCard(Flashcard card, Map<String, FlashcardStatistics> statisticsMap) {
        FlashcardStatistics statistics = statisticsMap.computeIfAbsent(card.getId(), id -> new FlashcardStatistics(FlashcardId.of(id)));
        boolean wasDue = statistics.isDue();

        if (sessionInteraction.askQuestion(card)) {
            sessionInteraction.showCorrectAnswerFeedback();
            statistics.incrementCorrect();
            int rating = sessionInteraction.requestSelfAssessment(wasDue);
            statistics.applyRating(rating, wasDue);
            return true;
        }

        sessionInteraction.showWrongAnswerFeedback(card);
        statistics.incrementWrong();
        return false;
    }

    private void saveResult(String sessionName, int correctCount, int totalCount, long durationMillis) {
        List<SessionResult> results = learningSessionResultRepository.findAll();
        results.add(new SessionResult(sessionName, correctCount, totalCount, durationMillis));
        learningSessionResultRepository.saveAll(results);
    }
}

