package src.main.java.de.sagaweschaefer.flashcard.model;

import java.io.Serializable;
import java.util.List;

public class Flashcard implements Serializable {

    private static final long serialVersionUID = 1L;

    private String question;
    private QuestionType questionType;
    private String answerText;
    private Double answerNum;
    private List<String> options;

    public Flashcard() {}

    // Konstruktor für Freitext
    public Flashcard(String question, String answerText) {
        this.question = question;
        this.questionType = QuestionType.FREE_TEXT;
        this.answerText = answerText;
    }

    // Konstruktor für Multiple Choice
    public Flashcard(String question, String answerText, List<String> options) {
        this.question = question;
        this.questionType = QuestionType.MULTIPLE_CHOICE;
        this.answerText = answerText;
        this.options = options;
    }

    // Konstruktor für True/False
    public Flashcard(String question, boolean trueFalse) {
        this.question = question;
        this.questionType = QuestionType.TRUE_FALSE;
        this.answerText = trueFalse ? "Wahr" : "Falsch";
    }

    // Konstruktor für numerische Fragen
    public Flashcard(String question, double answerNum) {
        this.question = question;
        this.questionType = QuestionType.NUMERIC;
        this.answerNum = answerNum;
    }

    public String getQuestion() {
        return question;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public String getAnswerText() {
        return answerText;
    }

    public Double getAnswerNum() {
        return answerNum;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public void setAnswerNum(Double answerNum) {
        this.answerNum = answerNum;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
