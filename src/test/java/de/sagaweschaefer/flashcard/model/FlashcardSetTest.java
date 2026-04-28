package de.sagaweschaefer.flashcard.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlashcardSetTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void serializesNameAsPlainStringProperty() {
        FlashcardSet set = new FlashcardSet("  Java Basics  ");

        JsonNode json = objectMapper.valueToTree(set);

        assertEquals("Java Basics", json.get("name").asText());
    }

    @Test
    void deserializesNameIntoValueObjectBackedEntity() throws Exception {
        FlashcardSet set = objectMapper.readValue("{\"name\":\"  Java Basics  \",\"flashcards\":[]}", FlashcardSet.class);

        assertEquals("Java Basics", set.getName());
        assertEquals(FlashcardSetName.of("java basics"), set.getSetName());
    }

    @Test
    void renamesViaValueObject() {
        FlashcardSet set = new FlashcardSet();

        set.renameTo(FlashcardSetName.of("  Java Basics  "));

        assertEquals("Java Basics", set.getName());
        assertEquals(FlashcardSetName.of("java basics"), set.getSetName());
    }
}



