package src.main.java.de.sagaweschaefer.flashcard.menu;

import src.main.java.de.sagaweschaefer.flashcard.model.Flashcard;
import src.main.java.de.sagaweschaefer.flashcard.model.FlashcardSet;
import src.main.java.de.sagaweschaefer.flashcard.util.BinaryStorage;

import java.util.List;

public class FlashcardManagerMenuHelper {
    private final FlashcardSet flashcardSet;
    private final List<FlashcardSet> allSets;
    private final BinaryStorage storage = new BinaryStorage();

    public FlashcardManagerMenuHelper(FlashcardSet flashcardSet, List<FlashcardSet> allSets) {
        this.flashcardSet = flashcardSet;
        this.allSets = allSets;
    }

    public void addFlashcard(Flashcard flashcard) {
        flashcardSet.getFlashcardSet().add(flashcard);
        save();
        System.out.println("Frage erfolgreich hinzugefügt!");
    }

    public void listFlashcards() {
        List<Flashcard> flashcards = flashcardSet.getFlashcardSet();
        if (flashcards.isEmpty()) {
            System.out.println("Dieses Set enthält noch keine Fragen.");
        } else {
            System.out.println("\n--- Fragen in '" + flashcardSet.getName() + "' ---");
            for (int i = 0; i < flashcards.size(); i++) {
                Flashcard f = flashcards.get(i);
                System.out.println((i + 1) + ". [" + f.getQuestionType() + "] " + f.getQuestion());
            }
        }
    }

    public boolean deleteFlashcard(int index) {
        List<Flashcard> flashcards = flashcardSet.getFlashcardSet();
        if (index < 0 || index >= flashcards.size()) {
            System.out.println("Ungültige Auswahl! Keine Frage gelöscht.");
            return false;
        }

        Flashcard removed = flashcards.remove(index);
        save();
        System.out.println("Frage '" + removed.getQuestion() + "' wurde gelöscht.");
        return true;
    }

    private void save() {
        storage.saveFlashcardSets(allSets);
    }

    public FlashcardSet getFlashcardSet() {
        return flashcardSet;
    }
}
