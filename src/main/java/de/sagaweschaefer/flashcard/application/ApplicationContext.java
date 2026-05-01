package de.sagaweschaefer.flashcard.application;

import de.sagaweschaefer.flashcard.adapter.persistence.JsonFlashcardSetRepository;
import de.sagaweschaefer.flashcard.adapter.persistence.JsonSessionResultRepository;
import de.sagaweschaefer.flashcard.adapter.persistence.JsonStatisticsRepository;
import de.sagaweschaefer.flashcard.domain.repository.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.domain.repository.SessionResultRepository;
import de.sagaweschaefer.flashcard.domain.repository.StatisticsRepository;

public class ApplicationContext {
    private static final String FLASHCARD_SETS_PATH = "data/flashcard-sets.json";
    private static final String STATISTICS_PATH = "data/flashcard-statistics.json";
    private static final String SESSION_RESULTS_PATH = "data/session-results.json";
    private static final String EXAM_RESULTS_PATH = "data/exam-results.json";

    private final FlashcardSetRepository flashcardSetRepository;
    private final StatisticsRepository statisticsRepository;
    private final SessionResultRepository sessionResultRepository;
    private final SessionResultRepository examResultRepository;

    public ApplicationContext() {
        this.flashcardSetRepository = new JsonFlashcardSetRepository(FLASHCARD_SETS_PATH);
        this.statisticsRepository = new JsonStatisticsRepository(STATISTICS_PATH);
        this.sessionResultRepository = new JsonSessionResultRepository(SESSION_RESULTS_PATH);
        this.examResultRepository = new JsonSessionResultRepository(EXAM_RESULTS_PATH);
    }

    public FlashcardSetRepository getFlashcardSetRepository() {
        return flashcardSetRepository;
    }

    public StatisticsRepository getStatisticsRepository() {
        return statisticsRepository;
    }

    public SessionResultRepository getSessionResultRepository() {
        return sessionResultRepository;
    }

    public SessionResultRepository getExamResultRepository() {
        return examResultRepository;
    }
}

