package src.main.java.de.sagaweschaefer.lernkarten.model;

import java.util.List;

public class Flashcard {
    private String question;          // Die Frage selbst
    private QuestionType questionType;          // Typ der Frage
    private String answerText;    // Für Freitext, True/False, Multiple Choice
    private Double answerNum;     // Für numerische Fragen
    private List<String> options; // Nur für Multiple Choice

    // Standard-Konstruktor (für JSON oder Libraries)
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
}
