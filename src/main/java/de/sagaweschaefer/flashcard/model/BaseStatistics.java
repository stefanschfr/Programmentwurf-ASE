package de.sagaweschaefer.flashcard.model;

public abstract class BaseStatistics {
    protected final int totalQuestions;
    protected final int totalCorrect;
    protected final int totalWrong;
    protected final int dueCards;
    protected final int[] levelDistribution;
    protected final double correctPercentage;

    protected BaseStatistics(int totalQuestions, int totalCorrect, int totalWrong,
                             int dueCards, int[] levelDistribution, double correctPercentage) {
        this.totalQuestions = totalQuestions;
        this.totalCorrect = totalCorrect;
        this.totalWrong = totalWrong;
        this.dueCards = dueCards;
        this.levelDistribution = levelDistribution;
        this.correctPercentage = correctPercentage;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getTotalCorrect() {
        return totalCorrect;
    }

    public int getTotalWrong() {
        return totalWrong;
    }

    public int getDueCards() {
        return dueCards;
    }

    public int[] getLevelDistribution() {
        return levelDistribution;
    }

    public double getCorrectPercentage() {
        return correctPercentage;
    }
}
