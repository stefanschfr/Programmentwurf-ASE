package de.sagaweschaefer.flashcard.domain.repository;

import de.sagaweschaefer.flashcard.domain.valueobject.DailyGoal;
import de.sagaweschaefer.flashcard.model.DailyProgress;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repository für das tägliche Lernziel und den Tagesfortschritt.
 *
 * <p>Reine Domain-Schnittstelle (DDD-Repository). Konkrete Implementierungen
 * liegen im Adapter-Layer (z.&nbsp;B. JSON-basierte Persistenz).</p>
 */
@SuppressWarnings("unused") // Public Domain-API: implementiert/verwendet von Adapter, UseCases, Menu
public interface DailyGoalRepository {

    /** Liefert das aktuell konfigurierte Tagesziel, falls eines gesetzt ist. */
    Optional<DailyGoal> findGoal();

    /** Speichert das Tagesziel persistent. */
    void saveGoal(DailyGoal goal);

    /** Liefert den Fortschritt für ein konkretes Datum, falls vorhanden. */
    Optional<DailyProgress> findProgress(LocalDate date);

    /** Speichert/aktualisiert den Fortschritt für den enthaltenen Tag. */
    void saveProgress(DailyProgress progress);
}


