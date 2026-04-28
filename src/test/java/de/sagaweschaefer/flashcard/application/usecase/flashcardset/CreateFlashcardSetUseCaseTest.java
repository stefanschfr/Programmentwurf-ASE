package de.sagaweschaefer.flashcard.application.usecase.flashcardset;

import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.support.InMemoryFlashcardSetRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateFlashcardSetUseCaseTest {
    @Test
    void createsFlashcardSetWithTrimmedName() {
        InMemoryFlashcardSetRepository repository = new InMemoryFlashcardSetRepository();
        CreateFlashcardSetUseCase useCase = new CreateFlashcardSetUseCase(repository);

        FlashcardSet created = useCase.execute("  Java Basics  ");

        assertEquals("Java Basics", created.getName());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void rejectsBlankName() {
        InMemoryFlashcardSetRepository repository = new InMemoryFlashcardSetRepository();
        CreateFlashcardSetUseCase useCase = new CreateFlashcardSetUseCase(repository);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> useCase.execute("   "));

        assertEquals("Der Name des Lernkartensets darf nicht leer sein.", exception.getMessage());
    }

    @Test
    void rejectsDuplicateNameIgnoringCase() {
        InMemoryFlashcardSetRepository repository = new InMemoryFlashcardSetRepository(List.of(new FlashcardSet("Java")));
        CreateFlashcardSetUseCase useCase = new CreateFlashcardSetUseCase(repository);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> useCase.execute("java"));

        assertEquals("Ein Lernkartenset mit diesem Namen existiert bereits.", exception.getMessage());
    }
}

