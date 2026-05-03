package de.sagaweschaefer.flashcard.application.usecase;

import de.sagaweschaefer.flashcard.testdoubles.FakeFlashcardSetRepository;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddFlashcardToSetUseCaseTest {

    @Test
    void execute_addsFlashcardToExistingSet() {
        FakeFlashcardSetRepository repo = new FakeFlashcardSetRepository();
        FlashcardSet set = new FlashcardSet("Mathe");
        repo.saveAll(List.of(set));

        AddFlashcardToSetUseCase useCase = new AddFlashcardToSetUseCase(repo);
        Flashcard card = new Flashcard("2+2?", "4");
        useCase.execute("Mathe", card);

        List<FlashcardSet> saved = repo.findAll();
        assertEquals(1, saved.getFirst().getFlashcards().size());
    }

    @Test
    void execute_throwsExceptionForUnknownSet() {
        FakeFlashcardSetRepository repo = new FakeFlashcardSetRepository();
        AddFlashcardToSetUseCase useCase = new AddFlashcardToSetUseCase(repo);

        assertThrows(IllegalArgumentException.class,
                () -> useCase.execute("NichtExistent", new Flashcard("?", "!")));
    }
}

