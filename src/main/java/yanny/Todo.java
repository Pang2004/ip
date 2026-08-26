package yanny;

/**
 * Represents a task without a date or time.
 */
public class Todo {
    private final String description;

    private boolean isDone;

    /**
     * Creates a new incomplete todo task.
     *
     * @param description the text describing the task.
     */
    public Todo(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public String getDescription() {
        return description;
    }

    /** Marks this task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns this todo task in a format suitable for displaying to the user.
     *
     * @return the formatted todo task.
     */
    @Override
    public String toString() {
        return "[T][" + getStatusIcon() + "] " + description;
    }
}
