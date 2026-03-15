package seedu.address.logic.commands;

import java.util.HashSet;
import java.util.List;
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


public class AddTagCommand extends Command {
    public static final String COMMAND_WORD = "tag add";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a tag to a person in the address book. "
            + "Parameters: "
            + "INDEX (must be a positive integer) "
            + PREFIX_TAG + "TAG (must be a non-empty string)\n"
            + "Example: " + COMMAND_WORD + " "
            + "1 "
            + PREFIX_TAG + "Primary1 "
            + PREFIX_TAG + "Mathematics";


    public static final String MESSAGE_SUCCESS = "Tag(s) added to person: %1$s";

    private final Index targetIndex;
    private final Set<Tag> tagsToAdd;

    public AddTagCommand(Index targetIndex, Set<Tag> tagsToAdd) {
        requireNonNull(targetIndex);
        requireNonNull(tagsToAdd);
        this.targetIndex = targetIndex;
        this.tagsToAdd = new HashSet<>(tagsToAdd);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToTag = lastShownList.get(targetIndex.getZeroBased());
        model.addTagsToPerson(personToTag, tagsToAdd);
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
        return targetIndex.equals(otherTagCommand.targetIndex)
                && tagsToAdd.equals(otherTagCommand.tagsToAdd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetIndex, tagsToAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("tagsToAdd", tagsToAdd)
                .toString();
    }

}
