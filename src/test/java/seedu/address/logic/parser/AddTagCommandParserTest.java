package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddTagCommand;
import seedu.address.model.tag.Tag;

public class AddTagCommandParserTest {

    private final AddTagCommandParser parser = new AddTagCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Set<Tag> tags = Set.of(new Tag("classmate"), new Tag("friend"));
        AddTagCommand expectedCommand = new AddTagCommand(List.of(INDEX_FIRST_PERSON), tags);
        assertParseSuccess(parser, "1" + TAG_DESC_FRIEND + " t/classmate", expectedCommand);
    }

    @Test
    public void parse_missingIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE);
        assertParseFailure(parser, TAG_DESC_FRIEND, expectedMessage);
    }

    @Test
    public void parse_missingTag_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "1", expectedMessage);
    }

    @Test
    public void parse_invalidTag_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "1 t/tag*", expectedMessage);
    }
}
