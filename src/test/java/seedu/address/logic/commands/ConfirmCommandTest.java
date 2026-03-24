package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ConfirmCommandTest {

    @Test
    public void execute_nonEmptyAddressBook_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager();

        ConfirmCommand confirmClear = new ConfirmCommand("clear");
        assertCommandSuccess(confirmClear, model, ConfirmCommand.MESSAGE_SUCCESS_CLEAR, expectedModel);
    }

    @Test
    public void execute_emptyAddressBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        ConfirmCommand confirmClear = new ConfirmCommand("clear");
        assertCommandSuccess(confirmClear, model, ConfirmCommand.MESSAGE_SUCCESS_CLEAR, expectedModel);
    }

    @Test
    public void execute_unknownArgs_returnsInvalidMessage() {
        Model model = new ModelManager();
        ConfirmCommand confirmUnknown = new ConfirmCommand("delete");

        assertCommandSuccess(confirmUnknown, model, ConfirmCommand.MESSAGE_INVALID, model);
    }
}
