package src.main.java.de.sagaweschaefer.lernkarten;

import src.main.java.de.sagaweschaefer.lernkarten.model.FlashcardSet;

import java.util.Scanner;

public class FlashcardMenu {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean isProgramRunning = true;

        while (isProgramRunning) {
            showMainMenu();
            int selection = readMenuSelection();

            switch (selection) {
                case 0 -> {
                    System.out.println("Programm wird beendet. Auf Wiedersehen!");
                    isProgramRunning = false;
                }
                case 1 -> createNewFlashcardSet();
                // Hier können weitere Menüpunkte hinzugefügt werden:
                // case 2 -> andereMethode();
                // case 3 -> nochEineMethode();
                default -> System.out.println("Ungültige Eingabe! Bitte wählen Sie eine verfügbare Option.");
            }
        }
        scanner.close();
    }

    private static void showMainMenu() {
        System.out.println("\n=== Lernkarten-Verwaltung ===");
        System.out.println("1. Neues Lernkarten-Set erstellen");
        // Hier können weitere Menüpunkte hinzugefügt werden:
        // System.out.println("2. Andere Option");
        // System.out.println("3. Noch eine Option");
        System.out.println("0. Programm beenden");
        System.out.println("==========================");
        System.out.print("Bitte wählen Sie eine Option: ");
    }

    private static int readMenuSelection() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Ungültige Eingabe
        }
    }

    private static void createNewFlashcardSet() {
        System.out.println("\n--- Neues Lernkarten-Set erstellen ---");
        System.out.println("Bitte geben Sie den Namen des Lernkartensets ein:");
        String setName = scanner.nextLine();

        FlashcardSet set = new FlashcardSet(setName);

        System.out.println("\nLernkartenset '" + setName + "' wurde erfolgreich erstellt!");
    }
}