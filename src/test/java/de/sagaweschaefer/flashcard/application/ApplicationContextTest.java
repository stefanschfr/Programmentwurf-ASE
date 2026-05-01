package de.sagaweschaefer.flashcard.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextTest {

    @Test
    void allRepositoriesAreInitialized() {
        ApplicationContext context = new ApplicationContext();
        assertNotNull(context.getFlashcardSetRepository());
        assertNotNull(context.getStatisticsRepository());
        assertNotNull(context.getSessionResultRepository());
        assertNotNull(context.getExamResultRepository());
    }
}

