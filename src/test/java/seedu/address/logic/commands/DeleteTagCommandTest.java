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
 * Contains integration tests (interaction with the Model) and unit tests for DeleteTagCommand.
 */
public class DeleteTagCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToUpdate = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToDelete = Set.of(new Tag("friends"));
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(INDEX_FIRST_PERSON, tagsToDelete);

        String expectedMessage = String.format(DeleteTagCommand.MESSAGE_SUCCESS, Messages.format(personToUpdate));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteTagsFromPerson(personToUpdate, tagsToDelete);

        assertCommandSuccess(deleteTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_tagNotFound_throwsCommandException() {
        Set<Tag> tagsToDelete = Set.of(new Tag("classmate"));
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(INDEX_FIRST_PERSON, tagsToDelete);

        assertCommandFailure(deleteTagCommand, model, DeleteTagCommand.MESSAGE_TAG_NOT_FOUND);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Set<Tag> tagsToDelete = Set.of(new Tag("friends"));
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(outOfBoundIndex, tagsToDelete);

        assertCommandFailure(deleteTagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Set<Tag> firstTags = Set.of(new Tag("friends"));
        Set<Tag> secondTags = Set.of(new Tag("teammate"));
        DeleteTagCommand deleteFirstCommand = new DeleteTagCommand(INDEX_FIRST_PERSON, firstTags);
        DeleteTagCommand deleteSecondCommand = new DeleteTagCommand(INDEX_SECOND_PERSON, secondTags);
        DeleteTagCommand deleteFirstCommandDifferentTags = new DeleteTagCommand(INDEX_FIRST_PERSON, secondTags);

        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        DeleteTagCommand deleteFirstCommandCopy = new DeleteTagCommand(INDEX_FIRST_PERSON, firstTags);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        assertFalse(deleteFirstCommand.equals(1));
        assertFalse(deleteFirstCommand.equals(null));
        assertFalse(deleteFirstCommand.equals(deleteFirstCommandDifferentTags));
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void hashCodeMethod() {
        Set<Tag> tagsToDelete = Set.of(new Tag("friends"));
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(INDEX_FIRST_PERSON, tagsToDelete);
        DeleteTagCommand deleteTagCommandCopy = new DeleteTagCommand(INDEX_FIRST_PERSON, tagsToDelete);

        assertEquals(deleteTagCommand.hashCode(), deleteTagCommandCopy.hashCode());
    }

    @Test
    public void toStringMethod() {
        Set<Tag> tagsToDelete = Set.of(new Tag("friends"));
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(INDEX_FIRST_PERSON, tagsToDelete);
        String expected = DeleteTagCommand.class.getCanonicalName()
                + "{targetIndex=" + INDEX_FIRST_PERSON + ", tagsToDelete=" + tagsToDelete + "}";
        assertEquals(expected, deleteTagCommand.toString());
    }
}
