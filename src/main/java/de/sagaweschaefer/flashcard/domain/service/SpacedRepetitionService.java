package de.sagaweschaefer.flashcard.domain.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class SpacedRepetitionService {

    public static final int MIN_LEVEL = 0;

    public static final int MAX_LEVEL = 6;

    public static final int RATING_BAD = 1;

    public static final int RATING_OK = 2;

    public static final int RATING_GOOD = 3;

    private final Clock clock;

    public SpacedRepetitionService() {
        this(Clock.system(ZoneId.systemDefault()));
    }

    public SpacedRepetitionService(Clock clock) {
        if (clock == null) {
            throw new IllegalArgumentException("clock darf nicht null sein");
        }
        this.clock = clock;
    }

    public boolean isDue(int level, LocalDateTime lastCorrectAt) {
        if (level <= MIN_LEVEL || lastCorrectAt == null) {
            return true;
        }
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime nextDue = nextDueDate(level, lastCorrectAt);
        return !nextDue.isAfter(now);
    }

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

    public int applyRating(int currentLevel, int rating, boolean wasDue) {
        int level = clamp(currentLevel);
        return switch (rating) {
            case RATING_BAD -> Math.max(MIN_LEVEL, level - 1);
            case RATING_OK -> level;
            case RATING_GOOD -> wasDue ? Math.min(MAX_LEVEL, level + 1) : level;
            default -> level;
        };
    }

    public int levelAfterWrongAnswer() {
        return MIN_LEVEL;
    }

    private int clamp(int level) {
        return Math.clamp(level, MIN_LEVEL, MAX_LEVEL);
    }
}

