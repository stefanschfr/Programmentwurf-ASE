package de.sagaweschaefer.flashcard.menu.flashcardsession;

import de.sagaweschaefer.flashcard.application.usecase.flashcardset.ListFlashcardSetsUseCase;
import de.sagaweschaefer.flashcard.application.usecase.session.PrepareSessionUseCase;
import de.sagaweschaefer.flashcard.application.usecase.session.RunExamSessionUseCase;
import de.sagaweschaefer.flashcard.application.usecase.session.RunLearningSessionUseCase;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.List;

public class FlashcardSessionMenuHelper {
    private final ListFlashcardSetsUseCase listFlashcardSetsUseCase;
    private final PrepareSessionUseCase prepareSessionUseCase;
    private final RunLearningSessionUseCase runLearningSessionUseCase;
    private final RunExamSessionUseCase runExamSessionUseCase;

    public FlashcardSessionMenuHelper(ListFlashcardSetsUseCase listFlashcardSetsUseCase,
                                      PrepareSessionUseCase prepareSessionUseCase,
                                      RunLearningSessionUseCase runLearningSessionUseCase,
                                      RunExamSessionUseCase runExamSessionUseCase) {
        this.listFlashcardSetsUseCase = listFlashcardSetsUseCase;
        this.prepareSessionUseCase = prepareSessionUseCase;
        this.runLearningSessionUseCase = runLearningSessionUseCase;
        this.runExamSessionUseCase = runExamSessionUseCase;
    }

    public void startSession() {
        try {
            List<FlashcardSet> flashcardSets = listFlashcardSetsUseCase.execute();
            MenuUtils.displayFlashcardSets(flashcardSets, "Verfügbare Lernkartensets");
            FlashcardSet set = MenuUtils.selectFromList(flashcardSets, "Wähle ein Lernkartenset (Nummer): ");
            if (set == null) return;

            runLearningSessionUseCase.execute(prepareSessionUseCase.prepareSetLearningSession(set));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void startWrongAnswersSession() {
        try {
            runLearningSessionUseCase.execute(prepareSessionUseCase.prepareWrongAnswersSession());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void startDueCardsSession() {
        try {
            runLearningSessionUseCase.execute(prepareSessionUseCase.prepareDueCardsSession());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void startExamMode() {
        try {
            List<FlashcardSet> flashcardSets = listFlashcardSetsUseCase.execute();
            MenuUtils.displayFlashcardSets(flashcardSets, "Verfügbare Lernkartensets");
            FlashcardSet set = MenuUtils.selectFromList(flashcardSets, "Wähle ein Lernkartenset für die Prüfung (Nummer): ");
            if (set == null) return;

            runExamSessionUseCase.execute(prepareSessionUseCase.prepareExamSession(set));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
