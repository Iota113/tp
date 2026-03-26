package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    private final List<Index> targetIndices;
    private final Set<Tag> tags;

    protected TagCommand(List<Index> targetIndices, Set<Tag> tags) {
        requireNonNull(targetIndices);
        requireNonNull(tags);
        this.targetIndices = new ArrayList<>(targetIndices);
        this.tags = new HashSet<>(tags);
    }

    protected List<Person> getTargetPersons(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        List<Person> targetPersons = new ArrayList<>();

        for (Index index : targetIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            targetPersons.add(lastShownList.get(index.getZeroBased()));
        }

        return targetPersons;
    }

    protected List<Index> getTargetIndices() {
        return new ArrayList<>(targetIndices);
    }

    protected Set<Tag> getTags() {
        return new HashSet<>(tags);
    }
}
