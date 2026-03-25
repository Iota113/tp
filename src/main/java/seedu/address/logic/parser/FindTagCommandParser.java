package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;
import seedu.address.logic.commands.FindTagCommand;
import seedu.address.model.person.TagsContainKeywordsPredicate;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FindTagCommand object.
 */
public class FindTagCommandParser implements Parser<FindTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindTagCommand
     * and returns a FindTagCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public FindTagCommand parse(String args) throws ParseException {
        // Debug: print the raw args received
        System.out.println("[DEBUG] FindTagCommandParser args: '" + args + "'");
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        // Debug: print the preamble
        System.out.println("[DEBUG] FindTagCommandParser preamble: '" + argMultimap.getPreamble() + "'");

        List<String> tagKeywords = argMultimap.getAllValues(PREFIX_TAG);
        System.out.println("[DEBUG] FindTagCommandParser tag keywords: " + tagKeywords);
        if (tagKeywords.isEmpty() || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTagCommand.MESSAGE_USAGE));
        }

        return new FindTagCommand(new TagsContainKeywordsPredicate(tagKeywords));
    }
}
