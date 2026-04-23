package de.sagaweschaefer.flashcard.model;

import java.util.List;
import java.util.Map;

public class GeneralStatistics extends BaseStatistics {
    private final int totalSets;
    private final int neverAnswered;
    private final double averageLevel;
    private final String mostFrequentQuestion;
    private final int mostFrequentCount;

    private GeneralStatistics(int totalSets, int totalQuestions, int totalCorrect, int totalWrong,
                              int neverAnswered, int dueCards, int[] levelDistribution,
                              double averageLevel, double correctPercentage,
                              String mostFrequentQuestion, int mostFrequentCount) {
        super(totalQuestions, totalCorrect, totalWrong, dueCards, levelDistribution, correctPercentage);
        this.totalSets = totalSets;
        this.neverAnswered = neverAnswered;
        this.averageLevel = averageLevel;
        this.mostFrequentQuestion = mostFrequentQuestion;
        this.mostFrequentCount = mostFrequentCount;
    }

    public static GeneralStatistics calculate(List<FlashcardSet> sets, Map<String, FlashcardStatistics> statsMap) {
        int totalQuestions = sets.stream().mapToInt(s -> s.getFlashcards().size()).sum();
        int totalCorrect = statsMap.values().stream().mapToInt(FlashcardStatistics::getCorrectCount).sum();
        int totalWrong = statsMap.values().stream().mapToInt(FlashcardStatistics::getWrongCount).sum();
        long dueInStats = statsMap.values().stream().filter(FlashcardStatistics::isDue).count();

        int[] levelDistribution = new int[7];
        double totalLevel = 0;
        FlashcardStatistics mostFrequentStats = null;

        for (FlashcardStatistics s : statsMap.values()) {
            int level = s.getLevel();
            if (level >= 0 && level <= 6) {
                levelDistribution[level]++;
                totalLevel += level;
            }

            int count = s.getCorrectCount() + s.getWrongCount();
            if (mostFrequentStats == null || count > (mostFrequentStats.getCorrectCount() + mostFrequentStats.getWrongCount())) {
                mostFrequentStats = s;
            }
        }

        int missingCards = totalQuestions - statsMap.size();
        int neverAnswered = (int) statsMap.values().stream().filter(s -> s.getCorrectCount() == 0 && s.getWrongCount() == 0).count() + Math.max(0, missingCards);
        int dueCards = (int) dueInStats + Math.max(0, missingCards);
        if (missingCards > 0) {
            levelDistribution[0] += missingCards;
        }

        String mostFrequentQuestion = "N/A";
        int mostFrequentCount = 0;
        if (mostFrequentStats != null && (mostFrequentStats.getCorrectCount() + mostFrequentStats.getWrongCount()) > 0) {
            mostFrequentQuestion = findQuestionById(sets, mostFrequentStats.getFlashcardId());
            mostFrequentCount = mostFrequentStats.getCorrectCount() + mostFrequentStats.getWrongCount();
        }

        double correctPercentage = calculatePercentage(totalCorrect, totalWrong);
        double averageLevel = totalQuestions > 0 ? totalLevel / totalQuestions : 0;

        return new GeneralStatistics(sets.size(), totalQuestions, totalCorrect, totalWrong,
                neverAnswered, dueCards, levelDistribution, averageLevel, correctPercentage,
                mostFrequentQuestion, mostFrequentCount);
    }

    private static String findQuestionById(List<FlashcardSet> sets, String id) {
        return sets.stream()
                .flatMap(s -> s.getFlashcards().stream())
                .filter(c -> c.getId().equals(id))
                .map(Flashcard::getQuestion)
                .findFirst()
                .orElse("Unbekannte Frage");
    }

    private static double calculatePercentage(int correct, int wrong) {
        int total = correct + wrong;
        return total > 0 ? (double) correct / total * 100 : 0;
    }

    public int getTotalSets() {
        return totalSets;
    }

    public int getNeverAnswered() {
        return neverAnswered;
    }

    public double getAverageLevel() {
        return averageLevel;
    }

    public String getMostFrequentQuestion() {
        return mostFrequentQuestion;
    }

    public int getMostFrequentCount() {
        return mostFrequentCount;
    }
}
