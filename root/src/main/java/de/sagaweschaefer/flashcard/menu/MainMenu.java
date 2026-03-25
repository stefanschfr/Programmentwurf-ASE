package src.main.java.de.sagaweschaefer.flashcard.menu;

import src.main.java.de.sagaweschaefer.flashcard.util.AppScanner;
import src.main.java.de.sagaweschaefer.flashcard.util.MenuUtils;

public class MainMenu extends Menu {

    private FlashcardSetManagerMenu flashcardSetManagerMenu = new FlashcardSetManagerMenu();

    @Override
    protected void showMenu() {
        System.out.println("\n=== Hauptmenü ===");
        System.out.println("1. Flashcard Set Manager öffnen");
        System.out.println("0. Programm beenden");
    }

    @Override
    protected boolean handleSelection(int selection) {
        return switch (selection) {
            case 0 -> {
                System.out.println("Programm wird beendet. Auf Wiedersehen!");
                yield false;
            }
            case 1 -> {
                flashcardSetManagerMenu.start();
                yield true;
            }
            default -> {
                System.out.println("Ungültige Eingabe! Bitte wählen Sie eine verfügbare Option.");
                yield true;
            }
        };
    }

    public static void main(String[] args) {
        new MainMenu().start();
        AppScanner.SCANNER.close();
    }
}