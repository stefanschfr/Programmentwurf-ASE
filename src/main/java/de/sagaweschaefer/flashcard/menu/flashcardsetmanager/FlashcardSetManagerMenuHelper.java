package de.sagaweschaefer.flashcard.menu.flashcardsetmanager;

import de.sagaweschaefer.flashcard.application.port.FlashcardStatisticsRepository;
import de.sagaweschaefer.flashcard.application.usecase.flashcard.AddFlashcardToSetUseCase;
import de.sagaweschaefer.flashcard.application.usecase.flashcard.DeleteFlashcardFromSetUseCase;
import de.sagaweschaefer.flashcard.application.usecase.flashcard.ListFlashcardsInSetUseCase;
import de.sagaweschaefer.flashcard.application.usecase.flashcardset.CreateFlashcardSetUseCase;
import de.sagaweschaefer.flashcard.application.usecase.flashcardset.DeleteFlashcardSetUseCase;
import de.sagaweschaefer.flashcard.application.usecase.flashcardset.ListFlashcardSetsUseCase;
import de.sagaweschaefer.flashcard.menu.flashcardmanager.FlashcardManagerMenu;
import de.sagaweschaefer.flashcard.menu.flashcardmanager.FlashcardManagerMenuHelper;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.List;

public class FlashcardSetManagerMenuHelper {
    private final CreateFlashcardSetUseCase createFlashcardSetUseCase;
    private final ListFlashcardSetsUseCase listFlashcardSetsUseCase;
    private final DeleteFlashcardSetUseCase deleteFlashcardSetUseCase;
    private final AddFlashcardToSetUseCase addFlashcardToSetUseCase;
    private final ListFlashcardsInSetUseCase listFlashcardsInSetUseCase;
    private final DeleteFlashcardFromSetUseCase deleteFlashcardFromSetUseCase;
    private final FlashcardStatisticsRepository flashcardStatisticsRepository;

    public FlashcardSetManagerMenuHelper(CreateFlashcardSetUseCase createFlashcardSetUseCase,
                                         ListFlashcardSetsUseCase listFlashcardSetsUseCase,
                                         DeleteFlashcardSetUseCase deleteFlashcardSetUseCase,
                                         AddFlashcardToSetUseCase addFlashcardToSetUseCase,
                                         ListFlashcardsInSetUseCase listFlashcardsInSetUseCase,
                                         DeleteFlashcardFromSetUseCase deleteFlashcardFromSetUseCase,
                                         FlashcardStatisticsRepository flashcardStatisticsRepository) {
        this.createFlashcardSetUseCase = createFlashcardSetUseCase;
        this.listFlashcardSetsUseCase = listFlashcardSetsUseCase;
        this.deleteFlashcardSetUseCase = deleteFlashcardSetUseCase;
        this.addFlashcardToSetUseCase = addFlashcardToSetUseCase;
        this.listFlashcardsInSetUseCase = listFlashcardsInSetUseCase;
        this.deleteFlashcardFromSetUseCase = deleteFlashcardFromSetUseCase;
        this.flashcardStatisticsRepository = flashcardStatisticsRepository;
    }

    public void addFlashcardSet() {
        String name = MenuUtils.promptForString("Name des Lernkartensets: ");
        try {
            FlashcardSet set = createFlashcardSetUseCase.execute(name);
            System.out.println("Lernkartenset '" + set.getName() + "' wurde erfolgreich erstellt!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void listFlashcardSets() {
        List<FlashcardSet> flashcardSets = listFlashcardSetsUseCase.execute();
        MenuUtils.displayFlashcardSets(flashcardSets, "Alle Lernkartensets");
    }

    public void deleteFlashcardSet() {
        List<FlashcardSet> flashcardSets = listFlashcardSetsUseCase.execute();
        MenuUtils.displayFlashcardSets(flashcardSets, "Alle Lernkartensets");
        if (flashcardSets.isEmpty()) return;

        int index = MenuUtils.selectIndexFromList(flashcardSets, "Geben Sie die Nummer des Sets ein, das gelöscht werden soll: ");
        if (index != -1) {
            FlashcardSet selectedSet = flashcardSets.get(index);
            deleteFlashcardSetUseCase.execute(selectedSet)
                    .ifPresent(removedSet -> System.out.println("Lernkartenset '" + removedSet.getName() + "' wurde gelöscht."));
        }
    }

    public void editFlashcardSet() {
        List<FlashcardSet> flashcardSets = listFlashcardSetsUseCase.execute();
        MenuUtils.displayFlashcardSets(flashcardSets, "Alle Lernkartensets");
        if (flashcardSets.isEmpty()) return;

        FlashcardSet set = MenuUtils.selectFromList(flashcardSets, "Geben Sie die Nummer des Sets ein, das bearbeitet werden soll: ");
        if (set != null) {
            var flashcardManagerHelper = new FlashcardManagerMenuHelper(
                    set,
                    addFlashcardToSetUseCase,
                    listFlashcardsInSetUseCase,
                    deleteFlashcardFromSetUseCase,
                    flashcardStatisticsRepository
            );
            new FlashcardManagerMenu(flashcardManagerHelper, set.getName()).start();
        }
    }
}