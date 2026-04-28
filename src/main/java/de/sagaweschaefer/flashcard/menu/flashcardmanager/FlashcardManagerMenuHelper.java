package de.sagaweschaefer.flashcard.menu.flashcardmanager;

import de.sagaweschaefer.flashcard.application.port.FlashcardStatisticsRepository;
import de.sagaweschaefer.flashcard.application.usecase.flashcard.AddFlashcardToSetUseCase;
import de.sagaweschaefer.flashcard.application.usecase.flashcard.DeleteFlashcardFromSetUseCase;
import de.sagaweschaefer.flashcard.application.usecase.flashcard.ListFlashcardsInSetUseCase;
import de.sagaweschaefer.flashcard.menu.flashcardcreation.FlashcardCreationMenu;
import de.sagaweschaefer.flashcard.menu.flashcardcreation.FlashcardCreationMenuHelper;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.List;
import java.util.Map;

public class FlashcardManagerMenuHelper {
    private final FlashcardSet flashcardSet;
    private final AddFlashcardToSetUseCase addFlashcardToSetUseCase;
    private final ListFlashcardsInSetUseCase listFlashcardsInSetUseCase;
    private final DeleteFlashcardFromSetUseCase deleteFlashcardFromSetUseCase;
    private final FlashcardStatisticsRepository flashcardStatisticsRepository;

    public FlashcardManagerMenuHelper(FlashcardSet flashcardSet,
                                      AddFlashcardToSetUseCase addFlashcardToSetUseCase,
                                      ListFlashcardsInSetUseCase listFlashcardsInSetUseCase,
                                      DeleteFlashcardFromSetUseCase deleteFlashcardFromSetUseCase,
                                      FlashcardStatisticsRepository flashcardStatisticsRepository) {
        this.flashcardSet = flashcardSet;
        this.addFlashcardToSetUseCase = addFlashcardToSetUseCase;
        this.listFlashcardsInSetUseCase = listFlashcardsInSetUseCase;
        this.deleteFlashcardFromSetUseCase = deleteFlashcardFromSetUseCase;
        this.flashcardStatisticsRepository = flashcardStatisticsRepository;
    }

    public void addFlashcard() {
        FlashcardCreationMenuHelper creationHelper = new FlashcardCreationMenuHelper(flashcardSet, addFlashcardToSetUseCase);
        FlashcardCreationMenu creationMenu = new FlashcardCreationMenu(creationHelper);
        creationMenu.start();
    }

    public void listFlashcards() {
        List<Flashcard> flashcards = listFlashcardsInSetUseCase.execute(flashcardSet);
        Map<String, FlashcardStatistics> statistics = flashcardStatisticsRepository.findAll();
        MenuUtils.displayFlashcards(flashcards, statistics, "Fragen in '" + flashcardSet.getName() + "'");
    }

    public void deleteFlashcard() {
        List<Flashcard> flashcards = listFlashcardsInSetUseCase.execute(flashcardSet);
        MenuUtils.displayFlashcards(flashcards, flashcardStatisticsRepository.findAll(), "Fragen in '" + flashcardSet.getName() + "'");
        if (flashcards.isEmpty()) return;

        int index = MenuUtils.selectIndexFromList(flashcards, "Geben Sie die Nummer der Frage ein, die gelöscht werden soll: ");
        if (index == -1) return;

        deleteFlashcardFromSetUseCase.execute(flashcardSet, index)
                .ifPresent(removed -> System.out.println("Frage '" + removed.getQuestion() + "' wurde gelöscht."));
        flashcardSet.setFlashcards(listFlashcardsInSetUseCase.execute(flashcardSet));
    }
}
