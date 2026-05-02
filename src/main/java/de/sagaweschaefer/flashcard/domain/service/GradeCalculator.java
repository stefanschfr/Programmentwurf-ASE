package de.sagaweschaefer.flashcard.domain.service;

/**
 * Domain-Service zur Berechnung von Schulnoten aus Prozentwerten.
 *
 * <p>Reine Domänenlogik ohne Abhängigkeiten zu Persistenz oder UI. Die
 * Klasse kapselt die deutsche Notenskala (1.0 – 6.0) und definiert die
 * stückweise lineare Abbildung von Prozent auf Note:</p>
 * <ul>
 *     <li>&ge; 95&nbsp;%  &rarr; 1.0</li>
 *     <li>50&nbsp;%–95&nbsp;% &rarr; 4.0 → 1.0 (linear)</li>
 *     <li>30&nbsp;%–50&nbsp;% &rarr; 5.0 → 4.0 (linear)</li>
 *     <li>0&nbsp;%–30&nbsp;%  &rarr; 6.0 → 5.0 (linear)</li>
 *     <li>&le; 0&nbsp;%   &rarr; 6.0</li>
 * </ul>
 *
 * <p>Die Notenstufen werden auf eine Nachkommastelle gerundet, um die in
 * deutschen Zeugnissen üblichen Dezimalnoten (z.&nbsp;B. 2.3) zu erzeugen.</p>
 *
 * <p>Diese Klasse ist gezielt aus {@code FlashcardSessionStatistics}
 * extrahiert worden (DRY-Refactor): die Notenberechnung wurde dort inline
 * geführt und sollte als reine Domain-Regel an genau einer Stelle leben.</p>
 */
@SuppressWarnings("unused") // Public Domain-API: wird aus FlashcardSessionStatistics, UseCases und Tests genutzt
public class GradeCalculator {

    /** Beste Note. */
    public static final double BEST_GRADE = 1.0;

    /** Schlechteste Note. */
    public static final double WORST_GRADE = 6.0;

    /** Prozent-Schwelle für Note 1.0. */
    private static final double THRESHOLD_BEST = 95.0;

    /** Prozent-Schwelle für Note 4.0 (knapp bestanden). */
    private static final double THRESHOLD_PASS = 50.0;

    /** Prozent-Schwelle für Note 5.0. */
    private static final double THRESHOLD_FAIL_LOWER = 30.0;

    /**
     * Berechnet die Note für einen gegebenen Prozentwert.
     *
     * @param percentage Prozentwert im Bereich 0–100 (Werte außerhalb werden
     *                   auf den Bereich geclamped)
     * @return Note auf eine Nachkommastelle gerundet
     */
    public double calculateGrade(double percentage) {
        if (percentage >= THRESHOLD_BEST) {
            return BEST_GRADE;
        }
        if (percentage <= 0.0) {
            return WORST_GRADE;
        }

        double grade;
        if (percentage >= THRESHOLD_PASS) {
            // 50 % -> 4.0, 95 % -> 1.0
            grade = 4.0 - (percentage - THRESHOLD_PASS) * (3.0 / (THRESHOLD_BEST - THRESHOLD_PASS));
        } else if (percentage >= THRESHOLD_FAIL_LOWER) {
            // 30 % -> 5.0, 50 % -> 4.0
            grade = 5.0 - (percentage - THRESHOLD_FAIL_LOWER) * (1.0 / (THRESHOLD_PASS - THRESHOLD_FAIL_LOWER));
        } else {
            // 0 % -> 6.0, 30 % -> 5.0
            grade = WORST_GRADE - percentage * (1.0 / THRESHOLD_FAIL_LOWER);
        }

        return roundToOneDecimal(grade);
    }

    /**
     * Berechnet die Note auf Basis von Anzahl richtiger Antworten und
     * Gesamtfragen.
     *
     * @param correctCount Anzahl richtiger Antworten
     * @param totalCount   Gesamtanzahl der Fragen ({@code 0} ergibt {@link #WORST_GRADE})
     * @return Note auf eine Nachkommastelle gerundet
     */
    public double calculateGrade(int correctCount, int totalCount) {
        if (totalCount <= 0) {
            return WORST_GRADE;
        }
        double percentage = (double) correctCount / totalCount * 100.0;
        return calculateGrade(percentage);
    }

    /**
     * Liefert {@code true}, wenn der gegebene Prozentwert mindestens für eine
     * bestandene Note (4.0) ausreicht.
     */
    public boolean isPassing(double percentage) {
        return calculateGrade(percentage) <= 4.0;
    }

    private double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}

