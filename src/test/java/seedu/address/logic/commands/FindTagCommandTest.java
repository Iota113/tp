package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.model.person.TagsContainKeywordsPredicate;

public class FindTagCommandTest {
    @Test
    public void equals() {
        TagsContainKeywordsPredicate firstPredicate = new TagsContainKeywordsPredicate(Collections.singletonList("friends"));
        TagsContainKeywordsPredicate secondPredicate = new TagsContainKeywordsPredicate(Collections.singletonList("colleagues"));

        FindTagCommand findFirstCommand = new FindTagCommand(firstPredicate);
        FindTagCommand findSecondCommand = new FindTagCommand(secondPredicate);

        // same object -> returns true
        assertEquals(findFirstCommand, findFirstCommand);

        // same values -> returns true
        FindTagCommand findFirstCommandCopy = new FindTagCommand(firstPredicate);
        assertEquals(findFirstCommand, findFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(findFirstCommand, 1);

        // null -> returns false
        assertNotEquals(findFirstCommand, null);

        // different predicate -> returns false
        assertNotEquals(findFirstCommand, findSecondCommand);
    }
}
