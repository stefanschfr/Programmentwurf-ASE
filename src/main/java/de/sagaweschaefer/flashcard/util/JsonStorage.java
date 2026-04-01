package de.sagaweschaefer.flashcard.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonStorage {

    private static final String FILE_PATH = "data/flashcard-sets.json";
    private static final String WRONG_ANSWERS_PATH = "data/wrong-answers.json";
    private final ObjectMapper objectMapper;

    public JsonStorage() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

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
            objectMapper.writeValue(file, object);
        } catch (IOException e) {
            System.out.println("Error while saving data: " + e.getMessage());
        }
    }

    public List<FlashcardSet> loadFlashcardSets() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file, new TypeReference<List<FlashcardSet>>() {});
        } catch (IOException e) {
            System.out.println("Error while loading flashcard sets: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Flashcard> loadWrongAnswers() {
        File file = new File(WRONG_ANSWERS_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file, new TypeReference<List<Flashcard>>() {});
        } catch (IOException e) {
            System.out.println("Error while loading wrong answers: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
