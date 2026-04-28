package de.sagaweschaefer.flashcard.application.usecase.session;

import de.sagaweschaefer.flashcard.application.port.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.application.port.FlashcardStatisticsRepository;
import de.sagaweschaefer.flashcard.application.session.PreparedSession;
import de.sagaweschaefer.flashcard.application.session.SessionMode;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PrepareSessionUseCase {
    private static final int EXAM_QUESTION_COUNT = 10;
    private static final long EXAM_TIME_LIMIT_MILLIS = 10 * 60 * 1000;

    private final FlashcardSetRepository flashcardSetRepository;
    private final FlashcardStatisticsRepository flashcardStatisticsRepository;

    public PrepareSessionUseCase(FlashcardSetRepository flashcardSetRepository,
                                 FlashcardStatisticsRepository flashcardStatisticsRepository) {
        this.flashcardSetRepository = flashcardSetRepository;
        this.flashcardStatisticsRepository = flashcardStatisticsRepository;
    }

    public PreparedSession prepareSetLearningSession(FlashcardSet selectedSet) {
        FlashcardSet managedSet = requireManagedSet(selectedSet);
        Map<String, FlashcardStatistics> statisticsMap = flashcardStatisticsRepository.findAll();
        List<Flashcard> cardsToLearn = new ArrayList<>();

        for (Flashcard flashcard : managedSet.getFlashcards()) {
            FlashcardStatistics statistics = statisticsMap.get(flashcard.getId());
            if (statistics == null || statistics.getLevel() < 6) {
                cardsToLearn.add(flashcard);
            }
        }

        if (cardsToLearn.isEmpty()) {
            throw new IllegalArgumentException("Alle Karten in diesem Set sind bereits vollständig gelernt (Stufe 6)!");
        }

        return new PreparedSession(SessionMode.SET_LEARNING, managedSet.getName(), cardsToLearn, 0);
    }

    public PreparedSession prepareWrongAnswersSession() {
        List<FlashcardSet> flashcardSets = requireAvailableSets();
        Map<String, FlashcardStatistics> statisticsMap = flashcardStatisticsRepository.findAll();
        List<Flashcard> wrongAnswers = new ArrayList<>();

        for (FlashcardSet set : flashcardSets) {
            for (Flashcard flashcard : set.getFlashcards()) {
                FlashcardStatistics statistics = statisticsMap.get(flashcard.getId());
                if (statistics != null && statistics.getLevel() == 0) {
                    wrongAnswers.add(flashcard);
                }
            }
        }

        if (wrongAnswers.isEmpty()) {
            throw new IllegalArgumentException("Es sind keine falsch beantworteten Fragen (Stufe 0) vorhanden.");
        }

        return new PreparedSession(SessionMode.WRONG_ANSWERS, "Falsch beantwortete Fragen (Stufe 0)", wrongAnswers, 0);
    }

    public PreparedSession prepareDueCardsSession() {
        List<FlashcardSet> flashcardSets = requireAvailableSets();
        Map<String, FlashcardStatistics> statisticsMap = flashcardStatisticsRepository.findAll();
        List<Flashcard> dueCards = new ArrayList<>();

        for (FlashcardSet set : flashcardSets) {
            for (Flashcard flashcard : set.getFlashcards()) {
                FlashcardStatistics statistics = statisticsMap.get(flashcard.getId());
                if (statistics == null || statistics.isDue()) {
                    dueCards.add(flashcard);
                }
            }
        }

        if (dueCards.isEmpty()) {
            throw new IllegalArgumentException("Es sind momentan keine Lernkarten fällig.");
        }

        return new PreparedSession(SessionMode.DUE_CARDS, "Fällige Lernkarten", dueCards, 0);
    }

    public PreparedSession prepareExamSession(FlashcardSet selectedSet) {
        FlashcardSet managedSet = requireManagedSet(selectedSet);
        List<Flashcard> examCards = new ArrayList<>(managedSet.getFlashcards());
        if (examCards.size() < EXAM_QUESTION_COUNT) {
            throw new IllegalArgumentException("Das gewählte Set enthält nur " + examCards.size() + " Karten. Für den Prüfungsmodus sind mindestens 10 Karten erforderlich.");
        }

        Collections.shuffle(examCards);
        return new PreparedSession(
                SessionMode.EXAM,
                managedSet.getName(),
                examCards.subList(0, EXAM_QUESTION_COUNT),
                EXAM_TIME_LIMIT_MILLIS
        );
    }

    private FlashcardSet requireManagedSet(FlashcardSet selectedSet) {
        List<FlashcardSet> flashcardSets = requireAvailableSets();
        return flashcardSets.stream()
                .filter(set -> isSameSet(set, selectedSet))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Das ausgewählte Lernkartenset wurde nicht gefunden."));
    }

    private List<FlashcardSet> requireAvailableSets() {
        List<FlashcardSet> flashcardSets = flashcardSetRepository.findAll();
        if (flashcardSets.isEmpty()) {
            throw new IllegalArgumentException("Es sind keine Lernkartensets verfügbar.");
        }
        return flashcardSets;
    }

    private boolean isSameSet(FlashcardSet first, FlashcardSet second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (first.getName() == null || second.getName() == null) {
            return false;
        }
        return first.getName().equalsIgnoreCase(second.getName());
    }
}

