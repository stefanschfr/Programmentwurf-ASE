package de.sagaweschaefer.flashcard.application.usecase.session;

import de.sagaweschaefer.flashcard.application.session.PreparedSession;
import de.sagaweschaefer.flashcard.application.session.SessionMode;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrepareSessionUseCaseTest {
    @Test
    void filtersOutFullyLearnedCardsInSetLearningSession() {
        Flashcard dueCard = TestDataFactory.freeTextCard("Frage 1", "A1");
        Flashcard learnedCard = TestDataFactory.freeTextCard("Frage 2", "A2");
        FlashcardSet set = TestDataFactory.flashcardSet("Set", dueCard, learnedCard);
        FlashcardStatistics learnedStatistics = TestDataFactory.statistics(learnedCard.getId(), 6, 5, 0, LocalDateTime.now().minusDays(2));
        InMemoryFlashcardSetRepository setRepository = new InMemoryFlashcardSetRepository(List.of(set));
        InMemoryFlashcardStatisticsRepository statisticsRepository = new InMemoryFlashcardStatisticsRepository(Map.of(learnedCard.getId(), learnedStatistics));
        PrepareSessionUseCase useCase = new PrepareSessionUseCase(setRepository, statisticsRepository);

        PreparedSession result = useCase.prepareSetLearningSession(set);

        assertEquals(SessionMode.SET_LEARNING, result.getMode());
        assertEquals(1, result.getFlashcards().size());
        assertEquals(dueCard.getQuestion(), result.getFlashcards().getFirst().getQuestion());
    }

    @Test
    void collectsWrongAnswerCardsFromAllSets() {
        Flashcard wrongCard = TestDataFactory.freeTextCard("Falsch", "A");
        Flashcard goodCard = TestDataFactory.freeTextCard("Gut", "B");
        FlashcardSet firstSet = TestDataFactory.flashcardSet("Set 1", wrongCard);
        FlashcardSet secondSet = TestDataFactory.flashcardSet("Set 2", goodCard);
        FlashcardStatistics wrongStatistics = TestDataFactory.statistics(wrongCard.getId(), 0, 0, 2, null);
        FlashcardStatistics goodStatistics = TestDataFactory.statistics(goodCard.getId(), 2, 4, 0, LocalDateTime.now());
        InMemoryFlashcardSetRepository setRepository = new InMemoryFlashcardSetRepository(List.of(firstSet, secondSet));
        InMemoryFlashcardStatisticsRepository statisticsRepository = new InMemoryFlashcardStatisticsRepository(Map.of(
                wrongCard.getId(), wrongStatistics,
                goodCard.getId(), goodStatistics
        ));
        PrepareSessionUseCase useCase = new PrepareSessionUseCase(setRepository, statisticsRepository);

        PreparedSession result = useCase.prepareWrongAnswersSession();

        assertEquals(SessionMode.WRONG_ANSWERS, result.getMode());
        assertEquals(1, result.getFlashcards().size());
        assertEquals(wrongCard.getQuestion(), result.getFlashcards().getFirst().getQuestion());
    }

    @Test
    void collectsDueAndUntrackedCards() {
        Flashcard untrackedCard = TestDataFactory.freeTextCard("Neu", "A");
        Flashcard dueCard = TestDataFactory.freeTextCard("Fällig", "B");
        Flashcard notDueCard = TestDataFactory.freeTextCard("Nicht fällig", "C");
        FlashcardSet set = TestDataFactory.flashcardSet("Set", untrackedCard, dueCard, notDueCard);
        FlashcardStatistics dueStatistics = TestDataFactory.statistics(dueCard.getId(), 1, 1, 0, LocalDateTime.now().minusMinutes(5));
        FlashcardStatistics notDueStatistics = TestDataFactory.statistics(notDueCard.getId(), 6, 3, 0, LocalDateTime.now());
        InMemoryFlashcardSetRepository setRepository = new InMemoryFlashcardSetRepository(List.of(set));
        InMemoryFlashcardStatisticsRepository statisticsRepository = new InMemoryFlashcardStatisticsRepository(Map.of(
                dueCard.getId(), dueStatistics,
                notDueCard.getId(), notDueStatistics
        ));
        PrepareSessionUseCase useCase = new PrepareSessionUseCase(setRepository, statisticsRepository);

        PreparedSession result = useCase.prepareDueCardsSession();

        assertEquals(SessionMode.DUE_CARDS, result.getMode());
        assertEquals(2, result.getFlashcards().size());
        assertTrue(result.getFlashcards().stream().map(Flashcard::getQuestion).toList().containsAll(List.of("Neu", "Fällig")));
    }

    @Test
    void rejectsExamWithTooFewCards() {
        FlashcardSet set = TestDataFactory.flashcardSet("Set",
                TestDataFactory.freeTextCard("1", "A"),
                TestDataFactory.freeTextCard("2", "A"),
                TestDataFactory.freeTextCard("3", "A")
        );
        InMemoryFlashcardSetRepository setRepository = new InMemoryFlashcardSetRepository(List.of(set));
        InMemoryFlashcardStatisticsRepository statisticsRepository = new InMemoryFlashcardStatisticsRepository();
        PrepareSessionUseCase useCase = new PrepareSessionUseCase(setRepository, statisticsRepository);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> useCase.prepareExamSession(set));

        assertEquals("Das gewählte Set enthält nur 3 Karten. Für den Prüfungsmodus sind mindestens 10 Karten erforderlich.", exception.getMessage());
    }
}

