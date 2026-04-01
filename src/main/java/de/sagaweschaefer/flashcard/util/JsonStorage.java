package de.sagaweschaefer.flashcard.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonStorage {

    private static final String FILE_PATH = "data/flashcard-sets.json";
    private static final String STATISTICS_PATH = "data/flashcard-statistics.json";
    private final ObjectMapper objectMapper;

    public JsonStorage() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void saveFlashcardSets(List<FlashcardSet> flashcardSets) {
        saveToFile(FILE_PATH, flashcardSets);
    }


    public void saveStatistics(Map<String, FlashcardStatistics> statistics) {
        saveToFile(STATISTICS_PATH, statistics);
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


    public Map<String, FlashcardStatistics> loadStatistics() {
        File file = new File(STATISTICS_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try {
            return objectMapper.readValue(file, new TypeReference<Map<String, FlashcardStatistics>>() {});
        } catch (IOException e) {
            System.out.println("Error while loading statistics: " + e.getMessage());
            return new HashMap<>();
        }
    }
}
