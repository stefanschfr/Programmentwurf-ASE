package de.sagaweschaefer.flashcard.application.usecase;

import de.sagaweschaefer.flashcard.testdoubles.FakeFlashcardSetRepository;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateFlashcardSetUseCaseTest {

    @Test
    void execute_createsAndSavesSet() {
        FakeFlashcardSetRepository repo = new FakeFlashcardSetRepository();
        CreateFlashcardSetUseCase useCase = new CreateFlashcardSetUseCase(repo);

        FlashcardSet result = useCase.execute("Mathe");

        assertEquals("Mathe", result.getName());
        assertEquals(1, repo.getStoredCount());
    }

    @Test
    void execute_multipleCreations() {
        FakeFlashcardSetRepository repo = new FakeFlashcardSetRepository();
        CreateFlashcardSetUseCase useCase = new CreateFlashcardSetUseCase(repo);

        useCase.execute("Mathe");
        useCase.execute("Deutsch");

        assertEquals(2, repo.getStoredCount());
    }
}

