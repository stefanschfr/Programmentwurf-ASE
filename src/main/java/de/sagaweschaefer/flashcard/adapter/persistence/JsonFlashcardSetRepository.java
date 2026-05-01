package de.sagaweschaefer.flashcard.adapter.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.sagaweschaefer.flashcard.domain.repository.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.model.FlashcardSet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonFlashcardSetRepository implements FlashcardSetRepository {
    private final String filePath;
    private final ObjectMapper objectMapper;

    public JsonFlashcardSetRepository(String filePath) {
        this.filePath = filePath;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public List<FlashcardSet> findAll() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(file, new TypeReference<>() {});
        } catch (IOException e) {
            System.out.println("Fehler beim Laden der Flashcard-Sets: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void saveAll(List<FlashcardSet> sets) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        try {
            if (parent != null && !parent.exists()) {
                //noinspection ResultOfMethodCallIgnored
                parent.mkdirs();
            }
            objectMapper.writeValue(file, sets);
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern der Flashcard-Sets: " + e.getMessage());
        }
    }
}

