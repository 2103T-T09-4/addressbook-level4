package seedu.address.ui.testutil;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import guitests.guihandles.CarparkCardHandle;
import guitests.guihandles.CarparkListPanelHandle;
import guitests.guihandles.ResultDisplayHandle;
import seedu.address.model.carpark.Carpark;

/**
 * A set of assertion methods useful for writing GUI tests.
 */
public class GuiTestAssert {
    /**
     * Asserts that {@code actualCard} displays the same values as {@code expectedCard}.
     */
    public static void assertCardEquals(CarparkCardHandle expectedCard, CarparkCardHandle actualCard) {
        assertEquals(expectedCard.getCarparkNumber(), actualCard.getCarparkNumber());
        assertEquals(expectedCard.getCarparkType(), actualCard.getCarparkType());
        assertEquals(expectedCard.getCoordinate(), actualCard.getCoordinate());
        assertEquals(expectedCard.getFreeParking(), actualCard.getFreeParking());
        assertEquals(expectedCard.getLotsAvailable(), actualCard.getLotsAvailable());
        assertEquals(expectedCard.getNightParking(), actualCard.getNightParking());
        assertEquals(expectedCard.getShortTerm(), actualCard.getShortTerm());
        assertEquals(expectedCard.getTotalLots(), actualCard.getTotalLots());
        assertEquals(expectedCard.getTypeOfParking(), actualCard.getTypeOfParking());
        assertEquals(expectedCard.getAddress(), actualCard.getAddress());
        assertEquals(expectedCard.getTags(), actualCard.getTags());
    }

    /**
     * Asserts that {@code actualCard} displays the details of {@code expectedCarpark}.
     */
    public static void assertCardDisplaysCarpark(Carpark expectedCarpark, CarparkCardHandle actualCard) {
        assertEquals(expectedCarpark.getCarparkNumber().value, actualCard.getCarparkNumber());
        assertEquals(expectedCarpark.getAddress().value, actualCard.getAddress());
        assertEquals(expectedCarpark.getCarparkType().value, actualCard.getCarparkType());
        assertEquals("Coordinate: " + expectedCarpark.getCoordinate().value, actualCard.getCoordinate());

        if (expectedCarpark.getTotalLots().value.equals("0")) {
            assertEquals("Total Lots: Not Available", actualCard.getTotalLots());
            assertEquals("Lots Available: Not Available", actualCard.getLotsAvailable());
        } else {
            assertEquals("Total Lots: " + expectedCarpark.getTotalLots().value, actualCard.getTotalLots());
            assertEquals("Lots Available: " + expectedCarpark.getLotsAvailable().value, actualCard.getLotsAvailable());
        }

        assertEquals("Free Parking: " + expectedCarpark.getFreeParking().value, actualCard.getFreeParking());
        assertEquals("Night Parking: " + expectedCarpark.getNightParking().value, actualCard.getNightParking());
        assertEquals("Short Term Parking: " + expectedCarpark.getShortTerm().value, actualCard.getShortTerm());
        assertEquals("Parking System: " + expectedCarpark.getTypeOfParking().value, actualCard.getTypeOfParking());
        assertEquals(expectedCarpark.getTags().stream().map(tag -> tag.tagName).collect(Collectors.toList()),
                actualCard.getTags());
    }

    /**
     * Asserts that the list in {@code carparkListPanelHandle} displays the details of {@code carparks} correctly and
     * in the correct order.
     */
    public static void assertListMatching(CarparkListPanelHandle carparkListPanelHandle, Carpark... carparks) {
        for (int i = 0; i < carparks.length; i++) {
            carparkListPanelHandle.navigateToCard(i);
            assertCardDisplaysCarpark(carparks[i], carparkListPanelHandle.getCarparkCardHandle(i));
        }
    }

    /**
     * Asserts that the list in {@code carparkListPanelHandle} displays the details of {@code carparks} correctly and
     * in the correct order.
     */
    public static void assertListMatching(CarparkListPanelHandle carparkListPanelHandle, List<Carpark> carparks) {
        assertListMatching(carparkListPanelHandle, carparks.toArray(new Carpark[0]));
    }

    /**
     * Asserts the size of the list in {@code carparkListPanelHandle} equals to {@code size}.
     */
    public static void assertListSize(CarparkListPanelHandle carparkListPanelHandle, int size) {
        int numberOfCarpark = carparkListPanelHandle.getListSize();
        assertEquals(size, numberOfCarpark);
    }

    /**
     * Asserts the message shown in {@code resultDisplayHandle} equals to {@code expected}.
     */
    public static void assertResultMessage(ResultDisplayHandle resultDisplayHandle, String expected) {
        assertEquals(expected, resultDisplayHandle.getText());
    }
}
