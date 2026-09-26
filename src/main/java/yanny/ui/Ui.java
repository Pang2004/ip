package yanny.ui;

import java.util.Scanner;

/**
 * Reads terminal commands and displays Yanny's session messages.
 */
public class Ui {
    private static final String BORDER = "+------------------------------------------+";
    private static final String COMMAND_REJECTED = "| YANNY_OS :: COMMAND REJECTED";
    private static final String ERROR_PREFIX = "| ERROR > ";
    private static final String BANNER = "____    ____  ___      .__   __. .__   __. ____    ____\n"
            + "\\   \\  /   / /   \\     |  \\ |  | |  \\ |  | \\   \\  /   /\n"
            + " \\   \\/   / /  ^  \\    |   \\|  | |   \\|  |  \\   \\/   /\n"
            + "  \\_    _/ /  /_\\  \\   |  . `  | |  . `  |   \\_    _/\n"
            + "    |  |  /  _____  \\  |  |\\   | |  |\\   |     |  |\n"
            + "    |__| /__/     \\__\\ |__| \\__| |__| \\__|     |__|";

    private final Scanner scanner;

    /** Creates a terminal interface using standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Returns whether another command can be read from standard input.
     *
     * @return true if another line is available.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command from standard input.
     *
     * @return the command line entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays the startup message for Yanny. */
    public void showStartupScreen() {
        System.out.println(BORDER);
        System.out.println("| YANNY_OS :: BOOT SEQUENCE COMPLETE");
        System.out.println(BANNER);
        System.out.println();
        System.out.println("| GREETINGS I'M YANNY.");
        System.out.println("| SYSTEM READY. AWAITING COMMAND...");
        System.out.println(BORDER);
    }

    /** Displays a border between command responses. */
    public void showBorder() {
        System.out.println(BORDER);
    }

    /**
     * Displays a formatted error for invalid user input.
     *
     * @param message the actionable error message.
     */
    public void showCommandError(String message) {
        System.out.println(COMMAND_REJECTED);
        System.out.println(ERROR_PREFIX + message);
    }

    /** Displays the shutdown message for Yanny. */
    public void showShutdownMessage() {
        System.out.println("| YANNY_OS :: SHUTDOWN INITIATED");
        System.out.println("| OUTPUT > Bye. Hope to see you again!");
        System.out.println(BORDER);
    }
}
