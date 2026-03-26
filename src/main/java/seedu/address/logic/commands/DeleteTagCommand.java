package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Deletes tags from one or more existing persons in the address book.
 */
public class DeleteTagCommand extends TagCommand {

    public static final String SUBCOMMAND_WORD = "delete";
    public static final String COMMAND_PHRASE = TagCommand.COMMAND_WORD + " " + SUBCOMMAND_WORD;

    public static final String MESSAGE_USAGE = COMMAND_PHRASE
            + ": Deletes tag(s) from person(s) in the address book. "
            + "Parameters: "
            + "INDEX [INDEX]... (must be positive integers) "
            + PREFIX_TAG + "TAG (must be a non-empty string)\n"
            + "Example: " + COMMAND_PHRASE + " "
            + "1 2 "
            + PREFIX_TAG + "Primary1 "
            + PREFIX_TAG + "Mathematics";

    public static final String MESSAGE_SUCCESS = "Tag(s) removed from person: %1$s";
    public static final String MESSAGE_BATCH_SUCCESS = "Tag(s) removed from %1$d persons: %2$s";
    public static final String MESSAGE_TAG_NOT_FOUND = "One or more specified tags do not exist for this person.";

    /**
     * Creates a DeleteTagCommand to remove tags from persons at {@code targetIndices}.
     */
    public DeleteTagCommand(List<Index> targetIndices, Set<Tag> tagsToDelete) {
        super(targetIndices, tagsToDelete);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> personsToUpdate = getTargetPersons(model);

        for (Person person : personsToUpdate) {
            if (!person.getTags().containsAll(getTags())) {
                throw new CommandException(MESSAGE_TAG_NOT_FOUND);
            }
        }

        for (Person person : personsToUpdate) {
            model.deleteTagsFromPerson(person, getTags());
        }

        if (personsToUpdate.size() == 1) {
            return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(personsToUpdate.get(0))));
        }
        String names = personsToUpdate.stream()
                .map(p -> p.getName().toString()).collect(Collectors.joining(", "));
        return new CommandResult(String.format(MESSAGE_BATCH_SUCCESS, personsToUpdate.size(), names));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeleteTagCommand)) {
            return false;
        }
        DeleteTagCommand otherDeleteTagCommand = (DeleteTagCommand) other;
        return getTargetIndices().equals(otherDeleteTagCommand.getTargetIndices())
            && getTags().equals(otherDeleteTagCommand.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTargetIndices(), getTags());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("targetIndices", getTargetIndices())
            .add("tagsToDelete", getTags())
            .toString();
    }
}
