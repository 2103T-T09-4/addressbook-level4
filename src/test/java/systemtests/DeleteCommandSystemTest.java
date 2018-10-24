package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.parking.commons.core.Messages.MESSAGE_INVALID_CARPARK_DISPLAYED_INDEX;
import static seedu.parking.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.parking.logic.commands.DeleteCommand.MESSAGE_DELETE_CARPARK_SUCCESS;
import static seedu.parking.testutil.TestUtil.getCarpark;
import static seedu.parking.testutil.TestUtil.getLastIndex;
import static seedu.parking.testutil.TestUtil.getMidIndex;
import static seedu.parking.testutil.TypicalCarparks.KEYWORD_MATCHING_SENGKANG;
import static seedu.parking.testutil.TypicalIndexes.INDEX_FIRST_CARPARK;

import org.junit.Test;

import seedu.parking.commons.core.Messages;
import seedu.parking.commons.core.index.Index;
import seedu.parking.logic.commands.DeleteCommand;
import seedu.parking.logic.commands.RedoCommand;
import seedu.parking.logic.commands.UndoCommand;
import seedu.parking.model.Model;
import seedu.parking.model.carpark.Carpark;

public class DeleteCommandSystemTest extends CarparkFinderSystemTest {

    private static final String MESSAGE_INVALID_DELETE_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);

    @Test
    public void delete() {
        /* ----------------- Performing delete operation while an unfiltered list is being shown ------------------- */

        /* Case: delete the first car park in the list, command with leading spaces and trailing spaces -> deleted */
        Model expectedModel = getModel();
        String command = "     " + DeleteCommand.COMMAND_WORD + "      " + INDEX_FIRST_CARPARK.getOneBased() + "      ";
        Carpark deletedPerson = removeCarpark(expectedModel, INDEX_FIRST_CARPARK);
        String expectedResultMessage = String.format(MESSAGE_DELETE_CARPARK_SUCCESS, deletedPerson);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: delete the last car park in the list -> deleted */
        Model modelBeforeDeletingLast = getModel();
        Index lastPersonIndex = getLastIndex(modelBeforeDeletingLast);
        assertCommandSuccess(lastPersonIndex);

        /* Case: undo deleting the last car park in the list -> last car park restored */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingLast, expectedResultMessage);

        /* Case: redo deleting the last car park in the list -> last car park deleted again */
        command = RedoCommand.COMMAND_WORD;
        removeCarpark(modelBeforeDeletingLast, lastPersonIndex);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingLast, expectedResultMessage);

        /* Case: delete the middle car park in the list -> deleted */
        Index middlePersonIndex = getMidIndex(getModel());
        assertCommandSuccess(middlePersonIndex);

        /* ------------------ Performing delete operation while a filtered list is being shown ---------------------- */

        /* Case: filtered car park list, delete index within bounds of car park finder and car park list -> deleted */
        showCarparksWithName(KEYWORD_MATCHING_SENGKANG);
        Index index = INDEX_FIRST_CARPARK;
        assertTrue(index.getZeroBased() < getModel().getFilteredCarparkList().size());
        assertCommandSuccess(index);

        /* Case: filtered car park list, delete index within bounds of car park finder but out of bounds of car park list
         * -> rejected
         */
        showCarparksWithName(KEYWORD_MATCHING_SENGKANG);
        int invalidIndex = getModel().getCarparkFinder().getCarparkList().size();
        command = DeleteCommand.COMMAND_WORD + " " + invalidIndex;
        assertCommandFailure(command, MESSAGE_INVALID_CARPARK_DISPLAYED_INDEX);

        /* --------------------- Performing delete operation while a car park card is selected ---------------------- */

        /* Case: delete the selected car park -> car park list panel selects the car park before the deleted car park */
        showAllCarparks();
        expectedModel = getModel();
        Index selectedIndex = getLastIndex(expectedModel);
        Index expectedIndex = Index.fromZeroBased(selectedIndex.getZeroBased() - 1);
        selectCarpark(selectedIndex);
        command = DeleteCommand.COMMAND_WORD + " " + selectedIndex.getOneBased();
        deletedPerson = removeCarpark(expectedModel, selectedIndex);
        expectedResultMessage = String.format(MESSAGE_DELETE_CARPARK_SUCCESS, deletedPerson);
        assertCommandSuccess(command, expectedModel, expectedResultMessage, expectedIndex);

        /* --------------------------------- Performing invalid delete operation ----------------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " 0";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " -1";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getCarparkFinder().getCarparkList().size() + 1);
        command = DeleteCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_CARPARK_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " abc", MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " 1 abc", MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("DelETE 1", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Removes the {@code Carpark} at the specified {@code index} in {@code model}'s car park finder.
     * @return the removed car park
     */
    private Carpark removeCarpark(Model model, Index index) {
        Carpark targetCarpark = getCarpark(model, index);
        model.deleteCarpark(targetCarpark);
        return targetCarpark;
    }

    /**
     * Deletes the car park at {@code toDelete} by creating a default {@code DeleteCommand} using {@code toDelete} and
     * performs the same verification as {@code assertCommandSuccess(String, Model, String)}.
     * @see DeleteCommandSystemTest#assertCommandSuccess(String, Model, String)
     */
    private void assertCommandSuccess(Index toDelete) {
        Model expectedModel = getModel();
        Carpark deletedCarpark = removeCarpark(expectedModel, toDelete);
        String expectedResultMessage = String.format(MESSAGE_DELETE_CARPARK_SUCCESS, deletedCarpark);

        assertCommandSuccess(
                DeleteCommand.COMMAND_WORD + " " + toDelete.getOneBased(), expectedModel, expectedResultMessage);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url and selected card remains unchanged.<br>
     * 4. Asserts that the status bar's sync status changes.<br>
     * 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 and 2 are performed by
     * {@code CarparkFinderSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see CarparkFinderSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String)} except that the browser url
     * and selected card are expected to update accordingly depending on the card at {@code expectedSelectedCardIndex}.
     * @see DeleteCommandSystemTest#assertCommandSuccess(String, Model, String)
     * @see CarparkFinderSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
                                      Index expectedSelectedCardIndex) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);

        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }

        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 4. Asserts that the command box has the error style.<br>
     * Verifications 1 and 2 are performed by
     * {@code CarparkFinderSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see CarparkFinderSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
