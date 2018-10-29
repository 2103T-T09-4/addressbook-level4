package seedu.parking.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.parking.logic.CommandHistory;
import seedu.parking.model.CarparkFinder;
import seedu.parking.model.Model;

/**
 * Clears the car park finder.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "All car park information has been cleared!";
    public static final String MESSAGE_EMPTY = "There is nothing to be cleared.";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        int size = model.getCarparkFinder().getCarparkList().size();

        try {
            if (size == 0) {
                return new CommandResult(MESSAGE_EMPTY);
            }

            return new CommandResult(MESSAGE_SUCCESS);
        } finally {
            model.resetData(new CarparkFinder());
            model.commitCarparkFinder();
        }
    }
}
