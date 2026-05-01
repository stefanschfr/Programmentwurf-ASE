package de.sagaweschaefer.flashcard.domain.observer;

import de.sagaweschaefer.flashcard.domain.repository.SessionResultRepository;
import de.sagaweschaefer.flashcard.domain.repository.StatisticsRepository;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.model.SessionResult;

import java.util.List;
import java.util.Map;

public class StatisticsObserver implements SessionObserver {
    private final StatisticsRepository statisticsRepository;
    private final SessionResultRepository sessionResultRepository;
    private Map<String, FlashcardStatistics> statisticsMap;

    public StatisticsObserver(StatisticsRepository statisticsRepository, SessionResultRepository sessionResultRepository) {
        this.statisticsRepository = statisticsRepository;
        this.sessionResultRepository = sessionResultRepository;
    }

    public void loadStatistics() {
        this.statisticsMap = statisticsRepository.findAll();
    }

    public Map<String, FlashcardStatistics> getStatisticsMap() {
        return statisticsMap;
    }

    @Override
    public void onQuestionAnswered(Flashcard card, boolean correct, int rating, boolean wasDue) {
        FlashcardStatistics stats = statisticsMap.computeIfAbsent(card.getId(), FlashcardStatistics::new);
        if (correct) {
            stats.incrementCorrect();
            stats.applyRating(rating, wasDue);
        } else {
            stats.incrementWrong();
        }
    }

    @Override
    public void onSessionFinished(String sessionName, int correctCount, int totalCount, long durationMillis) {
        statisticsRepository.saveAll(statisticsMap);
        List<SessionResult> results = sessionResultRepository.findAll();
        results.add(new SessionResult(sessionName, correctCount, totalCount, durationMillis));
        sessionResultRepository.saveAll(results);
    }
}

