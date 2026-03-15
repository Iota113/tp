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
 * Deletes tag(s) from an existing person in the address book.
 */
public class DeleteTagCommand extends TagCommand {

	public static final String COMMAND_WORD = "delete";

	public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes tag(s) from a person in the address book. "
			+ "Parameters: "
			+ "INDEX (must be a positive integer) "
			+ PREFIX_TAG + "TAG (must be a non-empty string)\n"
			+ "Example: " + TagCommand.COMMAND_WORD + " " + COMMAND_WORD + " "
			+ "1 "
			+ PREFIX_TAG + "Primary1 "
			+ PREFIX_TAG + "Mathematics";

	public static final String MESSAGE_SUCCESS = "Tag(s) removed from person: %1$s";
	public static final String MESSAGE_TAG_NOT_FOUND = "One or more specified tags do not exist for this person.";

	public DeleteTagCommand(Index targetIndex, Set<Tag> tagsToDelete) {
		super(targetIndex, tagsToDelete);
	}

	@Override
	public CommandResult execute(Model model) throws CommandException {
		requireNonNull(model);
		Person personToUpdate = getTargetPerson(model);
		if (!personToUpdate.getTags().containsAll(getTags())) {
			throw new CommandException(MESSAGE_TAG_NOT_FOUND);
		}
		model.deleteTagsFromPerson(personToUpdate, getTags());
		return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(personToUpdate)));
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
		return getTargetIndex().equals(otherDeleteTagCommand.getTargetIndex())
				&& getTags().equals(otherDeleteTagCommand.getTags());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getTargetIndex(), getTags());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.add("targetIndex", getTargetIndex())
				.add("tagsToDelete", getTags())
				.toString();
	}

}
