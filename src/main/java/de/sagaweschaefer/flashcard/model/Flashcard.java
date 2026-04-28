package de.sagaweschaefer.flashcard.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Flashcard implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private FlashcardId id;
    private final String question;
    private final QuestionType questionType;
    @JsonProperty("answerText")
    private final String answerText;
    @JsonProperty("answerNum")
    private final Double answerNum;
    private final List<String> options;

    public Flashcard() {
        this(FlashcardId.random().value(), null, null, null, null, null);
    }

    @JsonCreator
    public Flashcard(@JsonProperty("id") String id,
                     @JsonProperty("question") String question,
                     @JsonProperty("questionType") QuestionType questionType,
                     @JsonProperty("answerText") String answerText,
                     @JsonProperty("answerNum") Double answerNum,
                     @JsonProperty("options") List<String> options) {
        setId(id == null ? FlashcardId.random().value() : id);
        this.question = question;
        this.questionType = questionType;
        this.answerText = answerText;
        this.answerNum = answerNum;
        this.options = options;
    }

    // Konstruktor für Freitext
    public Flashcard(String question, String answerText) {
        this(FlashcardId.random().value(), question, QuestionType.FREE_TEXT, answerText, null, null);
    }

    // Konstruktor für Multiple Choice
    public Flashcard(String question, String answerText, List<String> options) {
        this(FlashcardId.random().value(), question, QuestionType.MULTIPLE_CHOICE, answerText, null, options);
    }

    // Konstruktor für True/False
    public Flashcard(String question, boolean trueFalse) {
        this(FlashcardId.random().value(), question, QuestionType.TRUE_FALSE, trueFalse ? "Wahr" : "Falsch", null, null);
    }

    // Konstruktor für numerische Fragen
    public Flashcard(String question, double answerNum) {
        this(FlashcardId.random().value(), question, QuestionType.NUMERIC, null, answerNum, null);
    }

    @JsonProperty("id")
    public String getId() {
        return id == null ? null : id.value();
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = FlashcardId.of(id);
    }

    public String getQuestion() {
        return question;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public List<String> getOptions() {
        return options;
    }


    public boolean checkAnswer(String answer) {
        if (answer == null) return false;

        switch (questionType) {
            case MULTIPLE_CHOICE:
            case FREE_TEXT:
                return answer.trim().equalsIgnoreCase(answerText.trim());
            case TRUE_FALSE:
                String normalizedAnswer = (answer.toLowerCase().startsWith("w")) ? "Wahr" : "Falsch";
                return normalizedAnswer.equalsIgnoreCase(answerText);
            case NUMERIC:
                try {
                    double val = Double.parseDouble(answer);
                    return Math.abs(val - answerNum) < 0.001;
                } catch (NumberFormatException e) {
                    return false;
                }
            default:
                return false;
        }
    }

    public String getCorrectAnswerDisplay() {
        if (questionType == QuestionType.NUMERIC) {
            return String.valueOf(answerNum);
        }
        return answerText;
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, questionType, answerText, answerNum, options);
    }
}
