import java.util.Scanner;

/**
 * A simple retro-style command-line task manager.
 */
public class Yanny {
  /**
   * Starts Yanny and processes commands entered by the user.
   *
   * @param args command-line arguments, which are not used
   */
  public static void main(String[] args) {
    String topBorder = "+------------------------------------------+";
    String bottomBorder = "+------------------------------------------+";
    String banner = "____    ____  ___      .__   __. .__   __. ____    ____\n"
        + "\\   \\  /   / /   \\     |  \\ |  | |  \\ |  | \\   \\  /   /\n"
        + " \\   \\/   / /  ^  \\    |   \\|  | |   \\|  |  \\   \\/   /\n"
        + "  \\_    _/ /  /_\\  \\   |  . `  | |  . `  |   \\_    _/\n"
        + "    |  |  /  _____  \\  |  |\\   | |  |\\   |     |  |\n"
        + "    |__| /__/     \\__\\ |__| \\__| |__| \\__|     |__|";

    System.out.println(topBorder);
    System.out.println("| YANNY_OS :: BOOT SEQUENCE COMPLETE");
    System.out.println(banner);
    System.out.println("");
    System.out.println("| Hello! I'm Yanny.");
    System.out.println("| SYSTEM READY. Awaiting command...");
    System.out.println(bottomBorder);

    String[] tasks = new String[100];
    boolean[] taskDone = new boolean[100];
    int taskCount = 0;
    Scanner scanner = new Scanner(System.in);
    while (scanner.hasNextLine()) {
      String command = scanner.nextLine();
      System.out.println(topBorder);

      if (command.equalsIgnoreCase("bye")) {
        System.out.println("| YANNY_OS :: SHUTDOWN INITIATED");
        System.out.println("| OUTPUT > Bye. Hope to see you again!");
        System.out.println(bottomBorder);
        break;
      }

      if (command.equalsIgnoreCase("list")) {
        System.out.println("| YANNY_OS :: TASK LIST");
        if (taskCount == 0) {
          System.out.println("| OUTPUT > NO TASKS STORED");
        } else {
          for (int i = 0; i < taskCount; i++) {
            String status = taskDone[i] ? "X" : " ";
            System.out.println("| " + (i + 1) + ".[" + status + "] " + tasks[i]);
          }
        }
        System.out.println(bottomBorder);
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
          taskDone[taskIndex] = true;
          System.out.println("| YANNY_OS :: MARKED TASK SUCCESSFULLY");
          System.out.println("| OUTPUT > [X] " + tasks[taskIndex]);
        } else {
          System.out.println("| YANNY_OS :: MARK TASK FAILED");
          System.out.println("| OUTPUT > INVALID TASK NUMBER");
        }
        System.out.println(bottomBorder);
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
          taskDone[taskIndex] = false;
          System.out.println("| YANNY_OS :: UNMARKED TASK SUCCESFULLY");
          System.out.println("| OUTPUT > [ ] " + tasks[taskIndex]);
        } else {
          System.out.println("| YANNY_OS :: UNMARK TASK FAILED");
          System.out.println("| OUTPUT > INVALID TASK NUMBER");
        }
        System.out.println(bottomBorder);
        continue;
      }

      System.out.println("| YANNY_OS :: COMMAND RECEIVED");
      System.out.println("| INPUT  > " + command);
      if (command.isBlank()) {
        System.out.println("| OUTPUT > PLEASE ENTER A TASK");
      } else if (taskCount == tasks.length) {
        System.out.println("| OUTPUT > TASK STORAGE FULL");
      } else {
        tasks[taskCount] = command;
        taskCount++;
        System.out.println("| OUTPUT > ADDED: " + command);
      }
      System.out.println(bottomBorder);
    }
  }
}
