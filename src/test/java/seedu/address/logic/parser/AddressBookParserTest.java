package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddTagCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteTagCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.MarkCommand;
import seedu.address.logic.commands.UnmarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear_returnsPrompt() throws Exception {
        ClearCommand command = (ClearCommand) parser.parseCommand(ClearCommand.COMMAND_WORD);
        assertEquals(new ClearCommand(ClearCommand.ClearState.PROMPT), command);
    }

    @Test
    public void parseCommand_clearThenY_returnsConfirmed() throws Exception {
        parser.parseCommand(ClearCommand.COMMAND_WORD);
        ClearCommand command = (ClearCommand) parser.parseCommand("y");
        assertEquals(new ClearCommand(ClearCommand.ClearState.CONFIRMED), command);
    }

    @Test
    public void parseCommand_clearThenUpperY_returnsConfirmed() throws Exception {
        parser.parseCommand(ClearCommand.COMMAND_WORD);
        ClearCommand command = (ClearCommand) parser.parseCommand("Y");
        assertEquals(new ClearCommand(ClearCommand.ClearState.CONFIRMED), command);
    }

    @Test
    public void parseCommand_clearThenN_returnsAborted() throws Exception {
        parser.parseCommand(ClearCommand.COMMAND_WORD);
        ClearCommand command = (ClearCommand) parser.parseCommand("n");
        assertEquals(new ClearCommand(ClearCommand.ClearState.ABORTED), command);
    }

    @Test
    public void parseCommand_clearThenUpperN_returnsAborted() throws Exception {
        parser.parseCommand(ClearCommand.COMMAND_WORD);
        ClearCommand command = (ClearCommand) parser.parseCommand("N");
        assertEquals(new ClearCommand(ClearCommand.ClearState.ABORTED), command);
    }

    @Test
    public void parseCommand_clearThenInvalidInput_returnsAborted() throws Exception {
        parser.parseCommand(ClearCommand.COMMAND_WORD);
        ClearCommand command = (ClearCommand) parser.parseCommand("maybe");
        assertEquals(new ClearCommand(ClearCommand.ClearState.ABORTED), command);
    }

    @Test
    public void parseCommand_clearConfirmWithoutClear_throwsParseException() {
        // bypass attempt — should fail as unknown command
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () ->
                parser.parseCommand("confirm clear"));
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(List.of(INDEX_FIRST_PERSON)), command);
    }


    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_mark() throws Exception {
        MarkCommand command = (MarkCommand) parser.parseCommand(
                MarkCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new MarkCommand(List.of(INDEX_FIRST_PERSON)), command);
    }

    @Test
    public void parseCommand_unmark() throws Exception {
        UnmarkCommand command = (UnmarkCommand) parser.parseCommand(
                UnmarkCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new UnmarkCommand(List.of(INDEX_FIRST_PERSON)), command);
    }

    @Test
    public void parseCommand_tag() throws Exception {
        AddTagCommand addTagCommand = (AddTagCommand) parser.parseCommand("tag add 1 t/friends");
        assertEquals(new AddTagCommand(List.of(INDEX_FIRST_PERSON), java.util.Set.of(new Tag("friends"))),
                addTagCommand);

        DeleteTagCommand deleteTagCommand = (DeleteTagCommand) parser.parseCommand("tag delete 1 t/friends");
        assertEquals(new DeleteTagCommand(List.of(INDEX_FIRST_PERSON), java.util.Set.of(new Tag("friends"))),
                deleteTagCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
