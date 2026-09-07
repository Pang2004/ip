package yanny.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import yanny.task.Deadline;
import yanny.task.Event;
import yanny.task.Task;

/**
 * Writes Yanny's current task list to a local data file.
 */
public class TaskFileWriter {
    private static final Path DATA_FILE = Path.of("data", "yanny.txt");

    /**
     * Writes all supplied tasks to the data file, replacing its previous contents.
     *
     * @param tasks the tasks to serialize.
     * @throws IOException if the data directory or file cannot be written.
     */
    public void saveTasks(List<Task> tasks) throws IOException {
        Path dataDirectory = DATA_FILE.getParent();
        Files.createDirectories(dataDirectory);
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(serializeTask(task));
        }
        Files.write(DATA_FILE, lines, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Converts one task to its line-based storage representation.
     *
     * @param task the task to serialize.
     * @return the serialized task line.
     */
    private String serializeTask(Task task) {
        String status = task.isDone() ? "1" : "0";
        if (task instanceof Deadline deadline) {
            return "D | " + status + " | " + task.getDescription()
                    + " | " + deadline.getDeadline();
        }
        if (task instanceof Event event) {
            return "E | " + status + " | " + task.getDescription()
                    + " | " + event.getStart() + " | " + event.getEnd();
        }
        return "T | " + status + " | " + task.getDescription();
    }
}
