package src.main.java.de.sagaweschaefer.flashcard.util;

import src.main.java.de.sagaweschaefer.flashcard.model.Flashcard;
import src.main.java.de.sagaweschaefer.flashcard.model.FlashcardSet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinaryStorage {

    private static final String FILE_PATH = "data/flashcard-sets.dat";
    private static final String WRONG_ANSWERS_PATH = "data/wrong-answers.dat";

    public void saveFlashcardSets(List<FlashcardSet> flashcardSets) {
        saveToFile(FILE_PATH, flashcardSets);
    }

    public void saveWrongAnswers(List<Flashcard> wrongAnswers) {
        saveToFile(WRONG_ANSWERS_PATH, wrongAnswers);
    }

    private void saveToFile(String path, Object object) {
        File file = new File(path);
        File parent = file.getParentFile();

        try {
            if (parent != null) {
                parent.mkdirs();
            }

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(object);
            out.close();
        } catch (IOException e) {
            System.out.println("Error while saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<FlashcardSet> loadFlashcardSets() {
        return (List<FlashcardSet>) loadFromFile(FILE_PATH, new ArrayList<FlashcardSet>());
    }

    @SuppressWarnings("unchecked")
    public List<Flashcard> loadWrongAnswers() {
        return (List<Flashcard>) loadFromFile(WRONG_ANSWERS_PATH, new ArrayList<Flashcard>());
    }

    private Object loadFromFile(String path, Object defaultObject) {
        File file = new File(path);

        if (!file.exists()) {
            return defaultObject;
        }

        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            Object object = in.readObject();
            in.close();
            return object;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error while loading data: " + e.getMessage());
            return defaultObject;
        }
    }
}
