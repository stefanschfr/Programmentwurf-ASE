package de.sagaweschaefer.flashcard.domain.service;

import de.sagaweschaefer.flashcard.domain.repository.DailyGoalRepository;
import de.sagaweschaefer.flashcard.domain.valueobject.DailyGoal;
import de.sagaweschaefer.flashcard.model.DailyProgress;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Domain-Service für den täglichen Lernplan.
 *
 * <p>Aufgaben:
 * <ul>
 *     <li>Verwaltung des Tagesziels (Anzahl Karten/Tag)</li>
 *     <li>Berechnung und Persistenz des Tagesfortschritts</li>
 *     <li>Vorschlag von Karten für eine Lerneinheit (zuerst fällige, dann neue)</li>
 * </ul>
 * </p>
 *
 * <p>Reine Domänenlogik. Persistenz erfolgt ausschließlich über das injizierte
 * {@link DailyGoalRepository}; das aktuelle Datum kommt aus einer {@link Clock},
 * sodass der Service deterministisch testbar ist.</p>
 */
@SuppressWarnings("unused") // Public Domain-API: wird aus UseCases, Menus und Tests genutzt
public class DailyLearningPlanService {

    private final DailyGoalRepository repository;
    private final SpacedRepetitionService spacedRepetitionService;
    private final Clock clock;

    public DailyLearningPlanService(DailyGoalRepository repository) {
        this(repository, new SpacedRepetitionService(), Clock.system(ZoneId.systemDefault()));
    }

    public DailyLearningPlanService(DailyGoalRepository repository,
                                    SpacedRepetitionService spacedRepetitionService,
                                    Clock clock) {
        if (repository == null) throw new IllegalArgumentException("repository darf nicht null sein");
        if (spacedRepetitionService == null) throw new IllegalArgumentException("spacedRepetitionService darf nicht null sein");
        if (clock == null) throw new IllegalArgumentException("clock darf nicht null sein");
        this.repository = repository;
        this.spacedRepetitionService = spacedRepetitionService;
        this.clock = clock;
    }

    /** Setzt das Tagesziel. */
    public void setGoal(DailyGoal goal) {
        if (goal == null) throw new IllegalArgumentException("goal darf nicht null sein");
        repository.saveGoal(goal);
    }

    /** Liefert das aktuelle Tagesziel oder den Default ({@link DailyGoal#defaultGoal()}). */
    public DailyGoal getGoalOrDefault() {
        return repository.findGoal().orElseGet(DailyGoal::defaultGoal);
    }

    /** Liefert den Fortschritt für heute (legt einen leeren Eintrag an, falls noch keiner existiert). */
    public DailyProgress getOrCreateTodayProgress() {
        LocalDate today = LocalDate.now(clock);
        Optional<DailyProgress> existing = repository.findProgress(today);
        if (existing.isPresent()) {
            return existing.get();
        }
        DailyProgress fresh = new DailyProgress(today, getGoalOrDefault().getCardsPerDay());
        repository.saveProgress(fresh);
        return fresh;
    }

    /**
     * Verbucht eine gelernte Karte im Tagesfortschritt.
     *
     * @param correct {@code true} wenn die Antwort korrekt war
     * @return aktualisierter Fortschritt
     */
    public DailyProgress recordCardLearned(boolean correct) {
        DailyProgress progress = getOrCreateTodayProgress();
        progress.recordCard(correct);
        repository.saveProgress(progress);
        return progress;
    }

    /**
     * Wählt Karten für die heutige Lerneinheit aus.
     *
     * <p>Strategie:
     * <ol>
     *     <li>Zuerst alle fälligen Karten (über {@link SpacedRepetitionService})</li>
     *     <li>Dann neue Karten (ohne Statistik) auffüllen</li>
     *     <li>Begrenzung auf das verbleibende Tagesziel</li>
     * </ol>
     * </p>
     *
     * @param sets       alle vorhandenen FlashcardSets
     * @param statistics Statistiken pro Karte (kann leer sein)
     * @return Liste empfohlener Karten (kann leer sein)
     */
    public List<Flashcard> recommendCardsForToday(List<FlashcardSet> sets,
                                                  Map<String, FlashcardStatistics> statistics) {
        if (sets == null || sets.isEmpty()) {
            return List.of();
        }
        DailyProgress progress = getOrCreateTodayProgress();
        int remaining = Math.max(0, progress.getGoalCards() - progress.getLearnedCards());
        if (remaining == 0) {
            return List.of();
        }

        Map<String, FlashcardStatistics> stats = (statistics == null) ? Map.of() : statistics;

        List<Flashcard> due = new ArrayList<>();
        List<Flashcard> fresh = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (FlashcardSet set : sets) {
            if (set == null || set.getFlashcards() == null) continue;
            for (Flashcard card : set.getFlashcards()) {
                if (card == null || card.getId() == null) continue;
                if (!seen.add(card.getId())) continue;

                FlashcardStatistics s = stats.get(card.getId());
                if (s == null) {
                    fresh.add(card);
                } else if (spacedRepetitionService.isDue(s.getLevel(), s.getLastCorrectAt())) {
                    due.add(card);
                }
            }
        }

        List<Flashcard> result = new ArrayList<>(remaining);
        for (Flashcard c : due) {
            if (result.size() >= remaining) break;
            result.add(c);
        }
        for (Flashcard c : fresh) {
            if (result.size() >= remaining) break;
            result.add(c);
        }
        return result;
    }
}

