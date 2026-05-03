package de.sagaweschaefer.flashcard.application.usecase;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.sagaweschaefer.flashcard.domain.repository.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.model.FlashcardSet;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImportFlashcardsUseCase {
    private final FlashcardSetRepository repository;
    private final ObjectMapper objectMapper;

    public ImportFlashcardsUseCase(FlashcardSetRepository repository) {
        this.repository = repository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public int execute(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("Datei nicht gefunden: " + filePath);
        }

        List<FlashcardSet> importedSets = objectMapper.readValue(file, new TypeReference<>() {});
        List<FlashcardSet> existingSets = repository.findAll();
        existingSets.addAll(importedSets);
        repository.saveAll(existingSets);
        return importedSets.size();
    }
}

