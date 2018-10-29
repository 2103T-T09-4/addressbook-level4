package seedu.parking.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.parking.commons.util.GsonUtil;
import seedu.parking.logic.CommandHistory;
import seedu.parking.logic.commands.exceptions.CommandException;
import seedu.parking.model.Model;
import seedu.parking.model.carpark.Address;
import seedu.parking.model.carpark.Carpark;
import seedu.parking.model.carpark.CarparkNumber;
import seedu.parking.model.carpark.CarparkType;
import seedu.parking.model.carpark.Coordinate;
import seedu.parking.model.carpark.FreeParking;
import seedu.parking.model.carpark.LotsAvailable;
import seedu.parking.model.carpark.NightParking;
import seedu.parking.model.carpark.ShortTerm;
import seedu.parking.model.carpark.TotalLots;
import seedu.parking.model.carpark.TypeOfParking;

/**
 * Queries when to get the car park information from the API.
 */
public class QueryCommand extends Command {

    public static final String COMMAND_WORD = "query";
    public static final String COMMAND_ALIAS = "q";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Updates all the car park information in Car Park Finder.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "%1$d Car parks updated";
    private static final String MESSAGE_ERROR_CARPARK = "Unable to load car park information from database";

    /**
     * Calls the API and load all the car parks information
     * @return An array of car parks
     */
    private List<Carpark> readCarpark(List<List<String>> carparkData) {
        List<Carpark> carparkList = new ArrayList<>();
        for (List<String> carpark : carparkData) {
            Carpark c = new Carpark(new Address(carpark.get(0)), new CarparkNumber(carpark.get(1)),
                    new CarparkType(carpark.get(2)), new Coordinate(carpark.get(3)),
                    new FreeParking(carpark.get(4)), new LotsAvailable(carpark.get(5)),
                    new NightParking(carpark.get(6)), new ShortTerm(carpark.get(7)),
                    new TotalLots(carpark.get(8)), new TypeOfParking(carpark.get(9)), null);
            carparkList.add(c);
        }
        return carparkList;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        int updated;

        try {
            List<List<String>> carparkData = new ArrayList<>(GsonUtil.fetchCarparkInfo());
            List<Carpark> allCarparks = new ArrayList<>(readCarpark(carparkData));
            model.loadCarpark(allCarparks);
            model.commitCarparkFinder();
            updated = model.compareCarparkFinder();
        } catch (Exception e) {
            throw new CommandException(MESSAGE_ERROR_CARPARK);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, updated));
    }
}
