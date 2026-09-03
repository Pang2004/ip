package yanny.task;

/**
 * Represents a task that occurs between a start and end date or time.
 */
public class Event extends Task {
    private final String start;
    private final String end;

    /**
     * Creates a new incomplete event task.
     *
     * @param description the text describing the event.
     * @param start the date or time when the event starts.
     * @param end the date or time when the event ends.
     */
    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    /**
     * Returns the event task including its start and end values.
     *
     * @return the formatted event task.
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + start + " to: " + end + ")";
    }
}
