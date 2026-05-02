package de.sagaweschaefer.flashcard.adapter.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.sagaweschaefer.flashcard.domain.repository.DailyGoalRepository;
import de.sagaweschaefer.flashcard.domain.valueobject.DailyGoal;
import de.sagaweschaefer.flashcard.model.DailyProgress;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * JSON-basierte Implementierung des {@link DailyGoalRepository}.
 *
 * <p>Speichert Goal und Tagesfortschritte gemeinsam in einer JSON-Datei.</p>
 */
@SuppressWarnings("unused") // Wird via ApplicationContext instanziiert
public class JsonDailyGoalRepository implements DailyGoalRepository {

    private final String filePath;
    private final ObjectMapper objectMapper;

    public JsonDailyGoalRepository(String filePath) {
        this.filePath = filePath;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Optional<DailyGoal> findGoal() {
        DailyGoalFile data = readFile();
        if (data.cardsPerDay <= 0) {
            return Optional.empty();
        }
        try {
            return Optional.of(new DailyGoal(data.cardsPerDay));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    public void saveGoal(DailyGoal goal) {
        DailyGoalFile data = readFile();
        data.cardsPerDay = goal.getCardsPerDay();
        writeFile(data);
    }

    @Override
    public Optional<DailyProgress> findProgress(LocalDate date) {
        DailyGoalFile data = readFile();
        DailyProgress progress = data.progress.get(date.toString());
        return Optional.ofNullable(progress);
    }

    @Override
    public void saveProgress(DailyProgress progress) {
        DailyGoalFile data = readFile();
        data.progress.put(progress.getDate().toString(), progress);
        writeFile(data);
    }

    private DailyGoalFile readFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new DailyGoalFile();
        }
        try {
            DailyGoalFile loaded = objectMapper.readValue(file, DailyGoalFile.class);
            if (loaded == null) return new DailyGoalFile();
            if (loaded.progress == null) loaded.progress = new HashMap<>();
            return loaded;
        } catch (IOException e) {
            System.out.println("Fehler beim Laden des Lernplans: " + e.getMessage());
            return new DailyGoalFile();
        }
    }

    private void writeFile(DailyGoalFile data) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        try {
            if (parent != null && !parent.exists()) {
                //noinspection ResultOfMethodCallIgnored
                parent.mkdirs();
            }
            objectMapper.writeValue(file, data);
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern des Lernplans: " + e.getMessage());
        }
    }

    /** Internes Dateischema. */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @SuppressWarnings("unused") // Felder werden via Reflection durch Jackson genutzt
    static class DailyGoalFile implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        public int cardsPerDay = 0;
        public Map<String, DailyProgress> progress = new HashMap<>();
    }
}




