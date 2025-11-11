package src.main.java.de.sagaweschaefer.lernkarten.model;

public enum Fragetyp {
    FREITEXT,        // Der Nutzer gibt einen Text als Antwort ein
    MULTIPLE_CHOICE, // Der Nutzer wählt aus mehreren Optionen
    TRUE_FALSE,      // Wahr/Falsch-Frage
    NUMERISCH       // Numerische Eingabe, z. B. Mathe-Aufgabe
}
