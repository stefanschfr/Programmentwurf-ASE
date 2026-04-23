package de.sagaweschaefer.flashcard.util;

import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsCalculator {

    public static GeneralStats calculateGeneralStats(List<FlashcardSet> sets, Map<String, FlashcardStatistics> statsMap) {
        GeneralStats stats = new GeneralStats();
        stats.totalSets = sets.size();

        Map<String, String> idToQuestionMap = new HashMap<>();
        for (FlashcardSet set : sets) {
            for (Flashcard card : set.getFlashcards()) {
                stats.totalQuestions++;
                idToQuestionMap.put(card.getId(), card.getQuestion());
            }
        }

        FlashcardStatistics mostFrequentStats = null;
        double totalLevel = 0;

        for (FlashcardStatistics s : statsMap.values()) {
            stats.totalCorrect += s.getCorrectCount();
            stats.totalWrong += s.getWrongCount();

            if (s.getCorrectCount() == 0 && s.getWrongCount() == 0) {
                stats.neverAnswered++;
            }

            if (s.isDue()) {
                stats.dueCards++;
            }

            int level = s.getLevel();
            if (level >= 0 && level <= 6) {
                stats.levelDistribution[level]++;
                totalLevel += level;
            }

            int count = s.getCorrectCount() + s.getWrongCount();
            if (mostFrequentStats == null || count > (mostFrequentStats.getCorrectCount() + mostFrequentStats.getWrongCount())) {
                mostFrequentStats = s;
            }
        }

        // Karten ohne Statistiken berücksichtigen
        int totalCardsInStats = statsMap.size();
        int missingCards = stats.totalQuestions - totalCardsInStats;
        if (missingCards > 0) {
            stats.neverAnswered += missingCards;
            stats.dueCards += missingCards;
            stats.levelDistribution[0] += missingCards;
        }

        if (mostFrequentStats != null && (mostFrequentStats.getCorrectCount() + mostFrequentStats.getWrongCount()) > 0) {
            stats.mostFrequentQuestion = idToQuestionMap.getOrDefault(mostFrequentStats.getFlashcardId(), "Unbekannte Frage");
            stats.mostFrequentCount = mostFrequentStats.getCorrectCount() + mostFrequentStats.getWrongCount();
        }

        int totalAnswers = stats.totalCorrect + stats.totalWrong;
        stats.correctPercentage = totalAnswers > 0 ? (double) stats.totalCorrect / totalAnswers * 100 : 0;
        stats.averageLevel = stats.totalQuestions > 0 ? totalLevel / stats.totalQuestions : 0;

        return stats;
    }

    public static class GeneralStats {
        public int totalSets;
        public int totalQuestions;
        public int totalCorrect;
        public int totalWrong;
        public int neverAnswered;
        public int dueCards;
        public int[] levelDistribution = new int[7];
        public double averageLevel;
        public double correctPercentage;
        public String mostFrequentQuestion = "N/A";
        public int mostFrequentCount = 0;
    }
}
