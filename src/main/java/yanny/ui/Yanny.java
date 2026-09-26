package yanny.ui;

import yanny.command.CommandProcessor;
import yanny.exception.YannyException;

/**
 * Starts Yanny and coordinates its command loop.
 */
public class Yanny {
    /**
     * Starts Yanny and processes commands entered by the user.
     *
     * @param args command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showStartupScreen();
        try {
            CommandProcessor commandProcessor = new CommandProcessor(ui);
            runCommandLoop(ui, commandProcessor);
        } catch (YannyException exception) {
            ui.showCommandError(exception.getMessage());
        }
    }

    /**
     * Reads and processes commands until the user exits or input ends.
     *
     * @param ui the terminal interface for reading commands and showing messages.
     * @param commandProcessor the component that processes user commands.
     */
    private static void runCommandLoop(Ui ui, CommandProcessor commandProcessor) {
        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showBorder();

            if (command.trim().equalsIgnoreCase("bye")) {
                ui.showShutdownMessage();
                break;
            }

            try {
                commandProcessor.processCommand(command);
            } catch (YannyException exception) {
                ui.showCommandError(exception.getMessage());
            }
            ui.showBorder();
        }
    }
}
