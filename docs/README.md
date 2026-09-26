# Yanny User Guide

Yanny is a command-line task manager for recording Todos, Deadlines, and Events.
It saves changes automatically, so your tasks are available the next time you
start the application from the same directory.

## Getting started

Follow the [build and run instructions](../README.md#building-and-running-the-executable-jar)
to create and start `yanny.jar`. Once Yanny displays
`SYSTEM READY. AWAITING COMMAND...`, type a command and press Enter.

In the syntax examples below, text in angle brackets describes a value that you
must replace. Do not type the angle brackets themselves. Commands and markers
such as `/by`, `/from`, and `/to` are case-insensitive.

## Adding a Todo

Use a Todo for a task that has no date or time.

Syntax: `todo <description>`

Example:

```text
todo borrow a book
```

Yanny adds the task as an incomplete Todo, represented by `[T][ ]`.

## Adding a Deadline

Use a Deadline for a task that must be completed by a particular date or time.
Enter either an ISO date (`yyyy-MM-dd`) or a day/month/year and 24-hour time
(`d/M/yyyy HHmm`). Invalid dates and times are rejected.

Syntax: `deadline <description> /by <yyyy-MM-dd or d/M/yyyy HHmm>`

Example:

```text
deadline submit report /by 2019-10-15
deadline return book /by 2/12/2019 1800
```

Yanny adds the task as an incomplete Deadline, represented by `[D][ ]`, and
displays its date as `Oct 15 2019` or its date and time as
`Dec 02 2019 6:00 PM`. Older deadlines saved as free text still load and
display their original value.

## Adding an Event

Use an Event for an activity with a start and an end. The values after `/from`
and `/to` are stored as entered.

Syntax: `event <description> /from <start> /to <end>`

Example:

```text
event project meeting /from Monday 2pm /to Monday 3pm
```

Yanny adds the task as an incomplete Event, represented by `[E][ ]`, and
displays its start and end values with the task.

## Listing tasks

Use `list` to display every stored task, its completion status, and its current
number.

```text
list
```

An incomplete task contains `[ ]`, while a completed task contains `[X]`. Task
numbers start at 1 and may change when a task is deleted, so run `list` before
using a command that requires a task number.

## Finding tasks

Use `find <keyword>` to search task descriptions. The search ignores letter
case and matches a keyword or phrase anywhere in a description.

```text
find book
```

Matching tasks retain their numbers from the full list, so you can use those
numbers with `mark`, `unmark`, `delete`, or `remove`. If nothing matches, Yanny
shows `NO MATCHING TASKS FOUND`. A blank keyword is rejected.

## Marking a task as completed

Syntax: `mark <number>`

Example:

```text
mark 2
```

Yanny marks task 2 as completed. The task will contain `[X]` when displayed.

## Marking a task as incomplete

Syntax: `unmark <number>`

Example:

```text
unmark 2
```

Yanny marks task 2 as incomplete again. The task will contain `[ ]` when
displayed.

## Deleting a task

Syntax: `delete <number>`

Example:

```text
delete 2
```

Yanny permanently removes task 2 and renumbers the remaining tasks. `remove`
is an alias for `delete`, so `remove 2` has the same effect.

## Exiting Yanny

Enter `bye` to close the application safely.

```text
bye
```

## Saving task data

Yanny automatically loads and saves tasks in `data/yanny.txt`, relative to the
directory from which the application is launched. The `data` directory and
file are created when the first task is saved.

Descriptions and Event start/end values accept plain text, but they cannot
contain the `|` character because Yanny uses it to separate values in the
storage file. New Deadline values must use one of the date formats above.
