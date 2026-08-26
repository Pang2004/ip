# Yanny UI Test Plan

Run these tests from the repository root after compiling with Java 25:

```text
mkdir -p out
javac -d out src/main/java/yanny/*.java
python3 .codex/skills/test-ui/scripts/run_ui_tests.py test/ui-test-plan.md
```

Each case runs in a fresh Yanny process. Expected output records stdout only;
the shell prompt and terminal echo are intentionally excluded.

## Test case: Add and list a Todo

### Aim

Verify that a Todo is added, counted, and displayed in the task list.

### Inputs

```text
todo borrow book
list
bye
```

### Expected output

```text
+------------------------------------------+
| YANNY_OS :: BOOT SEQUENCE COMPLETE
____    ____  ___      .__   __. .__   __. ____    ____
\   \  /   / /   \     |  \ |  | |  \ |  | \   \  /   /
 \   \/   / /  ^  \    |   \|  | |   \|  |  \   \/   /
  \_    _/ /  /_\  \   |  . `  | |  . `  |   \_    _/
    |  |  /  _____  \  |  |\   | |  |\   |     |  |
    |__| /__/     \__\ |__| \__| |__| \__|     |__|

| GREETINGS I'M YANNY.
| SYSTEM READY. AWAITING COMMAND...
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > todo borrow book
| OUTPUT > ADDED: [T][ ] borrow book
| OUTPUT > CURRENT TASK COUNT: 1
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: TASK LIST
| 1. [T][ ] borrow book
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```

## Test case: Mark and unmark a Todo

### Aim

Verify that the existing completion commands update the Todo status and that
the final list shows the task as incomplete after unmarking it.

### Inputs

```text
todo return book
mark 1
unmark 1
list
bye
```

### Expected output

```text
+------------------------------------------+
| YANNY_OS :: BOOT SEQUENCE COMPLETE
____    ____  ___      .__   __. .__   __. ____    ____
\   \  /   / /   \     |  \ |  | |  \ |  | \   \  /   /
 \   \/   / /  ^  \    |   \|  | |   \|  |  \   \/   /
  \_    _/ /  /_\  \   |  . `  | |  . `  |   \_    _/
    |  |  /  _____  \  |  |\   | |  |\   |     |  |
    |__| /__/     \__\ |__| \__| |__| \__|     |__|

| GREETINGS I'M YANNY.
| SYSTEM READY. AWAITING COMMAND...
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > todo return book
| OUTPUT > ADDED: [T][ ] return book
| OUTPUT > CURRENT TASK COUNT: 1
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: MARKED TASK SUCCESSFULLY
| OUTPUT > [X] return book
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: UNMARKED TASK SUCCESFULLY
| OUTPUT > [ ] return book
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: TASK LIST
| 1. [T][ ] return book
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```

## Test case: Add deadline and event tasks

### Aim

Verify that deadline and event commands preserve date/time values containing
spaces and display their type-specific details.

### Inputs

```text
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

### Expected output

```text
+------------------------------------------+
| YANNY_OS :: BOOT SEQUENCE COMPLETE
____    ____  ___      .__   __. .__   __. ____    ____
\   \  /   / /   \     |  \ |  | |  \ |  | \   \  /   /
 \   \/   / /  ^  \    |   \|  | |   \|  |  \   \/   /
  \_    _/ /  /_\  \   |  . `  | |  . `  |   \_    _/
    |  |  /  _____  \  |  |\   | |  |\   |     |  |
    |__| /__/     \__\ |__| \__| |__| \__|     |__|

| GREETINGS I'M YANNY.
| SYSTEM READY. AWAITING COMMAND...
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > deadline return book /by Sunday
| OUTPUT > ADDED: [D][ ] return book (by: Sunday)
| OUTPUT > CURRENT TASK COUNT: 1
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > event project meeting /from Mon 2pm /to 4pm
| OUTPUT > ADDED: [E][ ] project meeting (from: Mon 2pm to: 4pm)
| OUTPUT > CURRENT TASK COUNT: 2
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: TASK LIST
| 1. [D][ ] return book (by: Sunday)
| 2. [E][ ] project meeting (from: Mon 2pm to: 4pm)
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```
