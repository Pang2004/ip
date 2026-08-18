/**
 * Represents a task that can be marked as done or not done.
 */
public class Task {
  /** The text describing this task. */
  protected String description;

  /** Whether this task has been completed. */
  protected boolean isDone;

  /**
   * Creates a new incomplete task.
   *
   * @param description the text describing the task
   */
  public Task(String description) {
    this.description = description;
    this.isDone = false;
  }

  /**
   * Returns the status icon used when displaying this task.
   *
   * @return {@code X} for a completed task, or a space otherwise
   */
  public String getStatusIcon() {
    return isDone ? "X" : " ";
  }

  /**
   * Returns the task description.
   *
   * @return the task description
   */
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
}
