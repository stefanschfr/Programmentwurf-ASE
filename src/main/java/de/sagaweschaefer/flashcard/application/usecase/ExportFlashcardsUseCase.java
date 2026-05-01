package de.sagaweschaefer.flashcard.application.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.sagaweschaefer.flashcard.model.FlashcardSet;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExportFlashcardsUseCase {
    private final ObjectMapper objectMapper;

    public ExportFlashcardsUseCase() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void execute(List<FlashcardSet> setsToExport, String outputPath) throws IOException {
        File file = new File(outputPath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            //noinspection ResultOfMethodCallIgnored
            parent.mkdirs();
        }
        objectMapper.writeValue(file, setsToExport);
    }
}

