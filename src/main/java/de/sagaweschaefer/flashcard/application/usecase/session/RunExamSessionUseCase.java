package de.sagaweschaefer.flashcard.application.usecase.session;

import de.sagaweschaefer.flashcard.application.port.ExamSessionResultRepository;
import de.sagaweschaefer.flashcard.application.port.FlashcardStatisticsRepository;
import de.sagaweschaefer.flashcard.application.port.SessionInteraction;
import de.sagaweschaefer.flashcard.application.session.PreparedSession;
import de.sagaweschaefer.flashcard.application.session.SessionSummary;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardId;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.model.SessionResult;

import java.util.List;
import java.util.Map;

public class RunExamSessionUseCase {
    private final FlashcardStatisticsRepository flashcardStatisticsRepository;
    private final ExamSessionResultRepository examSessionResultRepository;
    private final SessionInteraction sessionInteraction;

    public RunExamSessionUseCase(FlashcardStatisticsRepository flashcardStatisticsRepository,
                                 ExamSessionResultRepository examSessionResultRepository,
                                 SessionInteraction sessionInteraction) {
        this.flashcardStatisticsRepository = flashcardStatisticsRepository;
        this.examSessionResultRepository = examSessionResultRepository;
        this.sessionInteraction = sessionInteraction;
    }

    public SessionSummary execute(PreparedSession preparedSession) {
        if (preparedSession.getFlashcards().isEmpty()) {
            throw new IllegalArgumentException("Keine Karten für die Prüfung verfügbar.");
        }

        long startTime = System.currentTimeMillis();
        int correctCount = 0;
        int totalQuestions = preparedSession.getFlashcards().size();
        boolean timedOut = false;
        Map<String, FlashcardStatistics> statisticsMap = flashcardStatisticsRepository.findAll();

        sessionInteraction.showSessionStarted(preparedSession);

        for (int i = 0; i < preparedSession.getFlashcards().size(); i++) {
            long remainingTime = preparedSession.getTimeLimitMillis() - (System.currentTimeMillis() - startTime);
            if (remainingTime <= 0) {
                timedOut = true;
                sessionInteraction.showTimeExpired();
                break;
            }

            Flashcard card = preparedSession.getFlashcards().get(i);
            sessionInteraction.showExamProgress(i + 1, totalQuestions, remainingTime);
            if (processCard(card, statisticsMap)) {
                correctCount++;
            }
        }

        flashcardStatisticsRepository.saveAll(statisticsMap);
        long durationMillis = System.currentTimeMillis() - startTime;
        saveResult(preparedSession.getSessionName(), correctCount, totalQuestions, durationMillis);

        SessionSummary summary = new SessionSummary(
                preparedSession.getSessionName(),
                preparedSession.getMode(),
                correctCount,
                totalQuestions,
                durationMillis,
                timedOut
        );
        sessionInteraction.showSessionFinished(summary);
        return summary;
    }

    private boolean processCard(Flashcard card, Map<String, FlashcardStatistics> statisticsMap) {
        FlashcardStatistics statistics = statisticsMap.computeIfAbsent(card.getId(), id -> new FlashcardStatistics(FlashcardId.of(id)));
        if (sessionInteraction.askQuestion(card)) {
            sessionInteraction.showCorrectAnswerFeedback();
            statistics.incrementCorrect();
            return true;
        }

        sessionInteraction.showWrongAnswerFeedback(card);
        statistics.incrementWrong();
        return false;
    }

    private void saveResult(String sessionName, int correctCount, int totalCount, long durationMillis) {
        List<SessionResult> results = examSessionResultRepository.findAll();
        results.add(new SessionResult(sessionName, correctCount, totalCount, durationMillis));
        examSessionResultRepository.saveAll(results);
    }
}

