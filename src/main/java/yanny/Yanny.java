package yanny;

import java.util.Scanner;

/**
 * A simple retro-style command-line task manager.
 */
public class Yanny {
    private static final int MAX_TASKS = 100;
    private static final String BORDER = "+------------------------------------------+";
    private static final String COMMAND_REJECTED = "| YANNY_OS :: COMMAND REJECTED";
    private static final String ERROR_PREFIX = "| ERROR > ";
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
        CommandProcessor commandProcessor = new CommandProcessor(MAX_TASKS);
        Scanner scanner = new Scanner(System.in);
        runCommandLoop(scanner, commandProcessor);
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
     * @param commandProcessor the component that processes user commands.
     */
    private static void runCommandLoop(Scanner scanner, CommandProcessor commandProcessor) {
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(BORDER);

            if (command.equalsIgnoreCase("bye")) {
                displayShutdownMessage();
                break;
            }

            try {
                commandProcessor.processCommand(command);
            } catch (YannyException exception) {
                displayCommandError(exception);
            }
            System.out.println(BORDER);
        }
    }

    /** Displays a formatted error for invalid user input. */
    private static void displayCommandError(YannyException exception) {
        System.out.println(COMMAND_REJECTED);
        System.out.println(ERROR_PREFIX + exception.getMessage());
    }

    /** Displays the shutdown message for Yanny. */
    private static void displayShutdownMessage() {
        System.out.println("| YANNY_OS :: SHUTDOWN INITIATED");
        System.out.println("| OUTPUT > Bye. Hope to see you again!");
        System.out.println(BORDER);
    }

}
