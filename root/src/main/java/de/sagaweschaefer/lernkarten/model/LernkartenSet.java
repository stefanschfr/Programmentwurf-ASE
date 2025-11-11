package src.main.java.de.sagaweschaefer.lernkarten.model;

import java.util.ArrayList;
import java.util.List;

public class LernkartenSet {

    private String name;
    private List<Lernkarte> karten;

    // Standard-Konstruktor (z.B. für JSON)
    public LernkartenSet() {
        this.karten = new ArrayList<>();
    }

    // Konstruktor mit Namen
    public LernkartenSet(String name) {
        this.name = name;
        this.karten = new ArrayList<>();
    }
}
