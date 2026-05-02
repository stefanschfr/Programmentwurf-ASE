package de.sagaweschaefer.flashcard.menu;

import de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.HashMap;
import java.util.Map;

public class Menu {
    private final String title;
    private final Map<Integer, MenuItem> items = new HashMap<>();
    private Runnable beforeDisplayHook = () -> { };

    public Menu(String title) {
        this.title = title;
    }

    public void addItem(int key, MenuItem item) {
        items.put(key, item);
    }

    /** Optionaler Hook, der vor jeder Menüanzeige ausgeführt wird. */
    public void setBeforeDisplayHook(Runnable beforeDisplayHook) {
        this.beforeDisplayHook = (beforeDisplayHook != null) ? beforeDisplayHook : () -> { };
    }

    public void start() {
        boolean running = true;
        while (running) {
            beforeDisplayHook.run();
            showMenu();
            int selection = MenuUtils.promptForInt("Bitte wählen Sie eine Option: ");
            running = handleSelection(selection);
        }
    }

    private void showMenu() {
        System.out.println("\n=== " + title + " ===");
        items.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.println(entry.getKey() + ". " + entry.getValue().getLabel()));
    }

    private boolean handleSelection(int selection) {
        MenuItem item = items.get(selection);
        if (item != null) {
            item.execute();
            return !item.shouldExit();
        } else {
            System.out.println("Ungültige Eingabe! Bitte wählen Sie eine verfügbare Option.");
            return true;
        }
    }
}