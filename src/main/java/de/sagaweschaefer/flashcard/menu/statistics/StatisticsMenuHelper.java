package de.sagaweschaefer.flashcard.menu.statistics;

import de.sagaweschaefer.flashcard.menu.flashcardsession.FlashcardSessionStatistics;
import de.sagaweschaefer.flashcard.model.*;
import de.sagaweschaefer.flashcard.util.JsonStorage;
import de.sagaweschaefer.flashcard.util.MenuUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsMenuHelper {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final JsonStorage storage;

    public StatisticsMenuHelper(JsonStorage storage) {
        this.storage = storage;
    }

    public void showGeneralStatistics() {
        List<FlashcardSet> sets = storage.loadFlashcardSets();
        Map<String, FlashcardStatistics> statsMap = storage.loadStatistics();
        GeneralStatistics stats = GeneralStatistics.calculate(sets, statsMap);

        printHeader("Gesamtstatistik");
        System.out.println("Gesamte Anzahl an Lernsets: " + stats.getTotalSets());
        printBaseStatistics(stats);

        printLevelDistribution(stats.getLevelDistribution());

        if (!"N/A".equals(stats.getMostFrequentQuestion())) {
            System.out.println("\nMeistgeübte Karte: \"" + stats.getMostFrequentQuestion() + "\" (" + stats.getMostFrequentCount() + " Versuche)");
        }

        System.out.println("\nSet-Highlights:");
        if (!"N/A".equals(stats.getBestSetName())) {
            System.out.printf("  Bestes Set: %s (%.2f%% richtig)\n", stats.getBestSetName(), stats.getBestSetPercentage());
        }
        if (!"N/A".equals(stats.getWorstSetName())) {
            System.out.printf("  Schlechtestes Set: %s (%.2f%% richtig)\n", stats.getWorstSetName(), stats.getWorstSetPercentage());
        }

        List<SessionResult> sessionResults = storage.loadSessionResults();
        if (!sessionResults.isEmpty()) {
            SessionResult last = sessionResults.getLast();
            System.out.println("\nLetzte Aktivität: " + last.getTimestamp().format(DATE_TIME_FORMATTER) + " (" + last.getSessionName() + ")");
        }
    }

    public void showSetStatistics() {
        List<FlashcardSet> sets = storage.loadFlashcardSets();
        if (sets.isEmpty()) {
            System.out.println("Keine Lernsets vorhanden.");
            return;
        }

        MenuUtils.displayFlashcardSets(sets, "Statistik nach Set");
        FlashcardSet selectedSet = MenuUtils.selectFromList(sets, "Bitte wählen Sie ein Set aus (0 zum Abbrechen): ");
        if (selectedSet == null) return;

        Map<String, FlashcardStatistics> statsMap = storage.loadStatistics();
        FlashcardSetStatistics stats = FlashcardSetStatistics.calculate(selectedSet, statsMap);

        printHeader("Statistik für Set: " + stats.getSetName());
        printBaseStatistics(stats);

        printLevelDistribution(stats.getLevelDistribution());
    }

    private void printBaseStatistics(BaseStatistics stats) {
        System.out.println("Anzahl Fragen: " + stats.getTotalQuestions());
        System.out.println("Richtig beantwortet: " + stats.getTotalCorrect());
        System.out.println("Falsch beantwortet: " + stats.getTotalWrong());
        System.out.printf("Erfolgsquote: %.2f%%\n", stats.getCorrectPercentage());
        System.out.println("Noch nie beantwortet: " + stats.getNeverAnswered());
        System.out.printf("Durchschnittliches Level: %.2f\n", stats.getAverageLevel());
        printSeparator();
        System.out.println("Fällige Karten: " + stats.getDueCards());
    }

    public void showCardStatistics() {
        List<FlashcardSet> sets = storage.loadFlashcardSets();
        if (sets.isEmpty()) {
            System.out.println("Keine Lernsets vorhanden.");
            return;
        }

        MenuUtils.displayFlashcardSets(sets, "Einzelspezifische Statistik: Set wählen");
        FlashcardSet selectedSet = MenuUtils.selectFromList(sets, "Bitte wählen Sie ein Set aus (0 zum Abbrechen): ");
        if (selectedSet == null) return;

        List<Flashcard> cards = selectedSet.getFlashcards();
        if (cards.isEmpty()) {
            System.out.println("Dieses Set enthält keine Karten.");
            return;
        }

        System.out.println("\n--- Karten im Set: " + selectedSet.getName() + " ---");
        for (int i = 0; i < cards.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, cards.get(i).getQuestion());
        }

        Flashcard selectedCard = MenuUtils.selectFromList(cards, "Bitte wählen Sie eine Karte aus (0 zum Abbrechen): ");
        if (selectedCard == null) return;

        Map<String, FlashcardStatistics> statsMap = storage.loadStatistics();
        FlashcardStatistics stats = statsMap.get(selectedCard.getId());

        printHeader("Statistik für Frage");
        System.out.println("Frage: " + selectedCard.getQuestion());
        System.out.println("Typ: " + selectedCard.getQuestionType());

        if (stats != null) {
            int totalAnswers = stats.getCorrectCount() + stats.getWrongCount();
            double correctPercentage = totalAnswers > 0 ? (double) stats.getCorrectCount() / totalAnswers * 100 : 0;

            System.out.println("Richtig beantwortet: " + stats.getCorrectCount());
            System.out.println("Falsch beantwortet: " + stats.getWrongCount());
            System.out.printf("Erfolgsquote: %.2f%%\n", correctPercentage);
            System.out.println("Aktuelles Level: " + stats.getLevel());
            System.out.println("Fällig: " + (stats.isDue() ? "Ja" : "Nein"));
            if (stats.getLastCorrectAt() != null) {
                System.out.println("Zuletzt richtig: " + stats.getLastCorrectAt().format(DATE_TIME_FORMATTER));
            }
        } else {
            System.out.println("Status: Noch nie geübt");
            System.out.println("Level: 0");
            System.out.println("Fällig: Ja");
        }
        printSeparator();
    }

    public void showLastSessionResults() {
        displayResults(storage.loadSessionResults(), "Lernsessions");
    }

    public void showLastExamResults() {
        displayResults(storage.loadExamResults(), "Prüfungen");
    }

    public void showDueCardsBySet() {
        List<FlashcardSet> sets = storage.loadFlashcardSets();
        Map<String, FlashcardStatistics> statsMap = storage.loadStatistics();

        if (sets.isEmpty()) {
            System.out.println("Keine Lernsets vorhanden.");
            return;
        }

        printHeader("Fällige Karten je Set");
        int totalDue = 0;

        for (FlashcardSet set : sets) {
            int dueInSet = 0;
            for (Flashcard card : set.getFlashcards()) {
                FlashcardStatistics stats = statsMap.get(card.getId());
                boolean due = (stats == null) || stats.isDue();
                if (due) {
                    dueInSet++;
                }
            }
            totalDue += dueInSet;
            int totalInSet = set.getFlashcards().size();
            double ratio = totalInSet == 0 ? 0.0 : (double) dueInSet / totalInSet;
            System.out.printf("- %-25s %3d/%-3d fällig  %s%n",
                    truncate(set.getName(), 25),
                    dueInSet,
                    totalInSet,
                    renderProgressBar(1.0 - ratio, 20));
        }

        printSeparator();
        System.out.println("Gesamt fällige Karten: " + totalDue);
    }

    public void showHardestCards() {
        List<FlashcardSet> sets = storage.loadFlashcardSets();
        Map<String, FlashcardStatistics> statsMap = storage.loadStatistics();

        if (sets.isEmpty()) {
            System.out.println("Keine Lernsets vorhanden.");
            return;
        }

        List<CardDifficultyEntry> entries = new ArrayList<>();
        for (FlashcardSet set : sets) {
            for (Flashcard card : set.getFlashcards()) {
                FlashcardStatistics stats = statsMap.get(card.getId());
                if (stats == null) {
                    continue;
                }
                int attempts = stats.getCorrectCount() + stats.getWrongCount();
                if (attempts < 2) {
                    continue;
                }
                double wrongRate = (double) stats.getWrongCount() / attempts;
                entries.add(new CardDifficultyEntry(set.getName(), card.getQuestion(), attempts, wrongRate, stats.getLevel()));
            }
        }

        if (entries.isEmpty()) {
            System.out.println("Noch nicht genug Lernhistorie für eine Auswertung (mind. 2 Versuche pro Karte).");
            return;
        }

        entries.sort(Comparator
                .comparingDouble(CardDifficultyEntry::wrongRate).reversed()
                .thenComparingInt(CardDifficultyEntry::attempts).reversed());

        printHeader("Schwierigste Karten (Top 5)");
        int limit = Math.min(5, entries.size());
        for (int i = 0; i < limit; i++) {
            CardDifficultyEntry e = entries.get(i);
            System.out.printf("%d. %-20s | %-40s | Fehlerquote: %5.1f%% | Versuche: %2d | Level: %d%n",
                    (i + 1),
                    truncate(e.setName(), 20),
                    truncate(e.question(), 40),
                    e.wrongRate() * 100,
                    e.attempts(),
                    e.level());
        }
    }

    public void showWeeklyLearningReport() {
        List<SessionResult> sessions = storage.loadSessionResults();
        List<SessionResult> exams = storage.loadExamResults();

        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6);

        List<SessionResult> weeklySessions = filterByDateRange(sessions, start, today);
        List<SessionResult> weeklyExams = filterByDateRange(exams, start, today);

        printHeader("Wochenbericht (" + start + " bis " + today + ")");
        if (weeklySessions.isEmpty() && weeklyExams.isEmpty()) {
            System.out.println("Keine Lernaktivität in den letzten 7 Tagen.");
            return;
        }

        int sessionCount = weeklySessions.size();
        int examCount = weeklyExams.size();
        int totalRuns = sessionCount + examCount;

        int totalQuestions = sumQuestions(weeklySessions) + sumQuestions(weeklyExams);
        int totalCorrect = sumCorrect(weeklySessions) + sumCorrect(weeklyExams);
        long totalDuration = sumDuration(weeklySessions) + sumDuration(weeklyExams);

        double successRate = totalQuestions > 0 ? (double) totalCorrect / totalQuestions * 100.0 : 0.0;
        double averageGrade = calculateAverageGrade(weeklySessions, weeklyExams);

        System.out.println("Aktivitäten gesamt: " + totalRuns + " (Sessions: " + sessionCount + ", Prüfungen: " + examCount + ")");
        System.out.println("Beantwortete Fragen: " + totalQuestions);
        System.out.println("Richtige Antworten: " + totalCorrect);
        System.out.printf("Erfolgsquote Woche: %.2f%%%n", successRate);
        if (!Double.isNaN(averageGrade)) {
            System.out.printf("Durchschnittsnote Woche: %.1f%n", averageGrade);
        }
        if (totalDuration > 0) {
            System.out.println("Gesamte Lernzeit: " + FlashcardSessionStatistics.formatTime(totalDuration));
        }

        LocalDate strongestDay = findMostActiveDay(weeklySessions, weeklyExams);
        if (strongestDay != null) {
            int dailyActivities = countActivitiesOnDay(weeklySessions, weeklyExams, strongestDay);
            System.out.println("Aktivster Tag: " + strongestDay + " (" + dailyActivities + " Aktivitäten)");
        }
    }

    public void showSessionTrendAnalysis() {
        List<SessionResult> sessions = storage.loadSessionResults();
        if (sessions.isEmpty()) {
            System.out.println("Noch keine Lernsessions gespeichert.");
            return;
        }

        int window = Math.min(5, sessions.size());
        List<SessionResult> recent = sessions.subList(sessions.size() - window, sessions.size());

        printHeader("Trendanalyse der letzten " + window + " Sessions");

        double previousRate = -1.0;
        for (int i = 0; i < recent.size(); i++) {
            SessionResult r = recent.get(i);
            double rate = r.getPercentage();
            double grade = FlashcardSessionStatistics.calculateGrade(rate);

            String trend = "neu";
            if (previousRate >= 0.0) {
                double delta = rate - previousRate;
                if (delta > 3.0) {
                    trend = "steigend";
                } else if (delta < -3.0) {
                    trend = "fallend";
                } else {
                    trend = "stabil";
                }
            }

            System.out.printf("%d) %-20s | %5.1f%% | Note %.1f | %s%n",
                    (i + 1),
                    truncate(r.getSessionName(), 20),
                    rate,
                    grade,
                    trend);
            previousRate = rate;
        }

        double first = recent.getFirst().getPercentage();
        double last = recent.getLast().getPercentage();
        double overallDelta = last - first;

        printSeparator();
        System.out.printf("Gesamttrend: %+.1f Prozentpunkte%n", overallDelta);
        if (overallDelta > 5.0) {
            System.out.println("Interpretation: Deutliche Verbesserung.");
        } else if (overallDelta < -5.0) {
            System.out.println("Interpretation: Leistung rückläufig - gezielte Wiederholung empfohlen.");
        } else {
            System.out.println("Interpretation: Konstantes Leistungsniveau.");
        }
    }

    private List<SessionResult> filterByDateRange(List<SessionResult> input, LocalDate from, LocalDate to) {
        if (input == null || input.isEmpty()) {
            return Collections.emptyList();
        }
        List<SessionResult> filtered = new ArrayList<>();
        for (SessionResult result : input) {
            if (result == null || result.getTimestamp() == null) continue;
            LocalDate date = result.getTimestamp().toLocalDate();
            if ((date.isEqual(from) || date.isAfter(from)) && (date.isEqual(to) || date.isBefore(to))) {
                filtered.add(result);
            }
        }
        return filtered;
    }

    private int sumQuestions(List<SessionResult> results) {
        int sum = 0;
        for (SessionResult r : results) {
            if (r != null) {
                sum += r.getTotalCount();
            }
        }
        return sum;
    }

    private int sumCorrect(List<SessionResult> results) {
        int sum = 0;
        for (SessionResult r : results) {
            if (r != null) {
                sum += r.getCorrectCount();
            }
        }
        return sum;
    }

    private long sumDuration(List<SessionResult> results) {
        long sum = 0L;
        for (SessionResult r : results) {
            if (r != null) {
                sum += r.getDurationMillis();
            }
        }
        return sum;
    }

    private double calculateAverageGrade(List<SessionResult> sessions, List<SessionResult> exams) {
        List<SessionResult> all = new ArrayList<>(sessions.size() + exams.size());
        all.addAll(sessions);
        all.addAll(exams);
        if (all.isEmpty()) {
            return Double.NaN;
        }
        double sum = 0.0;
        int count = 0;
        for (SessionResult r : all) {
            if (r == null || r.getTotalCount() <= 0) continue;
            sum += FlashcardSessionStatistics.calculateGrade(r.getPercentage());
            count++;
        }
        if (count == 0) {
            return Double.NaN;
        }
        return Math.round((sum / count) * 10.0) / 10.0;
    }

    private LocalDate findMostActiveDay(List<SessionResult> sessions, List<SessionResult> exams) {
        Map<LocalDate, Integer> countByDate = new HashMap<>();
        registerDayCounts(sessions, countByDate);
        registerDayCounts(exams, countByDate);
        if (countByDate.isEmpty()) {
            return null;
        }

        LocalDate best = null;
        int bestCount = -1;
        for (Map.Entry<LocalDate, Integer> e : countByDate.entrySet()) {
            if (e.getValue() > bestCount || (e.getValue() == bestCount && (best == null || e.getKey().isAfter(best)))) {
                best = e.getKey();
                bestCount = e.getValue();
            }
        }
        return best;
    }

    private void registerDayCounts(List<SessionResult> results, Map<LocalDate, Integer> countByDate) {
        for (SessionResult r : results) {
            if (r == null || r.getTimestamp() == null) continue;
            LocalDate d = r.getTimestamp().toLocalDate();
            countByDate.put(d, countByDate.getOrDefault(d, 0) + 1);
        }
    }

    private int countActivitiesOnDay(List<SessionResult> sessions, List<SessionResult> exams, LocalDate day) {
        int count = 0;
        for (SessionResult r : sessions) {
            if (r != null && r.getTimestamp() != null && day.equals(r.getTimestamp().toLocalDate())) {
                count++;
            }
        }
        for (SessionResult r : exams) {
            if (r != null && r.getTimestamp() != null && day.equals(r.getTimestamp().toLocalDate())) {
                count++;
            }
        }
        return count;
    }

    private void displayResults(List<SessionResult> results, String type) {
        if (results.isEmpty()) {
            System.out.println("Noch keine " + type + " gespeichert.");
            return;
        }

        int limit = 3;
        int totalResults = results.size();
        int count = Math.min(limit, totalResults);

        printHeader("Ergebnisse der " + type + " (Top " + count + ")");

        for (int i = 0; i < count; i++) {
            // Zeige die neuesten Ergebnisse zuerst (von hinten in der Liste)
            SessionResult result = results.get(totalResults - 1 - i);
            printSessionResult(i + 1, result);
        }
    }

    private void printSessionResult(int index, SessionResult result) {
        System.out.printf("%d. %s (%s)\n", index, result.getSessionName(), result.getTimestamp().format(DATE_TIME_FORMATTER));
        System.out.printf("   Ergebnis: %d von %d richtig (%.2f%%)\n",
                result.getCorrectCount(), result.getTotalCount(), result.getPercentage());

        if (result.getTotalCount() > 0) {
            System.out.println("   Erreichte Note: " + FlashcardSessionStatistics.calculateGrade(result.getPercentage()));
        }
        if (result.getDurationMillis() > 0) {
            System.out.println("   Benötigte Zeit: " + FlashcardSessionStatistics.formatTime(result.getDurationMillis()));
        }
        System.out.println("------------------------------------------------");
    }

    private void printHeader(String title) {
        System.out.println("\n--- " + title + " ---");
    }

    private void printSeparator() {
        System.out.println("-----------------------");
    }

    private void printLevelDistribution(int[] levelDistribution) {
        System.out.println("\nLevel-Verteilung:");
        for (int i = 0; i <= 6; i++) {
            int count = levelDistribution[i];
            System.out.printf("  Level %d: %3d Karten  %s%n", i, count, renderProgressBar(normalizeLevelCount(levelDistribution, count), 15));
        }
    }

    private double normalizeLevelCount(int[] levelDistribution, int count) {
        int max = 0;
        for (int value : levelDistribution) {
            if (value > max) {
                max = value;
            }
        }
        if (max == 0) {
            return 0.0;
        }
        return (double) count / max;
    }

    private String renderProgressBar(double ratio, int width) {
        double clamped = Math.max(0.0, Math.min(1.0, ratio));
        int filled = (int) Math.round(clamped * width);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < width; i++) {
            sb.append(i < filled ? '#' : '-');
        }
        sb.append(']');
        return sb.toString();
    }

    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    private record CardDifficultyEntry(String setName, String question, int attempts, double wrongRate, int level) {
    }
}
