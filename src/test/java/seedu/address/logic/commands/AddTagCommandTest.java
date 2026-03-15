package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for AddTagCommand.
 */
public class AddTagCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToTag = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToAdd = Set.of(new Tag("classmate"));
        AddTagCommand addTagCommand = new AddTagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        String expectedMessage = String.format(AddTagCommand.MESSAGE_SUCCESS, Messages.format(personToTag));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addTagsToPerson(personToTag, tagsToAdd);

        assertCommandSuccess(addTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Set<Tag> tagsToAdd = Set.of(new Tag("classmate"));
        AddTagCommand addTagCommand = new AddTagCommand(outOfBoundIndex, tagsToAdd);

        assertCommandFailure(addTagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Set<Tag> firstTags = Set.of(new Tag("classmate"));
        Set<Tag> secondTags = Set.of(new Tag("teammate"));
        AddTagCommand addFirstCommand = new AddTagCommand(INDEX_FIRST_PERSON, firstTags);
        AddTagCommand addSecondCommand = new AddTagCommand(INDEX_SECOND_PERSON, secondTags);
        AddTagCommand addFirstCommandDifferentTags = new AddTagCommand(INDEX_FIRST_PERSON, secondTags);

        assertTrue(addFirstCommand.equals(addFirstCommand));

        AddTagCommand addFirstCommandCopy = new AddTagCommand(INDEX_FIRST_PERSON, firstTags);
        assertTrue(addFirstCommand.equals(addFirstCommandCopy));

        assertFalse(addFirstCommand.equals(1));
        assertFalse(addFirstCommand.equals(null));
        assertFalse(addFirstCommand.equals(addFirstCommandDifferentTags));
        assertFalse(addFirstCommand.equals(addSecondCommand));
    }

    @Test
    public void hashCodeMethod() {
        Set<Tag> tagsToAdd = Set.of(new Tag("classmate"));
        AddTagCommand addTagCommand = new AddTagCommand(INDEX_FIRST_PERSON, tagsToAdd);
        AddTagCommand addTagCommandCopy = new AddTagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        assertEquals(addTagCommand.hashCode(), addTagCommandCopy.hashCode());
    }

    @Test
    public void toStringMethod() {
        Set<Tag> tagsToAdd = Set.of(new Tag("classmate"));
        AddTagCommand addTagCommand = new AddTagCommand(INDEX_FIRST_PERSON, tagsToAdd);
        String expected = AddTagCommand.class.getCanonicalName()
                + "{targetIndex=" + INDEX_FIRST_PERSON + ", tagsToAdd=" + tagsToAdd + "}";
        assertEquals(expected, addTagCommand.toString());
    }
}
