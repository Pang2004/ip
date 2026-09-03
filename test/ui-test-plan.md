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

## Test case: Reject incomplete and unknown commands

### Aim

Verify that an empty todo description and an unrecognized command are rejected
without adding tasks.

### Inputs

```text
todo
blah
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
| INPUT  > todo
| YANNY_OS :: COMMAND REJECTED
| ERROR > TODO DESCRIPTION CANNOT BE EMPTY. USE: TODO <DESCRIPTION>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > blah
| YANNY_OS :: COMMAND REJECTED
| ERROR > UNKNOWN COMMAND DETECTED. USE: TODO, DEADLINE, EVENT, LIST, MARK, UNMARK, OR BYE
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: TASK LIST
| OUTPUT > NO TASKS STORED
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```

## Test case: Reject an empty command

### Aim

Verify that a blank input line is rejected with guidance and that Yanny continues running.

### Inputs

```text

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
| INPUT  >
| YANNY_OS :: COMMAND REJECTED
| ERROR > COMMAND CANNOT BE EMPTY. ENTER A SUPPORTED COMMAND.
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```

## Test case: Reject invalid deadline and event commands

### Aim

Verify that missing, blank, and incomplete deadline and event values produce specific correction messages.

### Inputs

```text
deadline buy milk
deadline /by tomorrow
deadline buy milk /by
event meeting
event meeting /from Monday
event /from Monday /to Tuesday
event meeting /from /to Tuesday
event meeting /from Monday /to
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
| INPUT  > deadline buy milk
| YANNY_OS :: COMMAND REJECTED
| ERROR > DEADLINE COMMAND REQUIRES: DEADLINE <DESCRIPTION> /BY <DATE OR TIME>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > deadline /by tomorrow
| YANNY_OS :: COMMAND REJECTED
| ERROR > DEADLINE DESCRIPTION CANNOT BE EMPTY. USE: DEADLINE <DESCRIPTION> /BY <DATE OR TIME>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > deadline buy milk /by
| YANNY_OS :: COMMAND REJECTED
| ERROR > DEADLINE /BY VALUE CANNOT BE EMPTY. USE: DEADLINE <DESCRIPTION> /BY <DATE OR TIME>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > event meeting
| YANNY_OS :: COMMAND REJECTED
| ERROR > EVENT COMMAND REQUIRES: EVENT <DESCRIPTION> /FROM <START> /TO <END>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > event meeting /from Monday
| YANNY_OS :: COMMAND REJECTED
| ERROR > EVENT COMMAND REQUIRES: EVENT <DESCRIPTION> /FROM <START> /TO <END>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > event /from Monday /to Tuesday
| YANNY_OS :: COMMAND REJECTED
| ERROR > EVENT DESCRIPTION CANNOT BE EMPTY. USE: EVENT <DESCRIPTION> /FROM <START> /TO <END>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > event meeting /from /to Tuesday
| YANNY_OS :: COMMAND REJECTED
| ERROR > EVENT /FROM VALUE CANNOT BE EMPTY. USE: EVENT <DESCRIPTION> /FROM <START> /TO <END>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > event meeting /from Monday /to
| YANNY_OS :: COMMAND REJECTED
| ERROR > EVENT /TO VALUE CANNOT BE EMPTY. USE: EVENT <DESCRIPTION> /FROM <START> /TO <END>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```

## Test case: Reject invalid mark and unmark commands

### Aim

Verify that missing, non-numeric, non-positive, and out-of-range task numbers are rejected while valid commands still work.

### Inputs

```text
mark
unmark
mark abc
unmark abc
mark 0
unmark 0
mark 1
todo buy milk
mark 2
unmark 2
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
| YANNY_OS :: COMMAND REJECTED
| ERROR > MARK COMMAND REQUIRES A TASK NUMBER. USE: MARK <NUMBER>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > UNMARK COMMAND REQUIRES A TASK NUMBER. USE: UNMARK <NUMBER>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > MARK TASK NUMBER MUST BE AN INTEGER. USE: MARK <NUMBER>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > UNMARK TASK NUMBER MUST BE AN INTEGER. USE: UNMARK <NUMBER>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > MARK TASK NUMBER MUST BE POSITIVE. USE: MARK <NUMBER>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > UNMARK TASK NUMBER MUST BE POSITIVE. USE: UNMARK <NUMBER>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > NO TASKS AVAILABLE. ADD A TASK BEFORE USING MARK.
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > todo buy milk
| OUTPUT > ADDED: [T][ ] buy milk
| OUTPUT > CURRENT TASK COUNT: 1
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > TASK NUMBER OUT OF RANGE. USE A NUMBER FROM 1 TO 1.
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > TASK NUMBER OUT OF RANGE. USE A NUMBER FROM 1 TO 1.
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: MARKED TASK SUCCESSFULLY
| OUTPUT > [X] buy milk
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: UNMARKED TASK SUCCESFULLY
| OUTPUT > [ ] buy milk
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: TASK LIST
| 1. [T][ ] buy milk
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```
