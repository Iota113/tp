package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_prompt_showsConfirmMessage() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(ClearCommand.ClearState.PROMPT),
                model, ClearCommand.MESSAGE_CONFIRM_PROMPT, expectedModel);
    }

    @Test
    public void execute_aborted_showsAbortMessage() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Model should be unchanged after abort
        assertCommandSuccess(new ClearCommand(ClearCommand.ClearState.ABORTED),
                model, ClearCommand.MESSAGE_ABORTED, expectedModel);
    }

    @Test
    public void execute_confirmed_emptyAddressBook() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(ClearCommand.ClearState.CONFIRMED),
                model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
