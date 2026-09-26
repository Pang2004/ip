package yanny.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import yanny.exception.YannyException;
import yanny.storage.TaskFileWriter;
import yanny.task.Task;
import yanny.ui.Ui;

/**
 * Processes user commands and manages the tasks stored by Yanny.
 */
public class CommandProcessor {
    private final Parser parser = new Parser();
    private final List<Task> tasks;
    private final TaskFileWriter taskFileWriter;
    private final Ui ui;

    /**
     * Creates a command processor with dynamically sized task storage.
     *
     * @param ui the terminal interface used to display command results.
     * @throws YannyException if existing task data cannot be loaded.
     */
    public CommandProcessor(Ui ui) throws YannyException {
        this.ui = ui;
        tasks = new ArrayList<>();
        TaskFileWriter writer;
        try {
            writer = new TaskFileWriter();
            tasks.addAll(writer.loadTasks());
        } catch (IOException | IllegalArgumentException | SecurityException exception) {
            throw new YannyException("TASK DATA COULD NOT BE LOADED. CHECK FILE FORMAT AND PERMISSIONS.");
        }
        taskFileWriter = writer;
    }

    /**
     * Processes one command and updates the stored tasks when necessary.
     *
     * @param command the command entered by the user.
     * @throws YannyException if the command contains invalid user input.
     */
    public void processCommand(String command) throws YannyException {
        if (command == null) {
            throw new YannyException("COMMAND CANNOT BE EMPTY. ENTER A SUPPORTED COMMAND.");
        }
        String trimmedCommand = command.trim();

        if (trimmedCommand.equalsIgnoreCase("list")) {
            handleListCommand();
            return;
        }

        if (parser.isCommand(trimmedCommand, "mark")) {
            handleMarkCommand(trimmedCommand);
            return;
        }

        if (parser.isCommand(trimmedCommand, "unmark")) {
            handleUnmarkCommand(trimmedCommand);
            return;
        }

        if (command.equalsIgnoreCase("delete")
                || command.toLowerCase(Locale.ROOT).startsWith("delete ")) {
            handleDeleteCommand(command, "delete");
            return;
        }

        if (command.equalsIgnoreCase("remove")
                || command.toLowerCase(Locale.ROOT).startsWith("remove ")) {
            handleDeleteCommand(command, "remove");
            return;
        }

        handleAddCommand(command);
    }

    /** Displays the current tasks and their completion status. */
    private void handleListCommand() {
        ui.showTaskList(tasks);
    }

    /** Handles a command to mark a task as done. */
    private void handleMarkCommand(String command) throws YannyException {
        int taskIndex = parser.parseTaskIndex(command, "mark");
        validateTaskIndex(taskIndex, "MARK");
        Task task = tasks.get(taskIndex);
        boolean wasDone = task.isDone();
        task.markAsDone();
        try {
            saveTasks();
        } catch (YannyException exception) {
            restoreTaskStatus(task, wasDone);
            throw exception;
        }
        ui.showMarkedTask(task);
    }

    /** Handles a command to mark a task as not done. */
    private void handleUnmarkCommand(String command) throws YannyException {
        int taskIndex = parser.parseTaskIndex(command, "unmark");
        validateTaskIndex(taskIndex, "UNMARK");
        Task task = tasks.get(taskIndex);
        boolean wasDone = task.isDone();
        task.markAsNotDone();
        try {
            saveTasks();
        } catch (YannyException exception) {
            restoreTaskStatus(task, wasDone);
            throw exception;
        }
        ui.showUnmarkedTask(task);
    }

    /**
     * Deletes a task from the collection and displays the removed task.
     *
     * @param command the delete or remove command.
     * @param commandName the command keyword used in validation messages.
     * @throws YannyException if the task number is missing or invalid.
     */
    private void handleDeleteCommand(String command, String commandName) throws YannyException {
        int taskIndex = parser.parseTaskIndex(command, commandName);
        String upperCommandName = commandName.toUpperCase(Locale.ROOT);
        validateTaskIndex(taskIndex, upperCommandName);
        Task deletedTask = tasks.remove(taskIndex);
        try {
            saveTasks();
        } catch (YannyException exception) {
            tasks.add(taskIndex, deletedTask);
            throw exception;
        }
        ui.showDeletedTask(deletedTask);
    }

    /**
     * Adds a task from a command and displays the result.
     *
     * @param command the command entered by the user.
     * @throws YannyException if the command contains invalid user input.
     */
    private void handleAddCommand(String command) throws YannyException {
        ui.showCommandReceived(command);
        Task task = parser.parseTaskCommand(command);
        tasks.add(task);
        try {
            saveTasks();
        } catch (YannyException exception) {
            tasks.remove(tasks.size() - 1);
            throw exception;
        }
        ui.showAddedTask(task, tasks.size());
    }

    /** Saves the current task list and reports file-system failures as user errors. */
    private void saveTasks() throws YannyException {
        try {
            taskFileWriter.saveTasks(tasks);
        } catch (IOException | IllegalArgumentException | SecurityException exception) {
            throw new YannyException("TASK DATA COULD NOT BE SAVED. CHECK FILE PERMISSIONS.");
        }
    }

    /** Restores a task's completion state after a failed persistence operation. */
    private void restoreTaskStatus(Task task, boolean wasDone) {
        if (wasDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
    }

    /**
     * Rejects a task index that does not refer to a stored task.
     *
     * @param taskIndex the zero-based task index.
     * @param commandName the command being validated.
     * @throws YannyException if no tasks exist or the index is out of range.
     */
    private void validateTaskIndex(int taskIndex, String commandName) throws YannyException {
        if (tasks.isEmpty()) {
            throw new YannyException("NO TASKS AVAILABLE. ADD A TASK BEFORE USING " + commandName + ".");
        }
        if (taskIndex >= tasks.size()) {
            throw new YannyException("TASK NUMBER OUT OF RANGE. USE A NUMBER FROM 1 TO " + tasks.size() + ".");
        }
    }
}
