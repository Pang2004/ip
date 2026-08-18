package yanny;

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
        System.out.println(BORDER);
        System.out.println("| YANNY_OS :: BOOT SEQUENCE COMPLETE");
        System.out.println(BANNER);
        System.out.println();
        System.out.println("| Hello! I'm Yanny.");
        System.out.println("| SYSTEM READY. Awaiting command...");
        System.out.println(BORDER);

        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(BORDER);

            if (command.equalsIgnoreCase("bye")) {
                System.out.println("| YANNY_OS :: SHUTDOWN INITIATED");
                System.out.println("| OUTPUT > Bye. Hope to see you again!");
                System.out.println(BORDER);
                break;
            }

            if (command.equalsIgnoreCase("list")) {
                System.out.println("| YANNY_OS :: TASK LIST");
                if (taskCount == 0) {
                    System.out.println("| OUTPUT > NO TASKS STORED");
                } else {
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println("| " + (i + 1) + ".[" + tasks[i].getStatusIcon() + "] "
                                + tasks[i].getDescription());
                    }
                }
                System.out.println(BORDER);
                continue;
            }

            if (command.equalsIgnoreCase("mark")
                    || command.toLowerCase().startsWith("mark ")) {
                String taskNumberText = command.length() > 4 ? command.substring(4).trim() : "";
                int taskIndex = -1;
                try {
                    taskIndex = Integer.parseInt(taskNumberText) - 1;
                } catch (NumberFormatException exception) {
                    // Keep the task manager running when the task number is invalid.
                }

                if (taskIndex >= 0 && taskIndex < taskCount) {
                    tasks[taskIndex].markAsDone();
                    System.out.println("| YANNY_OS :: MARKED TASK SUCCESSFULLY");
                    System.out.println("| OUTPUT > [X] " + tasks[taskIndex].getDescription());
                } else {
                    System.out.println("| YANNY_OS :: MARK TASK FAILED");
                    System.out.println("| OUTPUT > INVALID TASK NUMBER");
                }
                System.out.println(BORDER);
                continue;
            }

            if (command.equalsIgnoreCase("unmark")
                    || command.toLowerCase().startsWith("unmark ")) {
                String taskNumberText = command.length() > 6 ? command.substring(6).trim() : "";
                int taskIndex = -1;
                try {
                    taskIndex = Integer.parseInt(taskNumberText) - 1;
                } catch (NumberFormatException exception) {
                    // Keep the task manager running when the task number is invalid.
                }

                if (taskIndex >= 0 && taskIndex < taskCount) {
                    tasks[taskIndex].markAsNotDone();
                    System.out.println("| YANNY_OS :: UNMARKED TASK SUCCESFULLY");
                    System.out.println("| OUTPUT > [ ] " + tasks[taskIndex].getDescription());
                } else {
                    System.out.println("| YANNY_OS :: UNMARK TASK FAILED");
                    System.out.println("| OUTPUT > INVALID TASK NUMBER");
                }
                System.out.println(BORDER);
                continue;
            }

            System.out.println("| YANNY_OS :: COMMAND RECEIVED");
            System.out.println("| INPUT  > " + command);
            if (command.isBlank()) {
                System.out.println("| OUTPUT > PLEASE ENTER A TASK");
            } else if (taskCount == tasks.length) {
                System.out.println("| OUTPUT > TASK STORAGE FULL");
            } else {
                tasks[taskCount] = new Task(command);
                taskCount++;
                System.out.println("| OUTPUT > ADDED: " + command);
            }
            System.out.println(BORDER);
        }
    }
}
