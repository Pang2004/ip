package yanny;

import java.util.Locale;
import java.util.Scanner;

/**
 * A simple retro-style command-line task manager.
 */
public class Yanny {
    private static final int MAX_TASKS = 100;
    private static final String BORDER = "+------------------------------------------+";
    private static final String BANNER = "____    ____  ___      .__   __. .__   __. ____    ____\n"
            + "\\   \\  /   / /   \\     |  \\ |  | |  \\ |  | \\   \\  /   /\n"
            + " \\   \\/   / /  ^  \\    |   \\|  | |   \\|  |  \\   \\/   /\n"
            + "  \\_    _/ /  /_\\  \\   |  . `  | |  . `  |   \\_    _/\n"
            + "    |  |  /  _____  \\  |  |\\   | |  |\\   |     |  |\n"
            + "    |__| /__/     \\__\\ |__| \\__| |__| \\__|     |__|";

    /**
     * Starts Yanny and processes commands entered by the user.
     *
     * @param args command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        displayStartupScreen();
        Task[] tasks = new Task[MAX_TASKS];
        Scanner scanner = new Scanner(System.in);
        runCommandLoop(scanner, tasks);
    }

    /** Displays the startup message for Yanny. */
    private static void displayStartupScreen() {
        System.out.println(BORDER);
        System.out.println("| YANNY_OS :: BOOT SEQUENCE COMPLETE");
        System.out.println(BANNER);
        System.out.println();
        System.out.println("| GREETINGS I'M YANNY.");
        System.out.println("| SYSTEM READY. AWAITING COMMAND...");
        System.out.println(BORDER);
    }

    /**
     * Reads and processes commands until the user exits or input ends.
     *
     * @param scanner the source of user commands.
     * @param tasks the task storage used by the application.
     */
    private static void runCommandLoop(Scanner scanner, Task[] tasks) {
        int taskCount = 0;
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(BORDER);

            if (command.equalsIgnoreCase("bye")) {
                displayShutdownMessage();
                break;
            }

            taskCount = processCommand(command, tasks, taskCount);
        }
    }

    /**
     * Processes a command other than {@code bye}.
     *
     * @param command the command entered by the user.
     * @param tasks the task storage used by the application.
     * @param taskCount the number of tasks currently stored.
     * @return the updated number of stored tasks.
     */
    private static int processCommand(String command, Task[] tasks, int taskCount) {
        if (command.equalsIgnoreCase("list")) {
            handleListCommand(tasks, taskCount);
            return taskCount;
        }

        if (command.equalsIgnoreCase("mark")
                || command.toLowerCase().startsWith("mark ")) {
            handleMarkCommand(command, tasks, taskCount);
            return taskCount;
        }

        if (command.equalsIgnoreCase("unmark")
                || command.toLowerCase().startsWith("unmark ")) {
            handleUnmarkCommand(command, tasks, taskCount);
            return taskCount;
        }

        return handleAddCommand(command, tasks, taskCount);
    }

    /** Displays the current tasks and their completion status. */
    private static void handleListCommand(Task[] tasks, int taskCount) {
        System.out.println("| YANNY_OS :: TASK LIST");
        if (taskCount == 0) {
            System.out.println("| OUTPUT > NO TASKS STORED");
        } else {
            for (int i = 0; i < taskCount; i++) {
                System.out.println("| " + (i + 1) + ". " + tasks[i]);
            }
        }
        System.out.println(BORDER);
    }

    /** Handles a command to mark a task as done. */
    private static void handleMarkCommand(String command, Task[] tasks, int taskCount) {
        int taskIndex = parseTaskIndex(command, "mark");
        if (taskIndex >= 0 && taskIndex < taskCount) {
            tasks[taskIndex].markAsDone();
            System.out.println("| YANNY_OS :: MARKED TASK SUCCESSFULLY");
            System.out.println("| OUTPUT > [X] " + tasks[taskIndex].getDescription());
        } else {
            System.out.println("| YANNY_OS :: MARK TASK FAILED");
            System.out.println("| OUTPUT > INVALID TASK NUMBER");
        }
        System.out.println(BORDER);
    }

    /** Handles a command to mark a task as not done. */
    private static void handleUnmarkCommand(String command, Task[] tasks, int taskCount) {
        int taskIndex = parseTaskIndex(command, "unmark");
        if (taskIndex >= 0 && taskIndex < taskCount) {
            tasks[taskIndex].markAsNotDone();
            System.out.println("| YANNY_OS :: UNMARKED TASK SUCCESFULLY");
            System.out.println("| OUTPUT > [ ] " + tasks[taskIndex].getDescription());
        } else {
            System.out.println("| YANNY_OS :: UNMARK TASK FAILED");
            System.out.println("| OUTPUT > INVALID TASK NUMBER");
        }
        System.out.println(BORDER);
    }

    /**
     * Adds a task from a command and displays the result.
     *
     * @param command the command entered by the user.
     * @param tasks the task storage used by the application.
     * @param taskCount the number of tasks currently stored.
     * @return the updated number of stored tasks.
     */
    private static int handleAddCommand(String command, Task[] tasks, int taskCount) {
        System.out.println("| YANNY_OS :: COMMAND RECEIVED");
        System.out.println("| INPUT  > " + command);
        try {
            Task task = parseTaskCommand(command);
            if (taskCount == tasks.length) {
                System.out.println("| OUTPUT > TASK STORAGE FULL");
            } else {
                tasks[taskCount] = task;
                int updatedTaskCount = taskCount + 1;
                System.out.println("| OUTPUT > ADDED: " + tasks[taskCount]);
                System.out.println("| OUTPUT > CURRENT TASK COUNT: " + updatedTaskCount);
                taskCount = updatedTaskCount;
            }
        } catch (IllegalArgumentException exception) {
            System.out.println("| YANNY_OS :: COMMAND REJECTED");
            System.out.println("| ERROR > " + exception.getMessage());
        }
        System.out.println(BORDER);
        return taskCount;
    }

    /**
     * Parses a one-based task number from a mark or unmark command.
     *
     * @param command the mark or unmark command.
     * @param commandName the command keyword.
     * @return the zero-based task index, or {@code -1} for invalid input.
     */
    private static int parseTaskIndex(String command, String commandName) {
        String taskNumberText = command.length() > commandName.length()
                ? command.substring(commandName.length()).trim() : "";
        try {
            return Integer.parseInt(taskNumberText) - 1;
        } catch (NumberFormatException exception) {
            // Keep the task manager running when the task number is invalid.
            return -1;
        }
    }

    /** Displays the shutdown message for Yanny. */
    private static void displayShutdownMessage() {
        System.out.println("| YANNY_OS :: SHUTDOWN INITIATED");
        System.out.println("| OUTPUT > Bye. Hope to see you again!");
        System.out.println(BORDER);
    }

    /**
     * Parses a task command into the appropriate task type.
     *
     * @param command the complete command entered by the user.
     * @return the parsed task.
     * @throws IllegalArgumentException if the command is unrecognized or contains
     *                                  invalid task data.
     */
    private static Task parseTaskCommand(String command) throws IllegalArgumentException {
        if (command.isBlank()) {
            throw new IllegalArgumentException("PLEASE ENTER A TASK");
        }

        String lowerCommand = command.toLowerCase(Locale.ROOT);
        if (lowerCommand.equals("todo") || lowerCommand.startsWith("todo ")) {
            return new Todo(requireDescription(command.substring(4), "TODO"));
        }

        if (lowerCommand.equals("deadline") || lowerCommand.startsWith("deadline ")) {
            return parseDeadline(command.substring(8));
        }

        if (lowerCommand.equals("event") || lowerCommand.startsWith("event ")) {
            return parseEvent(command.substring(5));
        }

        throw new IllegalArgumentException("UNKNOWN COMMAND DETECTED");
    }

    /**
     * Parses the description and deadline from a deadline command.
     *
     * @param commandContent the part of the command after {@code deadline}.
     * @return the parsed deadline task.
     * @throws IllegalArgumentException if the description or deadline is invalid.
     */
    private static Deadline parseDeadline(String commandContent) throws IllegalArgumentException {
        int byIndex = findMarker(commandContent, "/by");
        if (byIndex < 0) {
            throw new IllegalArgumentException("DEADLINE TASK REQUIRES A /BY VALUE");
        }

        String description = requireDescription(commandContent.substring(0, byIndex), "DEADLINE");
        String deadline = commandContent.substring(byIndex + 3).trim();
        if (deadline.isBlank()) {
            throw new IllegalArgumentException("DEADLINE TASK REQUIRES A NON-BLANK /BY VALUE");
        }
        return new Deadline(description, deadline);
    }

    /**
     * Parses the description, start, and end values from an event command.
     *
     * @param commandContent the part of the command after {@code event}.
     * @return the parsed event task.
     * @throws IllegalArgumentException if the description or event values are invalid.
     */
    private static Event parseEvent(String commandContent) throws IllegalArgumentException {
        int fromIndex = findMarker(commandContent, "/from");
        if (fromIndex < 0) {
            throw new IllegalArgumentException("EVENT TASK REQUIRES A /FROM VALUE");
        }

        String remainingContent = commandContent.substring(fromIndex + 5);
        int toIndex = findMarker(remainingContent, "/to");
        if (toIndex < 0) {
            throw new IllegalArgumentException("EVENT TASK REQUIRES A /TO VALUE");
        }
        toIndex += fromIndex + 5;

        String description = requireDescription(commandContent.substring(0, fromIndex), "EVENT");
        String start = commandContent.substring(fromIndex + 5, toIndex).trim();
        String end = commandContent.substring(toIndex + 3).trim();
        if (start.isBlank()) {
            throw new IllegalArgumentException("EVENT TASK REQUIRES A NON-BLANK /FROM VALUE");
        }
        if (end.isBlank()) {
            throw new IllegalArgumentException("EVENT TASK REQUIRES A NON-BLANK /TO VALUE");
        }
        return new Event(description, start, end);
    }

    /**
     * Finds a marker at the start of a value or after whitespace.
     *
     * @param text the text in which to search.
     * @param marker the marker to find.
     * @return the marker index, or {@code -1} when the marker is absent.
     */
    private static int findMarker(String text, String marker) {
        String lowerText = text.toLowerCase(Locale.ROOT);
        String lowerMarker = marker.toLowerCase(Locale.ROOT);
        int markerIndex = lowerText.indexOf(lowerMarker);
        while (markerIndex >= 0) {
            boolean startsAtBoundary = markerIndex == 0
                    || Character.isWhitespace(text.charAt(markerIndex - 1));
            if (startsAtBoundary) {
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
     * @throws IllegalArgumentException if the description is blank.
     */
    private static String requireDescription(String text, String taskType)
            throws IllegalArgumentException {
        String description = text.trim();
        if (description.isBlank()) {
            if (taskType.equals("TODO")) {
                throw new IllegalArgumentException("TODO DESCRIPTION CANNOT BE EMPTY");
            }
            throw new IllegalArgumentException(taskType + " TASK DESCRIPTION CANNOT BE BLANK");
        }
        return description;
    }
}
