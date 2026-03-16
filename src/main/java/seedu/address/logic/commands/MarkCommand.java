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
 * Marks a student's payment status as paid.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the payment status of the student identified by the index number"
            + " in the displayed student list as paid.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_MARK_PERSON_SUCCESS = "Marked student as paid: %1$s";
    public static final String MESSAGE_ALREADY_PAID = "This student has already been marked as paid.";

    private final Index targetIndex;

    /**
     * Creates a MarkCommand to mark the person at {@code targetIndex} as paid.
     */
    public MarkCommand(Index targetIndex) {
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

        Person personToMark = lastShownList.get(targetIndex.getZeroBased());

        if (personToMark.isPaid()) {
            throw new CommandException(MESSAGE_ALREADY_PAID);
        }

        Person markedPerson = new Person(
                personToMark.getName(),
                personToMark.getPhone(),
                personToMark.getEmail(),
                personToMark.getAddress(),
                personToMark.getDay(),
                personToMark.getStartTime(),
                personToMark.getEndTime(),
                personToMark.getRate(),
                true,
                personToMark.getTags()
        );

        model.setPerson(personToMark, markedPerson);
        return new CommandResult(String.format(MESSAGE_MARK_PERSON_SUCCESS, Messages.format(markedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof MarkCommand)) {
            return false;
        }

        MarkCommand otherMarkCommand = (MarkCommand) other;
        return targetIndex.equals(otherMarkCommand.targetIndex);
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
