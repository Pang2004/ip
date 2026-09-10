package yanny.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import yanny.exception.YannyException;
import yanny.storage.TaskFileWriter;
import yanny.task.Deadline;
import yanny.task.Event;
import yanny.task.Task;
import yanny.task.Todo;

/**
 * Processes user commands and manages the tasks stored by Yanny.
 */
public class CommandProcessor {
    private static final String SUPPORTED_COMMANDS =
            "TODO, DEADLINE, EVENT, LIST, MARK, UNMARK, DELETE, REMOVE, OR BYE";
    private static final String TODO_USAGE = "TODO <DESCRIPTION>";
    private static final String DEADLINE_USAGE = "DEADLINE <DESCRIPTION> /BY <DATE OR TIME>";
    private static final String EVENT_USAGE = "EVENT <DESCRIPTION> /FROM <START> /TO <END>";

    private final List<Task> tasks;
    private final TaskFileWriter taskFileWriter;

    /**
     * Creates a command processor with dynamically sized task storage.
     *
     * @throws YannyException if existing task data cannot be loaded.
     */
    public CommandProcessor() throws YannyException {
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

        if (isCommand(trimmedCommand, "mark")) {
            handleMarkCommand(trimmedCommand);
            return;
        }

        if (isCommand(trimmedCommand, "unmark")) {
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
        System.out.println("| YANNY_OS :: TASK LIST");
        if (tasks.isEmpty()) {
            System.out.println("| OUTPUT > NO TASKS STORED");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println("| " + (i + 1) + ". " + tasks.get(i));
            }
        }
    }

    /** Handles a command to mark a task as done. */
    private void handleMarkCommand(String command) throws YannyException {
        int taskIndex = parseTaskIndex(command, "mark");
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
        System.out.println("| YANNY_OS :: MARKED TASK SUCCESSFULLY");
        System.out.println("| OUTPUT > [X] " + task.getDescription());
    }

    /** Handles a command to mark a task as not done. */
    private void handleUnmarkCommand(String command) throws YannyException {
        int taskIndex = parseTaskIndex(command, "unmark");
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
        System.out.println("| YANNY_OS :: UNMARKED TASK SUCCESFULLY");
        System.out.println("| OUTPUT > [ ] " + task.getDescription());
    }

    /**
     * Deletes a task from the collection and displays the removed task.
     *
     * @param command the delete or remove command.
     * @param commandName the command keyword used in validation messages.
     * @throws YannyException if the task number is missing or invalid.
     */
    private void handleDeleteCommand(String command, String commandName) throws YannyException {
        int taskIndex = parseTaskIndex(command, commandName);
        String upperCommandName = commandName.toUpperCase(Locale.ROOT);
        validateTaskIndex(taskIndex, upperCommandName);
        Task deletedTask = tasks.remove(taskIndex);
        try {
            saveTasks();
        } catch (YannyException exception) {
            tasks.add(taskIndex, deletedTask);
            throw exception;
        }
        System.out.println("| YANNY_OS :: DELETED TASK SUCCESSFULLY");
        System.out.println("| OUTPUT > " + deletedTask);
    }

    /**
     * Adds a task from a command and displays the result.
     *
     * @param command the command entered by the user.
     * @throws YannyException if the command contains invalid user input.
     */
    private void handleAddCommand(String command) throws YannyException {
        System.out.println("| YANNY_OS :: COMMAND RECEIVED");
        String inputDisplay = command.isBlank() ? "" : " " + command;
        System.out.println("| INPUT  >" + inputDisplay);
        Task task = parseTaskCommand(command);
        tasks.add(task);
        try {
            saveTasks();
        } catch (YannyException exception) {
            tasks.remove(tasks.size() - 1);
            throw exception;
        }
        System.out.println("| OUTPUT > ADDED: " + task);
        System.out.println("| OUTPUT > CURRENT TASK COUNT: " + tasks.size());
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

    /** Returns whether a command is exactly a keyword or starts with whitespace after it. */
    private boolean isCommand(String command, String keyword) {
        if (command.length() < keyword.length()
                || !command.regionMatches(true, 0, keyword, 0, keyword.length())) {
            return false;
        }
        return command.length() == keyword.length()
                || Character.isWhitespace(command.charAt(keyword.length()));
    }

    /**
     * Parses a one-based task number from a mark or unmark command.
     *
     * @param command the mark or unmark command.
     * @param commandName the command keyword.
     * @return the zero-based task index.
     * @throws YannyException if the task number is missing or invalid.
     */
    private int parseTaskIndex(String command, String commandName) throws YannyException {
        String taskNumberText = command.length() > commandName.length()
                ? command.substring(commandName.length()).trim() : "";
        String upperCommandName = commandName.toUpperCase(Locale.ROOT);
        if (taskNumberText.isBlank()) {
            throw new YannyException(upperCommandName
                    + " COMMAND REQUIRES A TASK NUMBER. USE: " + upperCommandName + " <NUMBER>");
        }

        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber <= 0) {
                throw new YannyException(upperCommandName
                        + " TASK NUMBER MUST BE POSITIVE. USE: " + upperCommandName + " <NUMBER>");
            }
            return taskNumber - 1;
        } catch (NumberFormatException exception) {
            throw new YannyException(upperCommandName
                    + " TASK NUMBER MUST BE AN INTEGER. USE: " + upperCommandName + " <NUMBER>");
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

    /**
     * Parses a task command into the appropriate task type.
     *
     * @param command the complete command entered by the user.
     * @return the parsed task.
     * @throws YannyException if the command is unrecognized or contains invalid task data.
     */
    private Task parseTaskCommand(String command) throws YannyException {
        if (command == null || command.isBlank()) {
            throw new YannyException("COMMAND CANNOT BE EMPTY. ENTER A SUPPORTED COMMAND.");
        }
        String normalizedCommand = command.trim();

        if (isCommand(normalizedCommand, "todo")) {
            return new Todo(requireDescription(normalizedCommand.substring(4), "TODO"));
        }

        if (isCommand(normalizedCommand, "deadline")) {
            return parseDeadline(normalizedCommand.substring(8));
        }

        if (isCommand(normalizedCommand, "event")) {
            return parseEvent(normalizedCommand.substring(5));
        }

        throw new YannyException("UNKNOWN COMMAND DETECTED. USE: " + SUPPORTED_COMMANDS);
    }

    /**
     * Parses the description and deadline from a deadline command.
     *
     * @param commandContent the part of the command after {@code deadline}.
     * @return the parsed deadline task.
     * @throws YannyException if the description or deadline is invalid.
     */
    private Deadline parseDeadline(String commandContent) throws YannyException {
        int byIndex = findMarker(commandContent, "/by");
        if (byIndex < 0) {
            throw new YannyException("DEADLINE COMMAND REQUIRES: " + DEADLINE_USAGE);
        }

        String description = requireDescription(commandContent.substring(0, byIndex), "DEADLINE");
        String deadline = commandContent.substring(byIndex + 3).trim();
        if (deadline.isBlank()) {
            throw new YannyException("DEADLINE /BY VALUE CANNOT BE EMPTY. USE: " + DEADLINE_USAGE);
        }
        rejectUnsupportedStorageCharacters(deadline, "DEADLINE /BY VALUE");
        return new Deadline(description, deadline);
    }

    /**
     * Parses the description, start, and end values from an event command.
     *
     * @param commandContent the part of the command after {@code event}.
     * @return the parsed event task.
     * @throws YannyException if the description or event values are invalid.
     */
    private Event parseEvent(String commandContent) throws YannyException {
        int fromIndex = findMarker(commandContent, "/from");
        if (fromIndex < 0) {
            throw new YannyException("EVENT COMMAND REQUIRES: " + EVENT_USAGE);
        }

        String remainingContent = commandContent.substring(fromIndex + 5);
        int toIndex = findMarker(remainingContent, "/to");
        if (toIndex < 0) {
            throw new YannyException("EVENT COMMAND REQUIRES: " + EVENT_USAGE);
        }
        toIndex += fromIndex + 5;

        String description = requireDescription(commandContent.substring(0, fromIndex), "EVENT");
        String start = commandContent.substring(fromIndex + 5, toIndex).trim();
        String end = commandContent.substring(toIndex + 3).trim();
        if (start.isBlank()) {
            throw new YannyException("EVENT /FROM VALUE CANNOT BE EMPTY. USE: " + EVENT_USAGE);
        }
        if (end.isBlank()) {
            throw new YannyException("EVENT /TO VALUE CANNOT BE EMPTY. USE: " + EVENT_USAGE);
        }
        rejectUnsupportedStorageCharacters(start, "EVENT /FROM VALUE");
        rejectUnsupportedStorageCharacters(end, "EVENT /TO VALUE");
        return new Event(description, start, end);
    }

    /**
     * Finds a marker at the start of a value or after whitespace.
     *
     * @param text the text in which to search.
     * @param marker the marker to find.
     * @return the marker index, or {@code -1} when the marker is absent.
     */
    private int findMarker(String text, String marker) {
        String lowerText = text.toLowerCase(Locale.ROOT);
        String lowerMarker = marker.toLowerCase(Locale.ROOT);
        int markerIndex = lowerText.indexOf(lowerMarker);
        while (markerIndex >= 0) {
            boolean startsAtBoundary = markerIndex == 0
                    || Character.isWhitespace(text.charAt(markerIndex - 1));
            int markerEnd = markerIndex + marker.length();
            boolean endsAtBoundary = markerEnd == text.length()
                    || Character.isWhitespace(text.charAt(markerEnd));
            if (startsAtBoundary && endsAtBoundary) {
                return markerIndex;
            }
            markerIndex = lowerText.indexOf(lowerMarker, markerIndex + 1);
        }
        return -1;
    }

    /**
     * Returns a non-blank description or rejects the task command.
     *
     * @param text the candidate task description.
     * @param taskType the task type used in the error message.
     * @return the trimmed description.
     * @throws YannyException if the description is blank.
     */
    private String requireDescription(String text, String taskType) throws YannyException {
        String description = text.trim();
        if (description.isBlank()) {
            String usage = switch (taskType) {
            case "TODO" -> TODO_USAGE;
            case "DEADLINE" -> DEADLINE_USAGE;
            case "EVENT" -> EVENT_USAGE;
            default -> taskType;
            };
            throw new YannyException(taskType + " DESCRIPTION CANNOT BE EMPTY. USE: " + usage);
        }
        rejectUnsupportedStorageCharacters(description, taskType + " DESCRIPTION");
        return description;
    }

    /** Rejects values that cannot be represented safely by the task file format. */
    private void rejectUnsupportedStorageCharacters(String value, String fieldName) throws YannyException {
        if (value.indexOf('|') >= 0) {
            throw new YannyException(fieldName + " CONTAINS AN UNSUPPORTED CHARACTER. REMOVE '|'.");
        }
        if (value.indexOf('\u0000') >= 0 || value.indexOf('\n') >= 0 || value.indexOf('\r') >= 0) {
            throw new YannyException(fieldName
                    + " CONTAINS AN UNSUPPORTED CONTROL CHARACTER. USE PLAIN TEXT.");
        }
    }
}
