package src.main.java.de.sagaweschaefer.flashcard.util;

import src.main.java.de.sagaweschaefer.flashcard.model.FlashcardSet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinaryStorage {

    private static final String FILE_PATH = "data/flashcard-sets.dat";

    public void saveFlashcardSets(List<FlashcardSet> flashcardSets) {
        File file = new File(FILE_PATH);
        File parent = file.getParentFile();

        try {
            if (parent != null) {
                parent.mkdirs();
            }

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(flashcardSets);
            out.close();
        } catch (IOException e) {
            System.out.println("Error while saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<FlashcardSet> loadFlashcardSets() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            List<FlashcardSet> flashcardSets = (List<FlashcardSet>) in.readObject();
            in.close();
            return flashcardSets;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error while loading data: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
