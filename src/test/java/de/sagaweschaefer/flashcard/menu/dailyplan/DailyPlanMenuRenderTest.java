package de.sagaweschaefer.flashcard.menu.dailyplan;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DailyPlanMenuRenderTest {

    @Test
    void renderProgressBar_zero() {
        String bar = DailyPlanMenu.renderProgressBar(0.0);
        assertTrue(bar.startsWith("[--"));
        assertTrue(bar.endsWith("0%"));
    }

    @Test
    void renderProgressBar_full() {
        String bar = DailyPlanMenu.renderProgressBar(1.0);
        assertTrue(bar.contains("##"));
        assertFalse(bar.contains("-"));
        assertTrue(bar.endsWith("100%"));
    }

    @Test
    void renderProgressBar_clampedAboveOne() {
        String bar = DailyPlanMenu.renderProgressBar(2.0);
        assertTrue(bar.endsWith("100%"));
    }

    @Test
    void renderProgressBar_clampedBelowZero() {
        String bar = DailyPlanMenu.renderProgressBar(-1.0);
        assertTrue(bar.endsWith("0%"));
    }

    @Test
    void renderProgressBar_half() {
        String bar = DailyPlanMenu.renderProgressBar(0.5);
        assertTrue(bar.endsWith("50%"));
    }
}

