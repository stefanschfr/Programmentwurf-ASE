package de.sagaweschaefer.flashcard.application.usecase.flashcard;

import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.support.InMemoryFlashcardSetRepository;
import de.sagaweschaefer.flashcard.support.TestDataFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListFlashcardsInSetUseCaseTest {
    @Test
    void returnsCopyOfFlashcardsForSelectedSet() {
        Flashcard first = TestDataFactory.freeTextCard("Frage 1", "A1");
        Flashcard second = TestDataFactory.freeTextCard("Frage 2", "A2");
        FlashcardSet managedSet = TestDataFactory.flashcardSet("Set", first, second);
        FlashcardSet selectedSet = TestDataFactory.flashcardSet("Set");
        InMemoryFlashcardSetRepository repository = new InMemoryFlashcardSetRepository(List.of(managedSet));
        ListFlashcardsInSetUseCase useCase = new ListFlashcardsInSetUseCase(repository);

        List<Flashcard> result = useCase.execute(selectedSet);
        result.clear();

        assertEquals(2, repository.findAll().getFirst().getFlashcards().size());
    }
}

