package de.sagaweschaefer.flashcard.domain.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Domain-Service, der die Spaced-Repetition-Logik kapselt.
 *
 * <p>Reine Domänenlogik: enthält keine Abhängigkeiten zu Persistenz, UI oder
 * dem Application-Layer. Der Service ist durch einen injizierbaren {@link Clock}
 * vollständig deterministisch testbar.</p>
 *
 * <p>Die Klasse berechnet:
 * <ul>
 *     <li>ob eine Karte basierend auf ihrem Level/Last-Correct-Zeitpunkt fällig ist</li>
 *     <li>das nächste Level nach Anwendung eines Ratings (1, 2, 3)</li>
 *     <li>die Fälligkeit nach einer falschen Antwort (immer Level 0)</li>
 * </ul>
 * </p>
 */
@SuppressWarnings("unused") // Public Domain-API: wird aus FlashcardStatistics, UseCases und Tests genutzt
public class SpacedRepetitionService {

    /** Niedrigstes Level (Karte gilt als unbekannt / sofort fällig). */
    public static final int MIN_LEVEL = 0;

    /** Höchstes erreichbares Level. */
    public static final int MAX_LEVEL = 6;

    /** Rating: schlecht beantwortet (Level wird reduziert). */
    public static final int RATING_BAD = 1;

    /** Rating: ok beantwortet (Level bleibt). */
    public static final int RATING_OK = 2;

    /** Rating: sehr gut beantwortet (Level wird erhöht, sofern fällig). */
    public static final int RATING_GOOD = 3;

    private final Clock clock;

    /** Erzeugt einen Service mit System-Default-Clock. */
    public SpacedRepetitionService() {
        this(Clock.system(ZoneId.systemDefault()));
    }

    /** Erzeugt einen Service mit injizierter {@link Clock} (für deterministisches Testen). */
    public SpacedRepetitionService(Clock clock) {
        if (clock == null) {
            throw new IllegalArgumentException("clock darf nicht null sein");
        }
        this.clock = clock;
    }

    /**
     * Prüft, ob eine Karte mit gegebenem Level und Zeitpunkt der letzten korrekten
     * Antwort aktuell wieder fällig ist.
     *
     * @param level         aktuelles Level (0..MAX_LEVEL)
     * @param lastCorrectAt Zeitpunkt der letzten korrekten Antwort, oder {@code null} wenn nie
     * @return {@code true} wenn die Karte gelernt werden sollte
     */
    public boolean isDue(int level, LocalDateTime lastCorrectAt) {
        if (level <= MIN_LEVEL || lastCorrectAt == null) {
            return true;
        }
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime nextDue = nextDueDate(level, lastCorrectAt);
        return !nextDue.isAfter(now);
    }

    /**
     * Berechnet den Zeitpunkt, an dem eine Karte das nächste Mal fällig wird.
     *
     * @param level         aktuelles Level
     * @param lastCorrectAt Zeitpunkt der letzten korrekten Antwort
     * @return Zeitpunkt der nächsten Fälligkeit
     */
    public LocalDateTime nextDueDate(int level, LocalDateTime lastCorrectAt) {
        if (lastCorrectAt == null) {
            return LocalDateTime.now(clock);
        }
        return switch (clamp(level)) {
            case 0 -> lastCorrectAt;
            case 1 -> lastCorrectAt.plusMinutes(1);
            case 2 -> lastCorrectAt.plusMinutes(10);
            case 3 -> lastCorrectAt.plusHours(5);
            case 4 -> lastCorrectAt.plusDays(1);
            case 5 -> lastCorrectAt.plusDays(14);
            default -> lastCorrectAt.plusMonths(1); // Level 6
        };
    }

    /**
     * Berechnet das neue Level basierend auf dem Rating einer korrekten Antwort.
     *
     * <ul>
     *     <li>Rating 1 (schlecht): Level -1 (mind. {@link #MIN_LEVEL})</li>
     *     <li>Rating 2 (ok): Level bleibt</li>
     *     <li>Rating 3 (sehr gut): Level +1 wenn die Karte fällig war (max. {@link #MAX_LEVEL}),
     *         sonst keine Änderung</li>
     * </ul>
     *
     * @param currentLevel aktuelles Level
     * @param rating       Rating (1, 2 oder 3)
     * @param wasDue       ob die Karte vor der Antwort fällig war
     * @return neues Level
     */
    public int applyRating(int currentLevel, int rating, boolean wasDue) {
        int level = clamp(currentLevel);
        return switch (rating) {
            case RATING_BAD -> Math.max(MIN_LEVEL, level - 1);
            case RATING_OK -> level;
            case RATING_GOOD -> wasDue ? Math.min(MAX_LEVEL, level + 1) : level;
            default -> level;
        };
    }

    /**
     * Liefert das Level, das nach einer falschen Antwort gelten soll (immer {@link #MIN_LEVEL}).
     */
    public int levelAfterWrongAnswer() {
        return MIN_LEVEL;
    }

    /** Begrenzt das Level auf den gültigen Bereich [MIN_LEVEL, MAX_LEVEL]. */
    private int clamp(int level) {
        return Math.min(MAX_LEVEL, Math.max(MIN_LEVEL, level));
    }
}

