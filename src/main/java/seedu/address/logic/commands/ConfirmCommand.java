package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Locale;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Confirms a pending action specified by the user.
 * Currently supports confirmation of the clear command.
 */
public class ConfirmCommand extends Command {

    public static final String COMMAND_WORD = "confirm";
    public static final String MESSAGE_SUCCESS_CLEAR = "All OnlyTutors' contacts have been successfully cleared!";
    public static final String MESSAGE_INVALID = "Nothing to confirm.";

    private final String confirmType;

    /**
     * Creates a ConfirmCommand with the specified confirmation target.
     *
     * @param confirmType The type of action to confirm (e.g. "clear").
     */
    public ConfirmCommand(String confirmType) {
        this.confirmType = confirmType.toLowerCase(Locale.ROOT);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        switch (confirmType) {
        case "clear":
            model.setAddressBook(new AddressBook());
            return new CommandResult(MESSAGE_SUCCESS_CLEAR);

        default:
            return new CommandResult(MESSAGE_INVALID);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ConfirmCommand)) {
            return false;
        }
        ConfirmCommand otherCommand = (ConfirmCommand) other;
        return confirmType.equals(otherCommand.confirmType);
    }

    @Override
    public int hashCode() {
        return confirmType.hashCode();
    }
}
