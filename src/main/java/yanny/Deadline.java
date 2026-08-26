package yanny;

/**
 * Represents a task that must be completed by a specified date or time.
 */
public class Deadline extends Task {
    private final String deadline;

    /**
     * Creates a new incomplete deadline task.
     *
     * @param description the text describing the task.
     * @param deadline the date or time by which the task must be completed.
     */
    public Deadline(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    /**
     * Returns the deadline task including its deadline.
     *
     * @return the formatted deadline task.
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + deadline + ")";
    }
}
