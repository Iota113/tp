package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code MarkCommand}.
 */
public class MarkCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToMark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        MarkCommand markCommand = new MarkCommand(List.of(INDEX_FIRST_PERSON));

        Person markedPerson = new PersonBuilder(personToMark).withPaid(true).build();
        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_PERSON_SUCCESS,
                Messages.format(markedPerson));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToMark, markedPerson);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        MarkCommand markCommand = new MarkCommand(List.of(outOfBoundIndex));

        assertCommandFailure(markCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToMark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        MarkCommand markCommand = new MarkCommand(List.of(INDEX_FIRST_PERSON));

        Person markedPerson = new PersonBuilder(personToMark).withPaid(true).build();
        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_PERSON_SUCCESS,
                Messages.format(markedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToMark, markedPerson);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        MarkCommand markCommand = new MarkCommand(List.of(outOfBoundIndex));

        assertCommandFailure(markCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_alreadyPaid_throwsCommandException() {
        Person personToMark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person alreadyPaidPerson = new PersonBuilder(personToMark).withPaid(true).build();
        model.setPerson(personToMark, alreadyPaidPerson);

        MarkCommand markCommand = new MarkCommand(List.of(INDEX_FIRST_PERSON));

        assertCommandFailure(markCommand, model, MarkCommand.MESSAGE_ALREADY_PAID);
    }

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

    @Test
    public void equals() {
        MarkCommand markFirstCommand = new MarkCommand(List.of(INDEX_FIRST_PERSON));
        MarkCommand markSecondCommand = new MarkCommand(List.of(INDEX_SECOND_PERSON));

        // same object -> returns true
        assertTrue(markFirstCommand.equals(markFirstCommand));

        // same values -> returns true
        MarkCommand markFirstCommandCopy = new MarkCommand(List.of(INDEX_FIRST_PERSON));
        assertTrue(markFirstCommand.equals(markFirstCommandCopy));

        // different types -> returns false
        assertFalse(markFirstCommand.equals(1));

        // null -> returns false
        assertFalse(markFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(markFirstCommand.equals(markSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        MarkCommand markCommand = new MarkCommand(List.of(targetIndex));
        String expected = MarkCommand.class.getCanonicalName() + "{targetIndices=[" + targetIndex + "]}";
        assertEquals(expected, markCommand.toString());
    }
}
