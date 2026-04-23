package de.sagaweschaefer.flashcard.model;

import java.util.Map;

public class FlashcardSetStatistics extends BaseStatistics {
    private final String setName;

    private FlashcardSetStatistics(String setName, int totalQuestions, int totalCorrect, int totalWrong,
                                   int dueCards, int[] levelDistribution, double correctPercentage,
                                   double averageLevel, int neverAnswered) {
        super(totalQuestions, totalCorrect, totalWrong, dueCards, levelDistribution, correctPercentage, averageLevel, neverAnswered);
        this.setName = setName;
    }

    public static FlashcardSetStatistics calculate(FlashcardSet set, Map<String, FlashcardStatistics> statsMap) {
        int totalCorrect = 0;
        int totalWrong = 0;
        int dueCards = 0;
        int neverAnswered = 0;
        double totalLevel = 0;
        int[] levelDistribution = new int[7];

        for (Flashcard card : set.getFlashcards()) {
            FlashcardStatistics stats = statsMap.get(card.getId());
            if (stats != null) {
                totalCorrect += stats.getCorrectCount();
                totalWrong += stats.getWrongCount();
                if (stats.getCorrectCount() == 0 && stats.getWrongCount() == 0) neverAnswered++;
                if (stats.isDue()) dueCards++;
                int level = stats.getLevel();
                if (level >= 0 && level <= 6) {
                    levelDistribution[level]++;
                    totalLevel += level;
                }
            } else {
                dueCards++;
                neverAnswered++;
                levelDistribution[0]++;
            }
        }

        double correctPercentage = calculatePercentage(totalCorrect, totalWrong);
        double averageLevel = set.getFlashcards().isEmpty() ? 0 : totalLevel / set.getFlashcards().size();

        return new FlashcardSetStatistics(set.getName(), set.getFlashcards().size(), totalCorrect, totalWrong,
                dueCards, levelDistribution, correctPercentage, averageLevel, neverAnswered);
    }

    private static double calculatePercentage(int correct, int wrong) {
        int total = correct + wrong;
        return total > 0 ? (double) correct / total * 100 : 0;
    }

    public String getSetName() {
        return setName;
    }
}
