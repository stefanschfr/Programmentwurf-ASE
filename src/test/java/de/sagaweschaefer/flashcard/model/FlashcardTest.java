package de.sagaweschaefer.flashcard.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FlashcardTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void serializesIdAsPlainStringProperty() {
        Flashcard flashcard = new Flashcard("Frage", "Antwort");
        flashcard.setId("card-123");

        JsonNode json = objectMapper.valueToTree(flashcard);

        assertEquals("card-123", json.get("id").asText());
    }

    @Test
    void deserializesIdIntoValueObjectBackedEntity() throws Exception {
        Flashcard flashcard = objectMapper.readValue("{" +
                "\"id\":\" card-123 \"," +
                "\"question\":\"Frage\"," +
                "\"questionType\":\"FREE_TEXT\"," +
                "\"answerText\":\"Antwort\"}", Flashcard.class);

        assertEquals("card-123", flashcard.getId());
        assertEquals(FlashcardId.of("card-123"), FlashcardId.of(flashcard.getId()));
    }

    @Test
    void deserializesAllConstructorBackedFields() throws Exception {
        Flashcard flashcard = objectMapper.readValue("{" +
                "\"id\":\"card-456\"," +
                "\"question\":\"Wie viel ist 2+2?\"," +
                "\"questionType\":\"MULTIPLE_CHOICE\"," +
                "\"answerText\":\"4\"," +
                "\"options\":[\"3\",\"4\",\"5\"]}", Flashcard.class);

        assertEquals("card-456", flashcard.getId());
        assertEquals("Wie viel ist 2+2?", flashcard.getQuestion());
        assertEquals(QuestionType.MULTIPLE_CHOICE, flashcard.getQuestionType());
        assertEquals(3, flashcard.getOptions().size());
        assertEquals("4", flashcard.getCorrectAnswerDisplay());
    }

    @Test
    void defaultConstructorStillCreatesAnId() {
        Flashcard flashcard = new Flashcard();

        assertNotNull(flashcard.getId());
    }
}


