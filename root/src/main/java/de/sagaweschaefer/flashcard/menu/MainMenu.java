package src.main.java.de.sagaweschaefer.flashcard.menu;

import src.main.java.de.sagaweschaefer.flashcard.util.AppScanner;

public class MainMenu extends Menu {

    private FlashcardManagerMenu flashcardManagerMenu = new FlashcardManagerMenu();

    @Override
    protected void showMenu() {
        System.out.println("\n=== Hauptmenü ===");
        System.out.println("1. Flashcard Manager öffnen");
        System.out.println("0. Programm beenden");
        System.out.print("Bitte wählen Sie eine Option: ");
    }

    @Override
    protected boolean handleSelection(int selection) {
        return switch (selection) {
            case 0 -> {
                System.out.println("Programm wird beendet. Auf Wiedersehen!");
                yield false;
            }
            case 1 -> {
                flashcardManagerMenu.start();
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