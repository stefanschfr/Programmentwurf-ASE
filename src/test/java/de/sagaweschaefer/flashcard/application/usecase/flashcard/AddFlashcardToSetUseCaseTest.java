package de.sagaweschaefer.flashcard.application.usecase.flashcard;

import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.support.InMemoryFlashcardSetRepository;
import de.sagaweschaefer.flashcard.support.TestDataFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AddFlashcardToSetUseCaseTest {
    @Test
    void addsFlashcardToManagedSetAndSynchronizesSelectedSet() {
        Flashcard card = TestDataFactory.freeTextCard("Was ist Java?", "Eine Sprache");
        FlashcardSet managedSet = TestDataFactory.flashcardSet("Programmierung");
        FlashcardSet selectedSet = TestDataFactory.flashcardSet("  programmierung  ");
        InMemoryFlashcardSetRepository repository = new InMemoryFlashcardSetRepository(List.of(managedSet));
        AddFlashcardToSetUseCase useCase = new AddFlashcardToSetUseCase(repository);

        useCase.execute(selectedSet, card);

        assertEquals(1, repository.findAll().getFirst().getFlashcards().size());
        assertEquals(1, selectedSet.getFlashcards().size());
    }

    @Test
    void rejectsFlashcardWithoutQuestion() {
        FlashcardSet managedSet = TestDataFactory.flashcardSet("Programmierung");
        InMemoryFlashcardSetRepository repository = new InMemoryFlashcardSetRepository(List.of(managedSet));
        AddFlashcardToSetUseCase useCase = new AddFlashcardToSetUseCase(repository);
        Flashcard invalidCard = new Flashcard();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> useCase.execute(managedSet, invalidCard));

        assertEquals("Die Frage der Lernkarte darf nicht leer sein.", exception.getMessage());
    }
}

