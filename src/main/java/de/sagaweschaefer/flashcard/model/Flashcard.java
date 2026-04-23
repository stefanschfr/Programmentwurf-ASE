package de.sagaweschaefer.flashcard.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Flashcard implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String question;
    private QuestionType questionType;
    private String answerText;
    private Double answerNum;
    private List<String> options;

    public Flashcard() {
        this.id = UUID.randomUUID().toString();
    }

    // Konstruktor für Freitext
    public Flashcard(String question, String answerText) {
        this();
        this.question = question;
        this.questionType = QuestionType.FREE_TEXT;
        this.answerText = answerText;
    }

    // Konstruktor für Multiple Choice
    public Flashcard(String question, String answerText, List<String> options) {
        this();
        this.question = question;
        this.questionType = QuestionType.MULTIPLE_CHOICE;
        this.answerText = answerText;
        this.options = options;
    }

    // Konstruktor für True/False
    public Flashcard(String question, boolean trueFalse) {
        this();
        this.question = question;
        this.questionType = QuestionType.TRUE_FALSE;
        this.answerText = trueFalse ? "Wahr" : "Falsch";
    }

    // Konstruktor für numerische Fragen
    public Flashcard(String question, double answerNum) {
        this();
        this.question = question;
        this.questionType = QuestionType.NUMERIC;
        this.answerNum = answerNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Double getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(Double answerNum) {
        this.answerNum = answerNum;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flashcard flashcard = (Flashcard) o;
        return Objects.equals(question, flashcard.question) &&
                questionType == flashcard.questionType &&
                Objects.equals(answerText, flashcard.answerText) &&
                Objects.equals(answerNum, flashcard.answerNum) &&
                Objects.equals(options, flashcard.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, questionType, answerText, answerNum, options);
    }
}
