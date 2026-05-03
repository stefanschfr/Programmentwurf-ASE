package de.sagaweschaefer.flashcard.application.usecase;

import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.testdoubles.FakeFlashcardSetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteFlashcardSetUseCaseTest {

    private FakeFlashcardSetRepository repo;
    private DeleteFlashcardSetUseCase useCase;

    @BeforeEach
    void setUp() {
        repo = new FakeFlashcardSetRepository();
        repo.saveAll(List.of(
                new FlashcardSet("Mathe"),
                new FlashcardSet("Englisch"),
                new FlashcardSet("Physik")
        ));
        useCase = new DeleteFlashcardSetUseCase(repo);
    }

    @Test
    void execute_removesSetAtIndex() {
        String name = useCase.execute(1);
        assertEquals("Englisch", name);
        assertEquals(2, repo.findAll().size());
        assertEquals("Mathe", repo.findAll().get(0).getName());
        assertEquals("Physik", repo.findAll().get(1).getName());
    }

    @Test
    void execute_invalidIndex_throwsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> useCase.execute(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> useCase.execute(5));
    }
}

