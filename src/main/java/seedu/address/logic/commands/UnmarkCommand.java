package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Marks a student's payment status as unpaid.
 */
public class UnmarkCommand extends Command {

    public static final String COMMAND_WORD = "unmark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the payment status of the student identified by the index number"
            + " in the displayed student list as unpaid.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNMARK_PERSON_SUCCESS = "Marked student as unpaid: %1$s";
    public static final String MESSAGE_ALREADY_UNPAID = "This student has already been marked as unpaid.";

    private final Index targetIndex;

    /**
     * Creates an UnmarkCommand to mark the person at {@code targetIndex} as unpaid.
     */
    public UnmarkCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToUnmark = lastShownList.get(targetIndex.getZeroBased());

        if (!personToUnmark.isPaid()) {
            throw new CommandException(MESSAGE_ALREADY_UNPAID);
        }

        Person unmarkedPerson = new Person(
                personToUnmark.getName(),
                personToUnmark.getPhone(),
                personToUnmark.getEmail(),
                personToUnmark.getAddress(),
                personToUnmark.getDay(),
                personToUnmark.getStartTime(),
                personToUnmark.getEndTime(),
                personToUnmark.getRate(),
                false,
                personToUnmark.getTags()
        );

        model.setPerson(personToUnmark, unmarkedPerson);
        return new CommandResult(String.format(MESSAGE_UNMARK_PERSON_SUCCESS, Messages.format(unmarkedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnmarkCommand)) {
            return false;
        }

        UnmarkCommand otherUnmarkCommand = (UnmarkCommand) other;
        return targetIndex.equals(otherUnmarkCommand.targetIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
