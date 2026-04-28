package de.sagaweschaefer.flashcard.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlashcardStatisticsTest {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void serializesFlashcardIdAsPlainStringProperty() {
        FlashcardStatistics statistics = new FlashcardStatistics(FlashcardId.of("card-123"));

        JsonNode json = objectMapper.valueToTree(statistics);

        assertEquals("card-123", json.get("flashcardId").asText());
    }

    @Test
    void deserializesFlashcardIdIntoValueObjectBackedStatistics() throws Exception {
        FlashcardStatistics statistics = objectMapper.readValue("{" +
                "\"flashcardId\":\" card-123 \"," +
                "\"correctCount\":2," +
                "\"wrongCount\":1," +
                "\"level\":3}", FlashcardStatistics.class);

        assertEquals("card-123", statistics.getFlashcardId());
        assertEquals(FlashcardId.of("card-123"), FlashcardId.of(statistics.getFlashcardId()));
    }
}


