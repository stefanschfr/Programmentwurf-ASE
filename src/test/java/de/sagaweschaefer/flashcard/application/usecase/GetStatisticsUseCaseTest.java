package de.sagaweschaefer.flashcard.application.usecase;

import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardSetStatistics;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.model.GeneralStatistics;
import de.sagaweschaefer.flashcard.testdoubles.FakeFlashcardSetRepository;
import de.sagaweschaefer.flashcard.testdoubles.MockStatisticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetStatisticsUseCaseTest {

    private GetStatisticsUseCase useCase;

    @BeforeEach
    void setUp() {
        FakeFlashcardSetRepository setRepo = new FakeFlashcardSetRepository();
        MockStatisticsRepository statsRepo = new MockStatisticsRepository();

        FlashcardSet set = new FlashcardSet("Geographie");
        Flashcard c1 = new Flashcard("Was ist die Hauptstadt von Frankreich?", "Paris");
        set.getFlashcards().add(c1);
        setRepo.saveAll(List.of(set));

        FlashcardStatistics s = new FlashcardStatistics(c1.getId());
        s.incrementCorrect();
        s.incrementCorrect();
        s.incrementWrong();
        statsRepo.seedStatistic(c1.getId(), s);

        useCase = new GetStatisticsUseCase(setRepo, statsRepo);
    }

    @Test
    void getGeneralStatistics_returnsCorrectCounts() {
        GeneralStatistics stats = useCase.getGeneralStatistics();
        assertEquals(1, stats.getTotalQuestions());
        assertEquals(2, stats.getTotalCorrect());
        assertEquals(1, stats.getTotalWrong());
    }

    @Test
    void getSetStatistics_validIndex_returnsSetName() {
        FlashcardSetStatistics stats = useCase.getSetStatistics(0);
        assertEquals("Geographie", stats.getSetName());
    }

    @Test
    void getSetStatistics_invalidIndex_throws() {
        assertThrows(IndexOutOfBoundsException.class, () -> useCase.getSetStatistics(5));
    }
}

