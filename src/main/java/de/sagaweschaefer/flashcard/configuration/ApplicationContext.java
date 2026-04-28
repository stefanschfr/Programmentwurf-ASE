package de.sagaweschaefer.flashcard.configuration;

import de.sagaweschaefer.flashcard.application.port.ExamSessionResultRepository;
import de.sagaweschaefer.flashcard.application.port.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.application.port.FlashcardStatisticsRepository;
import de.sagaweschaefer.flashcard.application.port.LearningSessionResultRepository;
import de.sagaweschaefer.flashcard.application.usecase.flashcard.AddFlashcardToSetUseCase;
import de.sagaweschaefer.flashcard.application.usecase.flashcard.DeleteFlashcardFromSetUseCase;
import de.sagaweschaefer.flashcard.application.usecase.flashcard.ListFlashcardsInSetUseCase;
import de.sagaweschaefer.flashcard.application.usecase.flashcardset.CreateFlashcardSetUseCase;
import de.sagaweschaefer.flashcard.application.usecase.flashcardset.DeleteFlashcardSetUseCase;
import de.sagaweschaefer.flashcard.application.usecase.flashcardset.ListFlashcardSetsUseCase;
import de.sagaweschaefer.flashcard.application.usecase.session.PrepareSessionUseCase;
import de.sagaweschaefer.flashcard.application.usecase.session.RunExamSessionUseCase;
import de.sagaweschaefer.flashcard.application.usecase.session.RunLearningSessionUseCase;
import de.sagaweschaefer.flashcard.infrastructure.persistence.JsonExamSessionResultRepository;
import de.sagaweschaefer.flashcard.infrastructure.persistence.JsonFlashcardSetRepository;
import de.sagaweschaefer.flashcard.infrastructure.persistence.JsonFlashcardStatisticsRepository;
import de.sagaweschaefer.flashcard.infrastructure.persistence.JsonLearningSessionResultRepository;
import de.sagaweschaefer.flashcard.menu.flashcardsession.ConsoleSessionInteraction;
import de.sagaweschaefer.flashcard.menu.flashcardsession.FlashcardSessionMenu;
import de.sagaweschaefer.flashcard.menu.flashcardsession.FlashcardSessionMenuHelper;
import de.sagaweschaefer.flashcard.menu.flashcardsetmanager.FlashcardSetManagerMenu;
import de.sagaweschaefer.flashcard.menu.flashcardsetmanager.FlashcardSetManagerMenuHelper;
import de.sagaweschaefer.flashcard.menu.statistics.StatisticsMenu;
import de.sagaweschaefer.flashcard.util.JsonStorage;

public class ApplicationContext {
    private final JsonStorage storage;
    private final FlashcardSetRepository flashcardSetRepository;
    private final FlashcardStatisticsRepository flashcardStatisticsRepository;
    private final LearningSessionResultRepository learningSessionResultRepository;
    private final ExamSessionResultRepository examSessionResultRepository;

    public ApplicationContext() {
        this.storage = new JsonStorage();
        this.flashcardSetRepository = new JsonFlashcardSetRepository(storage);
        this.flashcardStatisticsRepository = new JsonFlashcardStatisticsRepository(storage);
        this.learningSessionResultRepository = new JsonLearningSessionResultRepository(storage);
        this.examSessionResultRepository = new JsonExamSessionResultRepository(storage);
    }

    public FlashcardSetManagerMenu createFlashcardSetManagerMenu() {
        CreateFlashcardSetUseCase createFlashcardSetUseCase = new CreateFlashcardSetUseCase(flashcardSetRepository);
        ListFlashcardSetsUseCase listFlashcardSetsUseCase = new ListFlashcardSetsUseCase(flashcardSetRepository);
        DeleteFlashcardSetUseCase deleteFlashcardSetUseCase = new DeleteFlashcardSetUseCase(
                flashcardSetRepository,
                flashcardStatisticsRepository
        );
        AddFlashcardToSetUseCase addFlashcardToSetUseCase = new AddFlashcardToSetUseCase(flashcardSetRepository);
        ListFlashcardsInSetUseCase listFlashcardsInSetUseCase = new ListFlashcardsInSetUseCase(flashcardSetRepository);
        DeleteFlashcardFromSetUseCase deleteFlashcardFromSetUseCase = new DeleteFlashcardFromSetUseCase(
                flashcardSetRepository,
                flashcardStatisticsRepository
        );

        FlashcardSetManagerMenuHelper helper = new FlashcardSetManagerMenuHelper(
                createFlashcardSetUseCase,
                listFlashcardSetsUseCase,
                deleteFlashcardSetUseCase,
                addFlashcardToSetUseCase,
                listFlashcardsInSetUseCase,
                deleteFlashcardFromSetUseCase,
                flashcardStatisticsRepository
        );

        return new FlashcardSetManagerMenu(helper);
    }

    public FlashcardSessionMenu createFlashcardSessionMenu() {
        ListFlashcardSetsUseCase listFlashcardSetsUseCase = new ListFlashcardSetsUseCase(flashcardSetRepository);
        PrepareSessionUseCase prepareSessionUseCase = new PrepareSessionUseCase(
                flashcardSetRepository,
                flashcardStatisticsRepository
        );
        ConsoleSessionInteraction sessionInteraction = new ConsoleSessionInteraction();
        RunLearningSessionUseCase runLearningSessionUseCase = new RunLearningSessionUseCase(
                flashcardStatisticsRepository,
                learningSessionResultRepository,
                sessionInteraction
        );
        RunExamSessionUseCase runExamSessionUseCase = new RunExamSessionUseCase(
                flashcardStatisticsRepository,
                examSessionResultRepository,
                sessionInteraction
        );

        FlashcardSessionMenuHelper helper = new FlashcardSessionMenuHelper(
                listFlashcardSetsUseCase,
                prepareSessionUseCase,
                runLearningSessionUseCase,
                runExamSessionUseCase
        );
        return new FlashcardSessionMenu(helper);
    }

    public StatisticsMenu createStatisticsMenu() {
        return new StatisticsMenu(storage);
    }
}

