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
import yanny.task.Todo;

/**
 * Reads and writes Yanny's task list in a local data file.
 */
public class TaskFileWriter {
    private static final String DATA_FILE_PROPERTY = "yanny.data.file";
    private static final Path DEFAULT_DATA_FILE = Path.of("data", "yanny.txt");
    private final Path dataFile;

    /** Creates a file helper using the default path or a configured test path. */
    public TaskFileWriter() {
        String configuredPath = System.getProperty(DATA_FILE_PROPERTY);
        dataFile = configuredPath == null || configuredPath.isBlank()
                ? DEFAULT_DATA_FILE : Path.of(configuredPath);
    }

    /**
     * Writes all supplied tasks to the data file, replacing its previous contents.
     *
     * @param tasks the tasks to serialize.
     * @throws IOException if the data directory or file cannot be written.
     */
    public void saveTasks(List<Task> tasks) throws IOException {
        Path dataDirectory = dataFile.getParent();
        if (dataDirectory != null) {
            Files.createDirectories(dataDirectory);
        }
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(serializeTask(task));
        }
        Files.write(dataFile, lines, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Loads all tasks from the data file.
     *
     * @return the tasks stored in the file, or an empty list when it does not exist.
     * @throws IOException if the existing data file cannot be read.
     * @throws IllegalArgumentException if a stored line is not in the expected format.
     */
    public List<Task> loadTasks() throws IOException {
        if (!Files.exists(dataFile)) {
            return new ArrayList<>();
        }

        List<Task> tasks = new ArrayList<>();
        for (String line : Files.readAllLines(dataFile, StandardCharsets.UTF_8)) {
            if (!line.isBlank()) {
                tasks.add(parseTask(line));
            }
        }
        return tasks;
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

    /**
     * Parses one line from the task data file.
     *
     * @param line the serialized task line.
     * @return the parsed task.
     */
    private Task parseTask(String line) {
        String[] fields = line.split("\\s*\\|\\s*", -1);
        if (fields.length < 3) {
            throw new IllegalArgumentException("A task record must contain a type, status, and description.");
        }

        String type = fields[0].trim();
        String status = fields[1].trim();
        Task task;
        switch (type) {
        case "T" -> {
            requireFieldCount(fields, 3, type);
            task = new Todo(requireValue(fields[2], "description"));
        }
        case "D" -> {
            requireFieldCount(fields, 4, type);
            task = new Deadline(requireValue(fields[2], "description"),
                    requireValue(fields[3], "deadline"));
        }
        case "E" -> {
            requireFieldCount(fields, 5, type);
            task = new Event(requireValue(fields[2], "description"),
                    requireValue(fields[3], "event start"), requireValue(fields[4], "event end"));
        }
        default -> throw new IllegalArgumentException("Unknown task type: " + type);
        }

        if ("1".equals(status)) {
            task.markAsDone();
        } else if (!"0".equals(status)) {
            throw new IllegalArgumentException("Task status must be 0 or 1.");
        }
        return task;
    }

    /** Validates the number of fields required by a task type. */
    private void requireFieldCount(String[] fields, int expectedCount, String type) {
        if (fields.length != expectedCount) {
            throw new IllegalArgumentException(type + " records contain the wrong number of fields.");
        }
    }

    /** Validates and returns a required stored value. */
    private String requireValue(String value, String fieldName) {
        String trimmedValue = value.trim();
        if (trimmedValue.isBlank()) {
            throw new IllegalArgumentException("Task " + fieldName + " cannot be empty.");
        }
        return trimmedValue;
    }
}
