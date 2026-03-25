package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.AddTagCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteTagCommand;
import seedu.address.logic.commands.FindTagCommand;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates the appropriate TagCommand object (AddTagCommand or DeleteTagCommand)
 * based on the command word.
 */
public class TagCommandParser implements Parser<Command> {

    public static final String COMMAND_WORD = TagCommand.COMMAND_WORD;

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Executes a tag subcommand.\n"
            + "Subcommands:\n"
            + "  " + AddTagCommand.MESSAGE_USAGE + "\n"
            + "  " + DeleteTagCommand.MESSAGE_USAGE + "\n"
            + "  " + FindTagCommand.MESSAGE_USAGE;

    @Override
    public Command parse(String userInput) throws ParseException {
        String trimmedInput = userInput.trim();
        if (trimmedInput.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }

        String[] splitInput = trimmedInput.split("\\s+", 2);
        String subcommand = splitInput[0];
        String subcommandArgs = splitInput.length > 1 ? splitInput[1] : "";

        if (subcommand.equals(AddTagCommand.SUBCOMMAND_WORD)) {
            return new AddTagCommandParser().parse(subcommandArgs);
        }
        if (subcommand.equals(DeleteTagCommand.SUBCOMMAND_WORD)) {
            return new DeleteTagCommandParser().parse(subcommandArgs);
        }
        if (subcommand.equals(FindTagCommand.SUBCOMMAND_WORD)) {
            return new FindTagCommandParser().parse(subcommandArgs);
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
    }
}
