package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Marks one or more students' payment status as unpaid.
 */
public class UnmarkCommand extends Command {

    public static final String COMMAND_WORD = "unmark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the payment status of the student(s) identified by index number(s)"
            + " in the displayed student list as unpaid.\n"
            + "Parameters: INDEX [INDEX]... (must be positive integers)\n"
            + "Example: " + COMMAND_WORD + " 1 2 3";

    public static final String MESSAGE_UNMARK_PERSON_SUCCESS = "Marked student as unpaid: %1$s";
    public static final String MESSAGE_UNMARK_PERSONS_SUCCESS = "Marked %1$d students as unpaid: %2$s";
    public static final String MESSAGE_ALREADY_UNPAID = "This student has already been marked as unpaid.";

    private final List<Index> targetIndices;

    /**
     * Creates an UnmarkCommand to mark the persons at {@code targetIndices} as unpaid.
     */
    public UnmarkCommand(List<Index> targetIndices) {
        requireNonNull(targetIndices);
        this.targetIndices = new ArrayList<>(targetIndices);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        for (Index index : targetIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        List<Person> personsToUnmark = new ArrayList<>();
        for (Index index : targetIndices) {
            Person person = lastShownList.get(index.getZeroBased());
            if (!person.isPaid()) {
                throw new CommandException(MESSAGE_ALREADY_UNPAID);
            }
            personsToUnmark.add(person);
        }

        List<Person> unmarkedPersons = new ArrayList<>();
        for (Person personToUnmark : personsToUnmark) {
            Person unmarkedPerson = new Person(
                    personToUnmark.getName(), personToUnmark.getPhone(), personToUnmark.getEmail(),
                    personToUnmark.getAddress(), personToUnmark.getDay(), personToUnmark.getStartTime(),
                    personToUnmark.getEndTime(), personToUnmark.getRate(), false, personToUnmark.getTags());
            model.setPerson(personToUnmark, unmarkedPerson);
            unmarkedPersons.add(unmarkedPerson);
        }

        if (unmarkedPersons.size() == 1) {
            return new CommandResult(String.format(MESSAGE_UNMARK_PERSON_SUCCESS,
                    Messages.format(unmarkedPersons.get(0))));
        }
        String names = unmarkedPersons.stream()
                .map(p -> p.getName().toString()).collect(Collectors.joining(", "));
        return new CommandResult(String.format(MESSAGE_UNMARK_PERSONS_SUCCESS, unmarkedPersons.size(), names));
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
        return targetIndices.equals(otherUnmarkCommand.targetIndices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", targetIndices)
                .toString();
    }
}
