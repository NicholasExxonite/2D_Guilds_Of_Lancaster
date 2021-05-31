package hashpizza.game.engine.ui;

/**
 * Represents a dialogue entry, containing the character it is from and the text that it contains
 */
public class DialogueEntry {

    /**
     * The character that this text is from
     */
    private final String from;

    /**
     * The text in this dialogue
     */
    private final String text;

    /**
     * Creates a new dialogue entry with the specified character and text
     *
     * @param from the character who said this text
     * @param text the text to display
     */
    public DialogueEntry(String from, String text) {

        this.from = from;
        this.text = text;
    }

    /**
     * @return the character that has said this dialogue
     */
    public String getFrom() {
        return from;
    }

    /**
     * @return the dialogue to display
     */
    public String getText() {
        return text;
    }
}
