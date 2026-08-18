import java.util.Scanner;

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

      System.out.println("| YANNY_OS :: COMMAND RECEIVED");
      System.out.println("| INPUT  > " + command);
      System.out.println("| OUTPUT > " + command);
      System.out.println(bottomBorder);
    }
  }
}
