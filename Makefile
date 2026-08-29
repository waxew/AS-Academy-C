# AS Academy C - مسیر ساده build برای هنرجویانی که Make را یاد می گیرند.
CC ?= cc
CFLAGS ?= -std=c23 -Wall -Wextra -Wpedantic
BUILD_DIR := build
SOURCES := examples/fundamentals/hello.c examples/beginner/student_records.c examples/advanced/dynamic_array.c examples/professional/bit_flags.c
TARGETS := $(BUILD_DIR)/hello $(BUILD_DIR)/student_records $(BUILD_DIR)/dynamic_array $(BUILD_DIR)/bit_flags

.PHONY: all clean run

all: $(TARGETS)

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

$(BUILD_DIR)/hello: examples/fundamentals/hello.c | $(BUILD_DIR)
	$(CC) $(CFLAGS) $< -o $@

$(BUILD_DIR)/student_records: examples/beginner/student_records.c | $(BUILD_DIR)
	$(CC) $(CFLAGS) $< -o $@

$(BUILD_DIR)/dynamic_array: examples/advanced/dynamic_array.c | $(BUILD_DIR)
	$(CC) $(CFLAGS) $< -o $@

$(BUILD_DIR)/bit_flags: examples/professional/bit_flags.c | $(BUILD_DIR)
	$(CC) $(CFLAGS) $< -o $@

run: all
	$(BUILD_DIR)/hello
	$(BUILD_DIR)/student_records
	$(BUILD_DIR)/dynamic_array
	$(BUILD_DIR)/bit_flags

clean:
	rm -rf $(BUILD_DIR)
