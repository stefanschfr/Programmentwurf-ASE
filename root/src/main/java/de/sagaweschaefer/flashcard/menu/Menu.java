package src.main.java.de.sagaweschaefer.flashcard.menu;

import src.main.java.de.sagaweschaefer.flashcard.util.MenuUtils;

public abstract class Menu {

    public void start() {
        boolean running = true;
        while (running) {
            showMenu();
            int selection = MenuUtils.promptForInt("Bitte wählen Sie eine Option: ");
            running = handleSelection(selection);
        }
    }

    protected abstract void showMenu();
    protected abstract boolean handleSelection(int selection);
}