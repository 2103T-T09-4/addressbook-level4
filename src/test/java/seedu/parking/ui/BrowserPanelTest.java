package seedu.parking.ui;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertEquals;
import static seedu.parking.testutil.EventsUtil.postNow;
import static seedu.parking.testutil.TypicalCarparks.ALFA;
import static seedu.parking.ui.BrowserPanel.MAINPAGE_FILE_PATH;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.BrowserPanelHandle;
import seedu.parking.commons.events.ui.CarparkPanelSelectionChangedEvent;

public class BrowserPanelTest extends GuiUnitTest {
    private CarparkPanelSelectionChangedEvent selectionChangedEventStub;

    private BrowserPanel browserPanel;
    private BrowserPanelHandle browserPanelHandle;

    @Before
    public void setUp() {
        selectionChangedEventStub = new CarparkPanelSelectionChangedEvent(ALFA);

        guiRobot.interact(() -> browserPanel = new BrowserPanel());
        uiPartRule.setUiPart(browserPanel);

        browserPanelHandle = new BrowserPanelHandle(browserPanel.getRoot());
    }

    @Test
    public void display() throws Exception {
        // default web page
        URL expectedDefaultPageUrl = new URL(getClass().getResource(MAINPAGE_FILE_PATH).toString() + "?isDefault=1");
        assertEquals(expectedDefaultPageUrl, browserPanelHandle.getLoadedUrl());

        // associated web page of a car park
        postNow(selectionChangedEventStub);
        URL expectedPersonUrl = new URL(getClass().getResource(MAINPAGE_FILE_PATH).toString() + "?json="
                + ALFA.toJson().replaceAll(" ", "%20"));

        waitUntilBrowserLoaded(browserPanelHandle);
        assertEquals(expectedPersonUrl, browserPanelHandle.getLoadedUrl());
    }
}
