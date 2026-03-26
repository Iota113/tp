# Batch Commands Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Allow mark, unmark, delete, tag add, and tag delete commands to accept multiple space-separated indices in a single command (e.g. `mark 1 2 3`, `delete 1 2 3`, `tag add 1 2 3 t/math`).

**Architecture:** Add `ParserUtil.parseIndices()` to parse space-separated indices with deduplication. Change all five command classes from `Index` to `List<Index>`. Each command validates all indices first, resolves Person objects from the original list, then applies operations. Delete resolves Person objects before removing to avoid index-shift bugs. Single-index usage remains backward-compatible with identical success messages.

**Tech Stack:** Java 17, JUnit 5, Gradle

---

## File Map

| File | Action | Responsibility |
|------|--------|---------------|
| `src/main/java/seedu/address/logic/parser/ParserUtil.java` | Modify | Add `parseIndices()` method |
| `src/main/java/seedu/address/logic/commands/MarkCommand.java` | Modify | Accept `List<Index>`, batch mark |
| `src/main/java/seedu/address/logic/parser/MarkCommandParser.java` | Modify | Use `parseIndices()` |
| `src/main/java/seedu/address/logic/commands/UnmarkCommand.java` | Modify | Accept `List<Index>`, batch unmark |
| `src/main/java/seedu/address/logic/parser/UnmarkCommandParser.java` | Modify | Use `parseIndices()` |
| `src/main/java/seedu/address/logic/commands/DeleteCommand.java` | Modify | Accept `List<Index>`, batch delete |
| `src/main/java/seedu/address/logic/parser/DeleteCommandParser.java` | Modify | Use `parseIndices()` |
| `src/main/java/seedu/address/logic/commands/TagCommand.java` | Modify | Accept `List<Index>` base class |
| `src/main/java/seedu/address/logic/commands/AddTagCommand.java` | Modify | Batch tag add |
| `src/main/java/seedu/address/logic/parser/AddTagCommandParser.java` | Modify | Use `parseIndices()` for preamble |
| `src/main/java/seedu/address/logic/commands/DeleteTagCommand.java` | Modify | Batch tag delete |
| `src/main/java/seedu/address/logic/parser/DeleteTagCommandParser.java` | Modify | Use `parseIndices()` for preamble |
| `src/test/java/seedu/address/logic/parser/ParserUtilTest.java` | Modify | Add `parseIndices` tests |
| `src/test/java/seedu/address/logic/commands/MarkCommandTest.java` | Modify | Update constructors, add batch test |
| `src/test/java/seedu/address/logic/commands/UnmarkCommandTest.java` | Modify | Update constructors, add batch test |
| `src/test/java/seedu/address/logic/commands/DeleteCommandTest.java` | Modify | Update constructors, add batch test |
| `src/test/java/seedu/address/logic/commands/AddTagCommandTest.java` | Modify | Update constructors, add batch test |
| `src/test/java/seedu/address/logic/commands/DeleteTagCommandTest.java` | Modify | Update constructors, add batch test |
| `src/test/java/seedu/address/logic/parser/MarkCommandParserTest.java` | Modify | Update constructors, add batch parse test |
| `src/test/java/seedu/address/logic/parser/UnmarkCommandParserTest.java` | Modify | Update constructors, add batch parse test |
| `src/test/java/seedu/address/logic/parser/DeleteCommandParserTest.java` | Modify | Update constructors, add batch parse test |
| `src/test/java/seedu/address/logic/parser/AddressBookParserTest.java` | Modify | Update constructor calls |
| `src/test/java/seedu/address/logic/parser/TagCommandParserTest.java` | Modify | Update constructor calls |
| `src/test/java/seedu/address/logic/parser/AddTagCommandParserTest.java` | Modify | Update constructor calls |
| `src/test/java/seedu/address/logic/parser/DeleteTagCommandParserTest.java` | Modify | Update constructor calls |

---

### Task 1: Add `parseIndices` to ParserUtil

**Files:**
- Modify: `src/main/java/seedu/address/logic/parser/ParserUtil.java:24-39`
- Test: `src/test/java/seedu/address/logic/parser/ParserUtilTest.java`

- [ ] **Step 1: Write failing tests for parseIndices**

Add these test methods to `ParserUtilTest.java`. Add the required imports at the top of the file:

```java
import java.util.List;
```

Then add these test methods:

```java
@Test
public void parseIndices_null_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> ParserUtil.parseIndices(null));
}

@Test
public void parseIndices_emptyString_throwsParseException() {
    assertThrows(ParseException.class, () -> ParserUtil.parseIndices(""));
    assertThrows(ParseException.class, () -> ParserUtil.parseIndices("  "));
}

@Test
public void parseIndices_invalidValue_throwsParseException() {
    assertThrows(ParseException.class, () -> ParserUtil.parseIndices("a"));
    assertThrows(ParseException.class, () -> ParserUtil.parseIndices("1 a 2"));
    assertThrows(ParseException.class, () -> ParserUtil.parseIndices("0 1"));
    assertThrows(ParseException.class, () -> ParserUtil.parseIndices("-1 2"));
}

@Test
public void parseIndices_singleValidValue_returnsSingletonList() throws Exception {
    List<Index> result = ParserUtil.parseIndices("1");
    assertEquals(List.of(Index.fromOneBased(1)), result);
}

@Test
public void parseIndices_multipleValidValues_returnsList() throws Exception {
    List<Index> result = ParserUtil.parseIndices("1 2 3");
    assertEquals(List.of(Index.fromOneBased(1), Index.fromOneBased(2), Index.fromOneBased(3)), result);
}

@Test
public void parseIndices_duplicateValues_deduplicatesPreservingOrder() throws Exception {
    List<Index> result = ParserUtil.parseIndices("1 2 1 3 2");
    assertEquals(List.of(Index.fromOneBased(1), Index.fromOneBased(2), Index.fromOneBased(3)), result);
}

@Test
public void parseIndices_extraWhitespace_returnsList() throws Exception {
    List<Index> result = ParserUtil.parseIndices("  1   2   3  ");
    assertEquals(List.of(Index.fromOneBased(1), Index.fromOneBased(2), Index.fromOneBased(3)), result);
}
```

- [ ] **Step 2: Run tests to verify they fail**

Run: `./gradlew test --tests "seedu.address.logic.parser.ParserUtilTest"`
Expected: Compilation failure — `parseIndices` method does not exist.

- [ ] **Step 3: Implement parseIndices in ParserUtil**

Add these imports to `ParserUtil.java`:

```java
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
```

Add this method after the existing `parseIndex` method (after line 39):

```java
/**
 * Parses {@code indicesString} containing one or more space-separated one-based indices
 * into a {@code List<Index>}. Duplicates are removed while preserving order.
 * Leading and trailing whitespace is trimmed.
 * @throws ParseException if any index is invalid (not a non-zero unsigned integer).
 */
public static List<Index> parseIndices(String indicesString) throws ParseException {
    requireNonNull(indicesString);
    String trimmed = indicesString.trim();
    if (trimmed.isEmpty()) {
        throw new ParseException(MESSAGE_INVALID_INDEX);
    }
    String[] parts = trimmed.split("\\s+");
    LinkedHashSet<Index> seen = new LinkedHashSet<>();
    for (String part : parts) {
        if (!StringUtil.isNonZeroUnsignedInteger(part)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        seen.add(Index.fromOneBased(Integer.parseInt(part)));
    }
    return new ArrayList<>(seen);
}
```

- [ ] **Step 4: Run tests to verify they pass**

Run: `./gradlew test --tests "seedu.address.logic.parser.ParserUtilTest"`
Expected: All PASS.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/seedu/address/logic/parser/ParserUtil.java src/test/java/seedu/address/logic/parser/ParserUtilTest.java
git commit -m "Add ParserUtil.parseIndices for batch index parsing"
```

---

### Task 2: Batch MarkCommand + MarkCommandParser

**Files:**
- Modify: `src/main/java/seedu/address/logic/commands/MarkCommand.java`
- Modify: `src/main/java/seedu/address/logic/parser/MarkCommandParser.java`
- Modify: `src/test/java/seedu/address/logic/commands/MarkCommandTest.java`
- Modify: `src/test/java/seedu/address/logic/parser/MarkCommandParserTest.java`
- Modify: `src/test/java/seedu/address/logic/parser/AddressBookParserTest.java`

- [ ] **Step 1: Update MarkCommand to accept List\<Index\>**

Replace the full contents of `MarkCommand.java` with:

```java
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Marks one or more students' payment status as paid.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the payment status of the student(s) identified by index number(s)"
            + " in the displayed student list as paid.\n"
            + "Parameters: INDEX [INDEX]... (must be positive integers)\n"
            + "Example: " + COMMAND_WORD + " 1 2 3";

    public static final String MESSAGE_MARK_PERSON_SUCCESS = "Marked student as paid: %1$s";
    public static final String MESSAGE_MARK_PERSONS_SUCCESS = "Marked %1$d students as paid: %2$s";
    public static final String MESSAGE_ALREADY_PAID = "This student has already been marked as paid.";

    private final List<Index> targetIndices;

    /**
     * Creates a MarkCommand to mark the persons at {@code targetIndices} as paid.
     */
    public MarkCommand(List<Index> targetIndices) {
        requireNonNull(targetIndices);
        this.targetIndices = new ArrayList<>(targetIndices);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Validate all indices
        for (Index index : targetIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        // Resolve persons and validate none are already paid
        List<Person> personsToMark = new ArrayList<>();
        for (Index index : targetIndices) {
            Person person = lastShownList.get(index.getZeroBased());
            if (person.isPaid()) {
                throw new CommandException(MESSAGE_ALREADY_PAID);
            }
            personsToMark.add(person);
        }

        // Apply marks
        List<Person> markedPersons = new ArrayList<>();
        for (Person personToMark : personsToMark) {
            Person markedPerson = new Person(
                    personToMark.getName(), personToMark.getPhone(), personToMark.getEmail(),
                    personToMark.getAddress(), personToMark.getDay(), personToMark.getStartTime(),
                    personToMark.getEndTime(), personToMark.getRate(), true, personToMark.getTags());
            model.setPerson(personToMark, markedPerson);
            markedPersons.add(markedPerson);
        }

        if (markedPersons.size() == 1) {
            return new CommandResult(String.format(MESSAGE_MARK_PERSON_SUCCESS,
                    Messages.format(markedPersons.get(0))));
        }
        String names = markedPersons.stream()
                .map(p -> p.getName().toString())
                .collect(Collectors.joining(", "));
        return new CommandResult(String.format(MESSAGE_MARK_PERSONS_SUCCESS, markedPersons.size(), names));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof MarkCommand)) {
            return false;
        }

        MarkCommand otherMarkCommand = (MarkCommand) other;
        return targetIndices.equals(otherMarkCommand.targetIndices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", targetIndices)
                .toString();
    }
}
```

- [ ] **Step 2: Update MarkCommandParser to use parseIndices**

Replace the full contents of `MarkCommandParser.java` with:

```java
package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new MarkCommand object.
 */
public class MarkCommandParser implements Parser<MarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MarkCommand
     * and returns a MarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MarkCommand parse(String args) throws ParseException {
        try {
            List<Index> indices = ParserUtil.parseIndices(args);
            return new MarkCommand(indices);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE), pe);
        }
    }
}
```

- [ ] **Step 3: Update MarkCommandTest to use List.of() constructors and add batch test**

In `MarkCommandTest.java`, add these imports:

```java
import java.util.List;
```

Then replace every `new MarkCommand(INDEX_FIRST_PERSON)` with `new MarkCommand(List.of(INDEX_FIRST_PERSON))` and every `new MarkCommand(INDEX_SECOND_PERSON)` with `new MarkCommand(List.of(INDEX_SECOND_PERSON))`. Also replace `new MarkCommand(outOfBoundIndex)` with `new MarkCommand(List.of(outOfBoundIndex))` and `new MarkCommand(targetIndex)` with `new MarkCommand(List.of(targetIndex))`.

Update the `toStringMethod` test — the field name changed from `targetIndex` to `targetIndices`:

```java
@Test
public void toStringMethod() {
    Index targetIndex = Index.fromOneBased(1);
    MarkCommand markCommand = new MarkCommand(List.of(targetIndex));
    String expected = MarkCommand.class.getCanonicalName() + "{targetIndices=[" + targetIndex + "]}";
    assertEquals(expected, markCommand.toString());
}
```

Add this batch test method:

```java
@Test
public void execute_batchValidIndicesUnfilteredList_success() {
    Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
    Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
    MarkCommand markCommand = new MarkCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));

    Person markedFirst = new PersonBuilder(firstPerson).withPaid(true).build();
    Person markedSecond = new PersonBuilder(secondPerson).withPaid(true).build();

    ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    expectedModel.setPerson(firstPerson, markedFirst);
    expectedModel.setPerson(secondPerson, markedSecond);

    String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_PERSONS_SUCCESS,
            2, markedFirst.getName() + ", " + markedSecond.getName());

    assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
}
```

- [ ] **Step 4: Update MarkCommandParserTest for List.of() and add batch parse test**

In `MarkCommandParserTest.java`, add this import:

```java
import java.util.List;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
```

Replace `new MarkCommand(INDEX_FIRST_PERSON)` with `new MarkCommand(List.of(INDEX_FIRST_PERSON))`.

Add this test method:

```java
@Test
public void parse_multipleValidArgs_returnsMarkCommand() {
    assertParseSuccess(parser, "1 2", new MarkCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON)));
}
```

- [ ] **Step 5: Update AddressBookParserTest mark/unmark constructor calls**

In `AddressBookParserTest.java`, find the line:
```java
assertEquals(new MarkCommand(INDEX_FIRST_PERSON), command);
```
Replace with:
```java
assertEquals(new MarkCommand(List.of(INDEX_FIRST_PERSON)), command);
```

(The unmark line will be updated in Task 3.)

Make sure `java.util.List` is imported. If not already present, add:
```java
import java.util.List;
```

- [ ] **Step 6: Run tests**

Run: `./gradlew test --tests "seedu.address.logic.commands.MarkCommandTest" --tests "seedu.address.logic.parser.MarkCommandParserTest"`
Expected: All PASS.

- [ ] **Step 7: Commit**

```bash
git add src/main/java/seedu/address/logic/commands/MarkCommand.java src/main/java/seedu/address/logic/parser/MarkCommandParser.java src/test/java/seedu/address/logic/commands/MarkCommandTest.java src/test/java/seedu/address/logic/parser/MarkCommandParserTest.java src/test/java/seedu/address/logic/parser/AddressBookParserTest.java
git commit -m "Add batch support to mark command"
```

---

### Task 3: Batch UnmarkCommand + UnmarkCommandParser

**Files:**
- Modify: `src/main/java/seedu/address/logic/commands/UnmarkCommand.java`
- Modify: `src/main/java/seedu/address/logic/parser/UnmarkCommandParser.java`
- Modify: `src/test/java/seedu/address/logic/commands/UnmarkCommandTest.java`
- Modify: `src/test/java/seedu/address/logic/parser/UnmarkCommandParserTest.java`
- Modify: `src/test/java/seedu/address/logic/parser/AddressBookParserTest.java`

- [ ] **Step 1: Update UnmarkCommand to accept List\<Index\>**

Replace the full contents of `UnmarkCommand.java` with:

```java
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Marks one or more students' payment status as unpaid.
 */
public class UnmarkCommand extends Command {

    public static final String COMMAND_WORD = "unmark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the payment status of the student(s) identified by index number(s)"
            + " in the displayed student list as unpaid.\n"
            + "Parameters: INDEX [INDEX]... (must be positive integers)\n"
            + "Example: " + COMMAND_WORD + " 1 2 3";

    public static final String MESSAGE_UNMARK_PERSON_SUCCESS = "Marked student as unpaid: %1$s";
    public static final String MESSAGE_UNMARK_PERSONS_SUCCESS = "Marked %1$d students as unpaid: %2$s";
    public static final String MESSAGE_ALREADY_UNPAID = "This student has already been marked as unpaid.";

    private final List<Index> targetIndices;

    /**
     * Creates an UnmarkCommand to mark the persons at {@code targetIndices} as unpaid.
     */
    public UnmarkCommand(List<Index> targetIndices) {
        requireNonNull(targetIndices);
        this.targetIndices = new ArrayList<>(targetIndices);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Validate all indices
        for (Index index : targetIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        // Resolve persons and validate none are already unpaid
        List<Person> personsToUnmark = new ArrayList<>();
        for (Index index : targetIndices) {
            Person person = lastShownList.get(index.getZeroBased());
            if (!person.isPaid()) {
                throw new CommandException(MESSAGE_ALREADY_UNPAID);
            }
            personsToUnmark.add(person);
        }

        // Apply unmarks
        List<Person> unmarkedPersons = new ArrayList<>();
        for (Person personToUnmark : personsToUnmark) {
            Person unmarkedPerson = new Person(
                    personToUnmark.getName(), personToUnmark.getPhone(), personToUnmark.getEmail(),
                    personToUnmark.getAddress(), personToUnmark.getDay(), personToUnmark.getStartTime(),
                    personToUnmark.getEndTime(), personToUnmark.getRate(), false, personToUnmark.getTags());
            model.setPerson(personToUnmark, unmarkedPerson);
            unmarkedPersons.add(unmarkedPerson);
        }

        if (unmarkedPersons.size() == 1) {
            return new CommandResult(String.format(MESSAGE_UNMARK_PERSON_SUCCESS,
                    Messages.format(unmarkedPersons.get(0))));
        }
        String names = unmarkedPersons.stream()
                .map(p -> p.getName().toString())
                .collect(Collectors.joining(", "));
        return new CommandResult(String.format(MESSAGE_UNMARK_PERSONS_SUCCESS, unmarkedPersons.size(), names));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnmarkCommand)) {
            return false;
        }

        UnmarkCommand otherUnmarkCommand = (UnmarkCommand) other;
        return targetIndices.equals(otherUnmarkCommand.targetIndices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", targetIndices)
                .toString();
    }
}
```

- [ ] **Step 2: Update UnmarkCommandParser to use parseIndices**

Replace the full contents of `UnmarkCommandParser.java` with:

```java
package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnmarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnmarkCommand object.
 */
public class UnmarkCommandParser implements Parser<UnmarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnmarkCommand
     * and returns an UnmarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UnmarkCommand parse(String args) throws ParseException {
        try {
            List<Index> indices = ParserUtil.parseIndices(args);
            return new UnmarkCommand(indices);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE), pe);
        }
    }
}
```

- [ ] **Step 3: Update UnmarkCommandTest**

In `UnmarkCommandTest.java`, add this import:

```java
import java.util.List;
```

Replace every `new UnmarkCommand(INDEX_FIRST_PERSON)` with `new UnmarkCommand(List.of(INDEX_FIRST_PERSON))`, every `new UnmarkCommand(INDEX_SECOND_PERSON)` with `new UnmarkCommand(List.of(INDEX_SECOND_PERSON))`, and every `new UnmarkCommand(outOfBoundIndex)` / `new UnmarkCommand(targetIndex)` with the List.of() wrapper.

Update the `toStringMethod` test:

```java
@Test
public void toStringMethod() {
    Index targetIndex = Index.fromOneBased(1);
    UnmarkCommand unmarkCommand = new UnmarkCommand(List.of(targetIndex));
    String expected = UnmarkCommand.class.getCanonicalName() + "{targetIndices=[" + targetIndex + "]}";
    assertEquals(expected, unmarkCommand.toString());
}
```

Add a batch test:

```java
@Test
public void execute_batchValidIndicesUnfilteredList_success() {
    // Set both persons as paid first
    Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
    Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
    Person paidFirst = new PersonBuilder(firstPerson).withPaid(true).build();
    Person paidSecond = new PersonBuilder(secondPerson).withPaid(true).build();
    model.setPerson(firstPerson, paidFirst);
    model.setPerson(secondPerson, paidSecond);

    UnmarkCommand unmarkCommand = new UnmarkCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));

    Person unmarkedFirst = new PersonBuilder(paidFirst).withPaid(false).build();
    Person unmarkedSecond = new PersonBuilder(paidSecond).withPaid(false).build();

    ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    expectedModel.setPerson(paidFirst, unmarkedFirst);
    expectedModel.setPerson(paidSecond, unmarkedSecond);

    String expectedMessage = String.format(UnmarkCommand.MESSAGE_UNMARK_PERSONS_SUCCESS,
            2, unmarkedFirst.getName() + ", " + unmarkedSecond.getName());

    assertCommandSuccess(unmarkCommand, model, expectedMessage, expectedModel);
}
```

- [ ] **Step 4: Update UnmarkCommandParserTest**

In `UnmarkCommandParserTest.java`, add imports:

```java
import java.util.List;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
```

Replace `new UnmarkCommand(INDEX_FIRST_PERSON)` with `new UnmarkCommand(List.of(INDEX_FIRST_PERSON))`.

Add this test:

```java
@Test
public void parse_multipleValidArgs_returnsUnmarkCommand() {
    assertParseSuccess(parser, "1 2", new UnmarkCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON)));
}
```

- [ ] **Step 5: Update AddressBookParserTest unmark line**

In `AddressBookParserTest.java`, find:
```java
assertEquals(new UnmarkCommand(INDEX_FIRST_PERSON), command);
```
Replace with:
```java
assertEquals(new UnmarkCommand(List.of(INDEX_FIRST_PERSON)), command);
```

- [ ] **Step 6: Run tests**

Run: `./gradlew test --tests "seedu.address.logic.commands.UnmarkCommandTest" --tests "seedu.address.logic.parser.UnmarkCommandParserTest"`
Expected: All PASS.

- [ ] **Step 7: Commit**

```bash
git add src/main/java/seedu/address/logic/commands/UnmarkCommand.java src/main/java/seedu/address/logic/parser/UnmarkCommandParser.java src/test/java/seedu/address/logic/commands/UnmarkCommandTest.java src/test/java/seedu/address/logic/parser/UnmarkCommandParserTest.java src/test/java/seedu/address/logic/parser/AddressBookParserTest.java
git commit -m "Add batch support to unmark command"
```

---

### Task 4: Batch DeleteCommand + DeleteCommandParser

**Files:**
- Modify: `src/main/java/seedu/address/logic/commands/DeleteCommand.java`
- Modify: `src/main/java/seedu/address/logic/parser/DeleteCommandParser.java`
- Modify: `src/test/java/seedu/address/logic/commands/DeleteCommandTest.java`
- Modify: `src/test/java/seedu/address/logic/parser/DeleteCommandParserTest.java`
- Modify: `src/test/java/seedu/address/logic/parser/AddressBookParserTest.java`

- [ ] **Step 1: Update DeleteCommand to accept List\<Index\>**

Replace the full contents of `DeleteCommand.java` with:

```java
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes one or more persons identified using their displayed indices from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person(s) identified by the index number(s) used in the displayed person list.\n"
            + "Parameters: INDEX [INDEX]... (must be positive integers)\n"
            + "Example: " + COMMAND_WORD + " 1 2 3";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String MESSAGE_DELETE_PERSONS_SUCCESS = "Deleted %1$d persons: %2$s";

    private final List<Index> targetIndices;

    /**
     * Creates a DeleteCommand to delete persons at {@code targetIndices}.
     */
    public DeleteCommand(List<Index> targetIndices) {
        requireNonNull(targetIndices);
        this.targetIndices = new ArrayList<>(targetIndices);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Validate all indices
        for (Index index : targetIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        // Resolve all persons from original list before any deletions
        List<Person> personsToDelete = new ArrayList<>();
        for (Index index : targetIndices) {
            personsToDelete.add(lastShownList.get(index.getZeroBased()));
        }

        // Delete all resolved persons
        for (Person person : personsToDelete) {
            model.deletePerson(person);
        }

        if (personsToDelete.size() == 1) {
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS,
                    Messages.format(personsToDelete.get(0))));
        }
        String names = personsToDelete.stream()
                .map(p -> p.getName().toString())
                .collect(Collectors.joining(", "));
        return new CommandResult(String.format(MESSAGE_DELETE_PERSONS_SUCCESS, personsToDelete.size(), names));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndices.equals(otherDeleteCommand.targetIndices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", targetIndices)
                .toString();
    }
}
```

- [ ] **Step 2: Update DeleteCommandParser to use parseIndices**

Replace the full contents of `DeleteCommandParser.java` with:

```java
package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object.
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        try {
            List<Index> indices = ParserUtil.parseIndices(args);
            return new DeleteCommand(indices);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
        }
    }
}
```

- [ ] **Step 3: Update DeleteCommandTest**

In `DeleteCommandTest.java`, add this import:

```java
import java.util.List;
```

Replace every `new DeleteCommand(INDEX_FIRST_PERSON)` with `new DeleteCommand(List.of(INDEX_FIRST_PERSON))`, every `new DeleteCommand(INDEX_SECOND_PERSON)` with `new DeleteCommand(List.of(INDEX_SECOND_PERSON))`, and every `new DeleteCommand(outOfBoundIndex)` / `new DeleteCommand(targetIndex)` with the List.of() wrapper.

Update the `toStringMethod` test:

```java
@Test
public void toStringMethod() {
    Index targetIndex = Index.fromOneBased(1);
    DeleteCommand deleteCommand = new DeleteCommand(List.of(targetIndex));
    String expected = DeleteCommand.class.getCanonicalName() + "{targetIndices=[" + targetIndex + "]}";
    assertEquals(expected, deleteCommand.toString());
}
```

Add a batch test:

```java
@Test
public void execute_batchValidIndicesUnfilteredList_success() {
    Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
    Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
    DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));

    ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    expectedModel.deletePerson(firstPerson);
    expectedModel.deletePerson(secondPerson);

    String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSONS_SUCCESS,
            2, firstPerson.getName() + ", " + secondPerson.getName());

    assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
}
```

- [ ] **Step 4: Update DeleteCommandParserTest**

In `DeleteCommandParserTest.java`, add imports:

```java
import java.util.List;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
```

Replace `new DeleteCommand(INDEX_FIRST_PERSON)` with `new DeleteCommand(List.of(INDEX_FIRST_PERSON))`.

Add this test:

```java
@Test
public void parse_multipleValidArgs_returnsDeleteCommand() {
    assertParseSuccess(parser, "1 2", new DeleteCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON)));
}
```

- [ ] **Step 5: Update AddressBookParserTest delete line**

In `AddressBookParserTest.java`, find:
```java
assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
```
Replace with:
```java
assertEquals(new DeleteCommand(List.of(INDEX_FIRST_PERSON)), command);
```

- [ ] **Step 6: Run tests**

Run: `./gradlew test --tests "seedu.address.logic.commands.DeleteCommandTest" --tests "seedu.address.logic.parser.DeleteCommandParserTest"`
Expected: All PASS.

- [ ] **Step 7: Commit**

```bash
git add src/main/java/seedu/address/logic/commands/DeleteCommand.java src/main/java/seedu/address/logic/parser/DeleteCommandParser.java src/test/java/seedu/address/logic/commands/DeleteCommandTest.java src/test/java/seedu/address/logic/parser/DeleteCommandParserTest.java src/test/java/seedu/address/logic/parser/AddressBookParserTest.java
git commit -m "Add batch support to delete command"
```

---

### Task 5: Batch TagCommand base + AddTagCommand + DeleteTagCommand

**Files:**
- Modify: `src/main/java/seedu/address/logic/commands/TagCommand.java`
- Modify: `src/main/java/seedu/address/logic/commands/AddTagCommand.java`
- Modify: `src/main/java/seedu/address/logic/commands/DeleteTagCommand.java`
- Modify: `src/main/java/seedu/address/logic/parser/AddTagCommandParser.java`
- Modify: `src/main/java/seedu/address/logic/parser/DeleteTagCommandParser.java`
- Modify: `src/test/java/seedu/address/logic/commands/AddTagCommandTest.java`
- Modify: `src/test/java/seedu/address/logic/commands/DeleteTagCommandTest.java`
- Modify: `src/test/java/seedu/address/logic/parser/AddressBookParserTest.java`
- Modify: `src/test/java/seedu/address/logic/parser/TagCommandParserTest.java`
- Modify: `src/test/java/seedu/address/logic/parser/AddTagCommandParserTest.java`
- Modify: `src/test/java/seedu/address/logic/parser/DeleteTagCommandParserTest.java`

- [ ] **Step 1: Update TagCommand base class**

Replace the full contents of `TagCommand.java` with:

```java
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Base class for tag subcommands.
 */
public abstract class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    private final List<Index> targetIndices;
    private final Set<Tag> tags;

    protected TagCommand(List<Index> targetIndices, Set<Tag> tags) {
        requireNonNull(targetIndices);
        requireNonNull(tags);
        this.targetIndices = new ArrayList<>(targetIndices);
        this.tags = new HashSet<>(tags);
    }

    protected List<Person> getTargetPersons(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        List<Person> targetPersons = new ArrayList<>();

        for (Index index : targetIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            targetPersons.add(lastShownList.get(index.getZeroBased()));
        }

        return targetPersons;
    }

    protected List<Index> getTargetIndices() {
        return new ArrayList<>(targetIndices);
    }

    protected Set<Tag> getTags() {
        return new HashSet<>(tags);
    }
}
```

- [ ] **Step 2: Update AddTagCommand for batch**

Replace the full contents of `AddTagCommand.java` with:

```java
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Adds tags to one or more existing persons in the address book.
 */
public class AddTagCommand extends TagCommand {
    public static final String SUBCOMMAND_WORD = "add";
    public static final String COMMAND_PHRASE = TagCommand.COMMAND_WORD + " " + SUBCOMMAND_WORD;

    public static final String MESSAGE_USAGE = COMMAND_PHRASE + ": Adds tag(s) to person(s) in the address book. "
            + "Parameters: "
            + "INDEX [INDEX]... (must be positive integers) "
            + PREFIX_TAG + "TAG (must be a non-empty string)\n"
            + "Example: " + COMMAND_PHRASE + " "
            + "1 2 "
            + PREFIX_TAG + "Primary1 "
            + PREFIX_TAG + "Mathematics";

    public static final String MESSAGE_SUCCESS = "Tag(s) added to person: %1$s";
    public static final String MESSAGE_BATCH_SUCCESS = "Tag(s) added to %1$d persons: %2$s";
    public static final String MESSAGE_TAG_ALREADY_EXISTS = "One or more specified tags already exist for this person.";

    public AddTagCommand(List<Index> targetIndices, Set<Tag> tagsToAdd) {
        super(targetIndices, tagsToAdd);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> personsToTag = getTargetPersons(model);

        // Validate no person already has any of the tags
        for (Person person : personsToTag) {
            if (person.getTags().stream().anyMatch(tag -> getTags().contains(tag))) {
                throw new CommandException(MESSAGE_TAG_ALREADY_EXISTS);
            }
        }

        // Apply tags
        for (Person person : personsToTag) {
            model.addTagsToPerson(person, getTags());
        }

        if (personsToTag.size() == 1) {
            return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(personsToTag.get(0))));
        }
        String names = personsToTag.stream()
                .map(p -> p.getName().toString())
                .collect(Collectors.joining(", "));
        return new CommandResult(String.format(MESSAGE_BATCH_SUCCESS, personsToTag.size(), names));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddTagCommand)) {
            return false;
        }

        AddTagCommand otherTagCommand = (AddTagCommand) other;
        return getTargetIndices().equals(otherTagCommand.getTargetIndices())
            && getTags().equals(otherTagCommand.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTargetIndices(), getTags());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("targetIndices", getTargetIndices())
            .add("tagsToAdd", getTags())
            .toString();
    }
}
```

- [ ] **Step 3: Update DeleteTagCommand for batch**

Replace the full contents of `DeleteTagCommand.java` with:

```java
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Deletes tags from one or more existing persons in the address book.
 */
public class DeleteTagCommand extends TagCommand {

    public static final String SUBCOMMAND_WORD = "delete";
    public static final String COMMAND_PHRASE = TagCommand.COMMAND_WORD + " " + SUBCOMMAND_WORD;

    public static final String MESSAGE_USAGE = COMMAND_PHRASE
            + ": Deletes tag(s) from person(s) in the address book. "
            + "Parameters: "
            + "INDEX [INDEX]... (must be positive integers) "
            + PREFIX_TAG + "TAG (must be a non-empty string)\n"
            + "Example: " + COMMAND_PHRASE + " "
            + "1 2 "
            + PREFIX_TAG + "Primary1 "
            + PREFIX_TAG + "Mathematics";

    public static final String MESSAGE_SUCCESS = "Tag(s) removed from person: %1$s";
    public static final String MESSAGE_BATCH_SUCCESS = "Tag(s) removed from %1$d persons: %2$s";
    public static final String MESSAGE_TAG_NOT_FOUND = "One or more specified tags do not exist for this person.";

    public DeleteTagCommand(List<Index> targetIndices, Set<Tag> tagsToDelete) {
        super(targetIndices, tagsToDelete);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> personsToUpdate = getTargetPersons(model);

        // Validate all persons have the tags
        for (Person person : personsToUpdate) {
            if (!person.getTags().containsAll(getTags())) {
                throw new CommandException(MESSAGE_TAG_NOT_FOUND);
            }
        }

        // Remove tags
        for (Person person : personsToUpdate) {
            model.deleteTagsFromPerson(person, getTags());
        }

        if (personsToUpdate.size() == 1) {
            return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(personsToUpdate.get(0))));
        }
        String names = personsToUpdate.stream()
                .map(p -> p.getName().toString())
                .collect(Collectors.joining(", "));
        return new CommandResult(String.format(MESSAGE_BATCH_SUCCESS, personsToUpdate.size(), names));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteTagCommand)) {
            return false;
        }

        DeleteTagCommand otherDeleteTagCommand = (DeleteTagCommand) other;
        return getTargetIndices().equals(otherDeleteTagCommand.getTargetIndices())
            && getTags().equals(otherDeleteTagCommand.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTargetIndices(), getTags());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("targetIndices", getTargetIndices())
            .add("tagsToDelete", getTags())
            .toString();
    }
}
```

- [ ] **Step 4: Update AddTagCommandParser to use parseIndices**

Replace the full contents of `AddTagCommandParser.java` with:

```java
package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddTagCommand object.
 */
public class AddTagCommandParser implements Parser<AddTagCommand> {

    @Override
    public AddTagCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        if (argMultimap.getPreamble().isEmpty() || argMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE));
        }

        try {
            List<Index> indices = ParserUtil.parseIndices(argMultimap.getPreamble());
            Set<Tag> tags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            return new AddTagCommand(indices, tags);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE), pe);
        }
    }
}
```

- [ ] **Step 5: Update DeleteTagCommandParser to use parseIndices**

Replace the full contents of `DeleteTagCommandParser.java` with:

```java
package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new DeleteTagCommand object.
 */
public class DeleteTagCommandParser implements Parser<DeleteTagCommand> {

    @Override
    public DeleteTagCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        if (argMultimap.getPreamble().isEmpty() || argMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteTagCommand.MESSAGE_USAGE));
        }

        try {
            List<Index> indices = ParserUtil.parseIndices(argMultimap.getPreamble());
            Set<Tag> tags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            return new DeleteTagCommand(indices, tags);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteTagCommand.MESSAGE_USAGE), pe);
        }
    }
}
```

- [ ] **Step 6: Update all tag test files**

**AddTagCommandTest.java:** Add `import java.util.List;`. Replace every `new AddTagCommand(INDEX_FIRST_PERSON, ...)` with `new AddTagCommand(List.of(INDEX_FIRST_PERSON), ...)` and `new AddTagCommand(INDEX_SECOND_PERSON, ...)` with `new AddTagCommand(List.of(INDEX_SECOND_PERSON), ...)`. Same for `outOfBoundIndex`. Update the `toStringMethod` test field name from `targetIndex` to `targetIndices`:

```java
@Test
public void toStringMethod() {
    Set<Tag> tagsToAdd = Set.of(new Tag("newTag"));
    AddTagCommand addTagCommand = new AddTagCommand(List.of(INDEX_FIRST_PERSON), tagsToAdd);
    String expected = AddTagCommand.class.getCanonicalName()
            + "{targetIndices=[" + INDEX_FIRST_PERSON + "], tagsToAdd=" + tagsToAdd + "}";
    assertEquals(expected, addTagCommand.toString());
}
```

**DeleteTagCommandTest.java:** Same pattern — add `import java.util.List;` and wrap all `Index` arguments in `List.of()`. Update the `toStringMethod` field name from `targetIndex` to `targetIndices`:

```java
@Test
public void toStringMethod() {
    Set<Tag> tagsToDelete = Set.of(new Tag("friends"));
    DeleteTagCommand deleteTagCommand = new DeleteTagCommand(List.of(INDEX_FIRST_PERSON), tagsToDelete);
    String expected = DeleteTagCommand.class.getCanonicalName()
            + "{targetIndices=[" + INDEX_FIRST_PERSON + "], tagsToDelete=" + tagsToDelete + "}";
    assertEquals(expected, deleteTagCommand.toString());
}
```

**AddressBookParserTest.java:** Find:
```java
assertEquals(new AddTagCommand(INDEX_FIRST_PERSON, java.util.Set.of(new Tag("friends"))), addTagCommand);
```
Replace with:
```java
assertEquals(new AddTagCommand(List.of(INDEX_FIRST_PERSON), java.util.Set.of(new Tag("friends"))), addTagCommand);
```

Find:
```java
assertEquals(new DeleteTagCommand(INDEX_FIRST_PERSON, java.util.Set.of(new Tag("friends"))),
```
Replace with:
```java
assertEquals(new DeleteTagCommand(List.of(INDEX_FIRST_PERSON), java.util.Set.of(new Tag("friends"))),
```

**TagCommandParserTest.java:** Add `import java.util.List;`. Find:
```java
AddTagCommand expectedCommand = new AddTagCommand(INDEX_FIRST_PERSON, Set.of(new Tag("friend")));
```
Replace with:
```java
AddTagCommand expectedCommand = new AddTagCommand(List.of(INDEX_FIRST_PERSON), Set.of(new Tag("friend")));
```

Find:
```java
DeleteTagCommand expectedCommand = new DeleteTagCommand(INDEX_FIRST_PERSON, Set.of(new Tag("friend")));
```
Replace with:
```java
DeleteTagCommand expectedCommand = new DeleteTagCommand(List.of(INDEX_FIRST_PERSON), Set.of(new Tag("friend")));
```

**AddTagCommandParserTest.java:** Add `import java.util.List;`. Replace:
```java
AddTagCommand expectedCommand = new AddTagCommand(INDEX_FIRST_PERSON, tags);
```
With:
```java
AddTagCommand expectedCommand = new AddTagCommand(List.of(INDEX_FIRST_PERSON), tags);
```

**DeleteTagCommandParserTest.java:** Add `import java.util.List;`. Replace:
```java
DeleteTagCommand expectedCommand = new DeleteTagCommand(INDEX_FIRST_PERSON, tags);
```
With:
```java
DeleteTagCommand expectedCommand = new DeleteTagCommand(List.of(INDEX_FIRST_PERSON), tags);
```

- [ ] **Step 7: Run all tests**

Run: `./gradlew test`
Expected: All PASS.

- [ ] **Step 8: Commit**

```bash
git add src/main/java/seedu/address/logic/commands/TagCommand.java src/main/java/seedu/address/logic/commands/AddTagCommand.java src/main/java/seedu/address/logic/commands/DeleteTagCommand.java src/main/java/seedu/address/logic/parser/AddTagCommandParser.java src/main/java/seedu/address/logic/parser/DeleteTagCommandParser.java src/test/java/seedu/address/logic/commands/AddTagCommandTest.java src/test/java/seedu/address/logic/commands/DeleteTagCommandTest.java src/test/java/seedu/address/logic/parser/AddressBookParserTest.java src/test/java/seedu/address/logic/parser/TagCommandParserTest.java src/test/java/seedu/address/logic/parser/AddTagCommandParserTest.java src/test/java/seedu/address/logic/parser/DeleteTagCommandParserTest.java
git commit -m "Add batch support to tag add and tag delete commands"
```

---

### Task 6: Full build verification

- [ ] **Step 1: Run full build with checkstyle**

Run: `./gradlew check`
Expected: BUILD SUCCESSFUL with no checkstyle violations.

- [ ] **Step 2: Fix any checkstyle issues**

If checkstyle reports line length or import order issues, fix them. Common issues:
- Lines > 120 characters: break the line
- Import ordering: static imports first, then `java.*`, then `org.*`, then `com.*`, alphabetical within groups

- [ ] **Step 3: Run full build again if fixes were needed**

Run: `./gradlew check`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 4: Final commit if checkstyle fixes were needed**

```bash
git add -A
git commit -m "Fix checkstyle issues in batch commands"
```
