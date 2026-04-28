package de.sagaweschaefer.flashcard.application.usecase.flashcard;

import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.support.InMemoryFlashcardSetRepository;
import de.sagaweschaefer.flashcard.support.InMemoryFlashcardStatisticsRepository;
import de.sagaweschaefer.flashcard.support.TestDataFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeleteFlashcardFromSetUseCaseTest {
    @Test
    void deletesFlashcardAndRemovesStatistics() {
        Flashcard card = TestDataFactory.freeTextCard("Frage", "Antwort");
        FlashcardSet managedSet = TestDataFactory.flashcardSet("Set", card);
        FlashcardSet selectedSet = TestDataFactory.flashcardSet("Set", card);
        InMemoryFlashcardSetRepository setRepository = new InMemoryFlashcardSetRepository(List.of(managedSet));
        FlashcardStatistics statistics = TestDataFactory.statistics(card.getId(), 2, 3, 1, LocalDateTime.now().minusHours(1));
        InMemoryFlashcardStatisticsRepository statisticsRepository = new InMemoryFlashcardStatisticsRepository(Map.of(card.getId(), statistics));
        DeleteFlashcardFromSetUseCase useCase = new DeleteFlashcardFromSetUseCase(setRepository, statisticsRepository);

        var result = useCase.execute(selectedSet, 0);

        assertTrue(result.isPresent());
        assertEquals(0, setRepository.findAll().getFirst().getFlashcards().size());
        assertTrue(statisticsRepository.findAll().isEmpty());
        assertTrue(selectedSet.getFlashcards().isEmpty());
    }

    @Test
    void returnsEmptyForInvalidIndex() {
        Flashcard card = TestDataFactory.freeTextCard("Frage", "Antwort");
        FlashcardSet managedSet = TestDataFactory.flashcardSet("Set", card);
        InMemoryFlashcardSetRepository setRepository = new InMemoryFlashcardSetRepository(List.of(managedSet));
        InMemoryFlashcardStatisticsRepository statisticsRepository = new InMemoryFlashcardStatisticsRepository();
        DeleteFlashcardFromSetUseCase useCase = new DeleteFlashcardFromSetUseCase(setRepository, statisticsRepository);

        var result = useCase.execute(managedSet, 5);

        assertTrue(result.isEmpty());
        assertEquals(1, setRepository.findAll().getFirst().getFlashcards().size());
    }
}

