package seedu.address.logic.commands;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Base class for tag subcommands.
 */
public abstract class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    private final Index targetIndex;
    private final Set<Tag> tags;

    protected TagCommand(Index targetIndex, Set<Tag> tags) {
        requireNonNull(targetIndex);
        requireNonNull(tags);
        this.targetIndex = targetIndex;
        this.tags = new HashSet<>(tags);
    }

    protected Person getTargetPerson(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return lastShownList.get(targetIndex.getZeroBased());
    }

    Index getTargetIndex() {
        return targetIndex;
    }

    Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }
}