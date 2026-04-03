package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Prompts the user to confirm their intent to clear all OnlyTutors' contacts.
 */
public class ClearCommand extends Command {

    /**
     * Represents the confirmation state of a {@code ClearCommand}.
     * <ul>
     *     <li>{@code PROMPT} - The user has typed {@code clear} and is awaiting confirmation.</li>
     *     <li>{@code CONFIRMED} - The user confirmed with {@code y}, address book will be cleared.</li>
     *     <li>{@code ABORTED} - The user aborted with {@code n}, no changes will be made.</li>
     * </ul>
     */
    public enum ClearState {
        PROMPT, CONFIRMED, ABORTED
    }

    public static final String COMMAND_WORD = "clear";
    // Updated prompt to suggest that non-y input defaults to 'No'
    public static final String MESSAGE_CONFIRM_PROMPT =
            "This will delete all contacts. Are you sure? [y/N]:";
    public static final String MESSAGE_ABORTED = "Clear aborted (invalid or 'n' input).";
    public static final String MESSAGE_SUCCESS = "Cleared all contacts";

    private final ClearState state;

    public ClearCommand(ClearState state) {
        this.state = state;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        switch (state) {
        case PROMPT:
            return new CommandResult(MESSAGE_CONFIRM_PROMPT);
        case ABORTED:
            return new CommandResult(MESSAGE_ABORTED);
        case CONFIRMED:
            model.setAddressBook(new AddressBook());
            return new CommandResult(MESSAGE_SUCCESS);
        default:
            throw new AssertionError("Unknown ClearState: " + state);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ClearCommand)) {
            return false;
        }
        ClearCommand otherClearCommand = (ClearCommand) other;
        return state == otherClearCommand.state;
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }
}
