package src.main.java.de.sagaweschaefer.flashcard.menu;

public class MenuItem {
    private final String label;
    private final Runnable action;
    private final boolean exitAfter;

    public MenuItem(String label, Runnable action) {
        this(label, action, false);
    }

    public MenuItem(String label, Runnable action, boolean exitAfter) {
        this.label = label;
        this.action = action;
        this.exitAfter = exitAfter;
    }

    public String getLabel() {
        return label;
    }

    public void execute() {
        if (action != null) {
            action.run();
        }
    }

    public boolean shouldExit() {
        return exitAfter;
    }
}
