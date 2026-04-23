package de.sagaweschaefer.flashcard.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.model.SessionResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonStorage {

    private static final String FILE_PATH = "data/flashcard-sets.json";
    private static final String STATISTICS_PATH = "data/flashcard-statistics.json";
    private static final String SESSION_RESULTS_PATH = "data/session-results.json";
    private static final String EXAM_RESULTS_PATH = "data/exam-results.json";
    private final ObjectMapper objectMapper;

    public JsonStorage() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void saveFlashcardSets(List<FlashcardSet> flashcardSets) {
        saveToFile(FILE_PATH, flashcardSets);
    }


    public void saveStatistics(Map<String, FlashcardStatistics> statistics) {
        saveToFile(STATISTICS_PATH, statistics);
    }

    public void saveSessionResults(List<SessionResult> results) {
        saveToFile(SESSION_RESULTS_PATH, results);
    }

    public void saveExamResults(List<SessionResult> results) {
        saveToFile(EXAM_RESULTS_PATH, results);
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
            return objectMapper.readValue(file, new TypeReference<List<FlashcardSet>>() {
            });
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
            return objectMapper.readValue(file, new TypeReference<Map<String, FlashcardStatistics>>() {
            });
        } catch (IOException e) {
            System.out.println("Error while loading statistics: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public List<SessionResult> loadSessionResults() {
        return loadResultsFromFile(SESSION_RESULTS_PATH);
    }

    public List<SessionResult> loadExamResults() {
        return loadResultsFromFile(EXAM_RESULTS_PATH);
    }

    private List<SessionResult> loadResultsFromFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file, new TypeReference<List<SessionResult>>() {
            });
        } catch (IOException e) {
            System.out.println("Error while loading results from " + path + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
