package de.sagaweschaefer.flashcard.model;

import java.util.Map;

public class FlashcardSetStatistics extends BaseStatistics {
    private final String setName;

    private FlashcardSetStatistics(String setName, int totalQuestions, int totalCorrect, int totalWrong,
                                   int dueCards, int[] levelDistribution, double correctPercentage) {
        super(totalQuestions, totalCorrect, totalWrong, dueCards, levelDistribution, correctPercentage);
        this.setName = setName;
    }

    public static FlashcardSetStatistics calculate(FlashcardSet set, Map<String, FlashcardStatistics> statsMap) {
        int totalCorrect = 0;
        int totalWrong = 0;
        int dueCards = 0;
        int[] levelDistribution = new int[7];

        for (Flashcard card : set.getFlashcards()) {
            FlashcardStatistics stats = statsMap.get(card.getId());
            if (stats != null) {
                totalCorrect += stats.getCorrectCount();
                totalWrong += stats.getWrongCount();
                if (stats.isDue()) dueCards++;
                int level = stats.getLevel();
                if (level >= 0 && level <= 6) levelDistribution[level]++;
            } else {
                dueCards++;
                levelDistribution[0]++;
            }
        }

        double correctPercentage = calculatePercentage(totalCorrect, totalWrong);

        return new FlashcardSetStatistics(set.getName(), set.getFlashcards().size(), totalCorrect, totalWrong,
                dueCards, levelDistribution, correctPercentage);
    }

    private static double calculatePercentage(int correct, int wrong) {
        int total = correct + wrong;
        return total > 0 ? (double) correct / total * 100 : 0;
    }

    public String getSetName() {
        return setName;
    }
}
