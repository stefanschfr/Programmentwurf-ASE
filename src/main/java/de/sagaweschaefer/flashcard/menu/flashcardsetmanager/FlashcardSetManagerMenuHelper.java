package de.sagaweschaefer.flashcard.menu.flashcardsetmanager;

import de.sagaweschaefer.flashcard.application.ApplicationContext;
import de.sagaweschaefer.flashcard.application.usecase.ExportFlashcardsUseCase;
import de.sagaweschaefer.flashcard.application.usecase.ImportFlashcardsUseCase;
import de.sagaweschaefer.flashcard.menu.flashcardmanager.FlashcardManagerMenu;
import de.sagaweschaefer.flashcard.menu.flashcardmanager.FlashcardManagerMenuHelper;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.util.JsonStorage;
import de.sagaweschaefer.flashcard.util.MenuUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FlashcardSetManagerMenuHelper {
    private final JsonStorage storage;
    private final ApplicationContext context;
    private final List<FlashcardSet> flashcardSets;

    public FlashcardSetManagerMenuHelper(JsonStorage storage) {
        this.storage = storage;
        this.context = new ApplicationContext();
        this.flashcardSets = storage.loadFlashcardSets();
    }

    public void addFlashcardSet() {
        String name = MenuUtils.promptForString("Name des Lernkartensets: ");
        FlashcardSet set = new FlashcardSet(name);
        flashcardSets.add(set);
        storage.saveFlashcardSets(flashcardSets);
        System.out.println("Lernkartenset '" + set.getName() + "' wurde erfolgreich erstellt!");
    }

    public void listFlashcardSets() {
        MenuUtils.displayFlashcardSets(flashcardSets, "Alle Lernkartensets");
    }

    public void deleteFlashcardSet() {
        listFlashcardSets();
        if (flashcardSets.isEmpty()) return;

        int index = MenuUtils.selectIndexFromList(flashcardSets, "Geben Sie die Nummer des Sets ein, das gelöscht werden soll: ");
        if (index != -1) {
            performDelete(index);
            storage.saveFlashcardSets(flashcardSets);
        }
    }

    private void performDelete(int index) {
        FlashcardSet removed = flashcardSets.remove(index);
        cleanupOrphanedFlashcardSetStatistics(removed);

        System.out.println("Lernkartenset '" + removed.getName() + "' wurde gelöscht.");
    }

    private void cleanupOrphanedFlashcardSetStatistics(FlashcardSet removed) {
        Map<String, FlashcardStatistics> statisticsMap = storage.loadStatistics();
        boolean statsChanged = false;
        for (Flashcard card : removed.getFlashcards()) {
            if (statisticsMap.remove(card.getId()) != null) {
                statsChanged = true;
            }
        }
        if (statsChanged) {
            storage.saveStatistics(statisticsMap);
        }
    }

    public void editFlashcardSet() {
        listFlashcardSets();
        if (flashcardSets.isEmpty()) return;

        FlashcardSet set = MenuUtils.selectFromList(flashcardSets, "Geben Sie die Nummer des Sets ein, das bearbeitet werden soll: ");
        if (set != null) {
            var flashcardManagerHelper = new FlashcardManagerMenuHelper(set, flashcardSets, storage);
            new FlashcardManagerMenu(flashcardManagerHelper, set.getName()).start();
        }
    }

    public void exportFlashcardSets() {
        if (flashcardSets.isEmpty()) {
            System.out.println("Keine Lernkartensets zum Exportieren vorhanden.");
            return;
        }

        String path = MenuUtils.promptForString("Exportpfad eingeben (z.B. export/meine-sets.json): ");
        ExportFlashcardsUseCase exportUseCase = new ExportFlashcardsUseCase();
        try {
            exportUseCase.execute(flashcardSets, path);
            System.out.println(flashcardSets.size() + " Set(s) erfolgreich exportiert nach: " + path);
        } catch (IOException e) {
            System.out.println("Fehler beim Export: " + e.getMessage());
        }
    }

    public void importFlashcardSets() {
        String path = MenuUtils.promptForString("Importpfad eingeben (z.B. export/meine-sets.json): ");
        ImportFlashcardsUseCase importUseCase = new ImportFlashcardsUseCase(context.getFlashcardSetRepository());
        try {
            int count = importUseCase.execute(path);
            // Refresh local list
            flashcardSets.clear();
            flashcardSets.addAll(storage.loadFlashcardSets());
            System.out.println(count + " Set(s) erfolgreich importiert.");
        } catch (IOException e) {
            System.out.println("Fehler beim Import: " + e.getMessage());
        }
    }
}