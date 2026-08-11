JAVAC = javac
JAVA = java
SRC_MAIN = src/main/java
SRC_TEST = src/test/java
BUILD = build

.PHONY: all compile test run clean

all: compile

compile:
	mkdir -p $(BUILD)
	$(JAVAC) -d $(BUILD) $(shell find $(SRC_MAIN) -name "*.java")

run: compile
	$(JAVA) -cp $(BUILD) Main

test: compile
	$(JAVAC) -cp $(BUILD) -d $(BUILD) $(shell find $(SRC_TEST) -name "*.java")
	for clase in $(shell find $(SRC_TEST) -name "*.java" -exec basename {} .java \;); do \
		echo "--- Ejecutando $$clase ---"; \
		$(JAVA) -cp $(BUILD) $$clase; \
	done

clean:
	rm -rf $(BUILD)
