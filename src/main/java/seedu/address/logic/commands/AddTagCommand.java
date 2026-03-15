package seedu.address.logic.commands;

import java.util.Objects;
import static java.util.Objects.requireNonNull;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Adds a tag to an existing person in the address book.
 */
public class AddTagCommand extends TagCommand {
    public static final String COMMAND_WORD = "add";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a tag to a person in the address book. "
            + "Parameters: "
            + "INDEX (must be a positive integer) "
            + PREFIX_TAG + "TAG (must be a non-empty string)\n"
            + "Example: " + TagCommand.COMMAND_WORD + " " + COMMAND_WORD + " "
            + "1 "
            + PREFIX_TAG + "Primary1 "
            + PREFIX_TAG + "Mathematics";


    public static final String MESSAGE_SUCCESS = "Tag(s) added to person: %1$s";

    public AddTagCommand(Index targetIndex, Set<Tag> tagsToAdd) {
        super(targetIndex, tagsToAdd);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person personToTag = getTargetPerson(model);
        model.addTagsToPerson(personToTag, getTags());
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(personToTag)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddTagCommand)) {
            return false;
        }

        AddTagCommand otherTagCommand = (AddTagCommand) other;
        return getTargetIndex().equals(otherTagCommand.getTargetIndex())
            && getTags().equals(otherTagCommand.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTargetIndex(), getTags());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", getTargetIndex())
                .add("tagsToAdd", getTags())
                .toString();
    }

}
