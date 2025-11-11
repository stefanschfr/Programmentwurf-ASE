package src.main.java.de.sagaweschaefer.lernkarten.model;

import java.util.List;

public class Lernkarte {
    private String frage;          // Die Frage selbst
    private Fragetyp typ;          // Typ der Frage
    private String antwortText;    // Für Freitext, True/False, Multiple Choice
    private Double antwortNum;     // Für numerische Fragen
    private List<String> optionen; // Nur für Multiple Choice

    // Standard-Konstruktor (für JSON oder Libraries)
    public Lernkarte() {}

    // Konstruktor für Freitext
    public Lernkarte(String frage, String antwortText) {
        this.frage = frage;
        this.typ = Fragetyp.FREITEXT;
        this.antwortText = antwortText;
    }

    // Konstruktor für Multiple Choice
    public Lernkarte(String frage, String antwortText, List<String> optionen) {
        this.frage = frage;
        this.typ = Fragetyp.MULTIPLE_CHOICE;
        this.antwortText = antwortText;
        this.optionen = optionen;
    }

    // Konstruktor für True/False
    public Lernkarte(String frage, boolean wahr) {
        this.frage = frage;
        this.typ = Fragetyp.TRUE_FALSE;
        this.antwortText = wahr ? "Wahr" : "Falsch";
    }

    // Konstruktor für numerische Fragen
    public Lernkarte(String frage, double antwortNum) {
        this.frage = frage;
        this.typ = Fragetyp.NUMERISCH;
        this.antwortNum = antwortNum;
    }
}
