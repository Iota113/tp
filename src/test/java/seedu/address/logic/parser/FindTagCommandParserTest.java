package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.TagsContainKeywordsPredicate;

public class FindTagCommandParserTest {
    private final FindTagCommandParser parser = new FindTagCommandParser();

    @Test
    public void parse_tagPrefixAtStart_success() throws Exception {
        FindTagCommand expected = new FindTagCommand(new TagsContainKeywordsPredicate(Collections.singletonList("friends")));
        assertEquals(expected, parser.parse("t/friends"));
    }

    @Test
    public void parse_multipleTags_success() throws Exception {
        FindTagCommand expected = new FindTagCommand(new TagsContainKeywordsPredicate(Arrays.asList("friends", "colleagues")));
        assertEquals(expected, parser.parse("t/friends t/colleagues"));
    }

    @Test
    public void parse_emptyInput_failure() {
        assertThrows(ParseException.class, () -> parser.parse(" "));
    }

    @Test
    public void parse_noTagPrefix_failure() {
        assertThrows(ParseException.class, () -> parser.parse("friends"));
    }

    @Test
    public void parse_preamblePresent_failure() {
        String input = "random t/friends";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTagCommand.MESSAGE_USAGE), ex.getMessage());
    }

    @Test
    public void parse_repeatedTags_success() throws Exception {
        FindTagCommand expected = new FindTagCommand(new TagsContainKeywordsPredicate(Arrays.asList("friends", "friends")));
        assertEquals(expected, parser.parse("t/friends t/friends"));
    }
}
