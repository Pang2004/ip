#!/usr/bin/env bash
set -euo pipefail

test_data_directory="$(mktemp -d)"
trap 'rm -rf "$test_data_directory"' EXIT
data_file="$test_data_directory/nested/yanny.txt"

printf 'todo read saved task\ndeadline return book /by Sunday\nmark 1\ndelete 2\nbye\n' \
    | java -Dyanny.data.file="$data_file" -cp out yanny.ui.Yanny >/dev/null

loaded_output="$(printf 'list\nbye\n' \
    | java -Dyanny.data.file="$data_file" -cp out yanny.ui.Yanny)"

grep -F '| 1. [T][X] read saved task' <<<"$loaded_output" >/dev/null
if grep -F '| 2. [D][ ] return book (by: Sunday)' <<<"$loaded_output" >/dev/null; then
    printf '%s\n' 'Deleted task was unexpectedly restored after restart.' >&2
    exit 1
fi
printf '%s\n' 'Persistence read test passed.'
