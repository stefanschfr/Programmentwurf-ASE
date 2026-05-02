package de.sagaweschaefer.flashcard.domain.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeCalculatorTest {

    private final GradeCalculator calculator = new GradeCalculator();

    @Test
    void grade_atOrAbove95Percent_isOne() {
        assertEquals(1.0, calculator.calculateGrade(95.0));
        assertEquals(1.0, calculator.calculateGrade(100.0));
        assertEquals(1.0, calculator.calculateGrade(110.0)); // außerhalb -> Top
    }

    @Test
    void grade_atOrBelowZero_isSix() {
        assertEquals(6.0, calculator.calculateGrade(0.0));
        assertEquals(6.0, calculator.calculateGrade(-10.0));
    }

    @Test
    void grade_at50Percent_isFour() {
        assertEquals(4.0, calculator.calculateGrade(50.0));
    }

    @Test
    void grade_at30Percent_isFive() {
        assertEquals(5.0, calculator.calculateGrade(30.0));
    }

    @Test
    void grade_betweenPassAndBest_isLinear() {
        // Mitte zwischen 50 % (4.0) und 95 % (1.0) -> 72.5 % -> 2.5
        assertEquals(2.5, calculator.calculateGrade(72.5));
    }

    @Test
    void grade_betweenFailAndPass_isLinear() {
        // Mitte zwischen 30 % (5.0) und 50 % (4.0) -> 40 % -> 4.5
        assertEquals(4.5, calculator.calculateGrade(40.0));
    }

    @Test
    void grade_betweenZeroAndFail_isLinear() {
        // Mitte zwischen 0 % (6.0) und 30 % (5.0) -> 15 % -> 5.5
        assertEquals(5.5, calculator.calculateGrade(15.0));
    }

    @Test
    void grade_isRoundedToOneDecimal() {
        double grade = calculator.calculateGrade(73.0);
        // genau eine Nachkommastelle -> grade*10 ist ganzzahlig
        assertEquals(grade * 10.0, Math.round(grade * 10.0), 1e-9);
    }

    // --- correctCount/totalCount-Variante ---

    @Test
    void grade_byCounts_zeroTotal_returnsWorst() {
        assertEquals(GradeCalculator.WORST_GRADE, calculator.calculateGrade(0, 0));
    }

    @Test
    void grade_byCounts_allCorrect_returnsBest() {
        assertEquals(GradeCalculator.BEST_GRADE, calculator.calculateGrade(10, 10));
    }

    @Test
    void grade_byCounts_halfCorrect_returnsFour() {
        assertEquals(4.0, calculator.calculateGrade(5, 10));
    }

    // --- isPassing ---

    @Test
    void isPassing_atFiftyPercent_true() {
        assertTrue(calculator.isPassing(50.0));
    }

    @Test
    void isPassing_belowFiftyPercent_false() {
        // 45 % -> Note 4.3 (gerundet) -> nicht bestanden
        assertFalse(calculator.isPassing(45.0));
    }

    // --- Konsistenz mit altem Verhalten von FlashcardSessionStatistics ---

    @Test
    void grade_isBackwardCompatible_with_oldFormula() {
        // Stichproben aus der ursprünglichen Implementierung
        assertEquals(1.0, calculator.calculateGrade(100));
        assertEquals(4.0, calculator.calculateGrade(50));
        assertEquals(6.0, calculator.calculateGrade(0));
    }
}


