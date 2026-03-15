package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddTagCommand;
import seedu.address.logic.commands.DeleteTagCommand;
import seedu.address.model.tag.Tag;

public class TagCommandParserTest {

    private final TagCommandParser parser = new TagCommandParser();

    @Test
    public void parse_addSubcommand_success() {
        AddTagCommand expectedCommand = new AddTagCommand(INDEX_FIRST_PERSON, Set.of(new Tag("friend")));
        assertParseSuccess(parser, "add 1" + TAG_DESC_FRIEND, expectedCommand);
    }

    @Test
    public void parse_deleteSubcommand_success() {
        DeleteTagCommand expectedCommand = new DeleteTagCommand(INDEX_FIRST_PERSON, Set.of(new Tag("friend")));
        assertParseSuccess(parser, "delete 1" + TAG_DESC_FRIEND, expectedCommand);
    }

    @Test
    public void parse_emptyInput_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommandParser.MESSAGE_USAGE);
        assertParseFailure(parser, "   ", expectedMessage);
    }

    @Test
    public void parse_invalidSubcommand_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommandParser.MESSAGE_USAGE);
        assertParseFailure(parser, "update 1" + TAG_DESC_FRIEND, expectedMessage);
    }

    @Test
    public void parse_invalidSubcommandWithoutArgs_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommandParser.MESSAGE_USAGE);
        assertParseFailure(parser, "update", expectedMessage);
    }
}
