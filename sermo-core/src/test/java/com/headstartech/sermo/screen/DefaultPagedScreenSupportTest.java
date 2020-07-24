package com.headstartech.sermo.screen;

import com.headstartech.sermo.SermoSystemConstants;
import org.junit.jupiter.api.Test;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.support.DefaultExtendedState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class DefaultPagedScreenSupportTest {

    @Test
    public void pagedScreenSetupInExtendedState() {
        // given
        PagedScreenSupport pagedScreenSupport = new DefaultPagedScreenSupport();
        ExtendedState extendedState = new DefaultExtendedState();
        PagedScreenSetup pagedScreenSetup = new PagedScreenSetup(null, null, null, null, null, 0);

        // when
        pagedScreenSupport.initializePagedScreen(extendedState, pagedScreenSetup);

        // then
        assertSame(pagedScreenSetup, extendedState.getVariables().get(SermoSystemConstants.PAGED_SCREEN_KEY));
    }

    @Test
    public void canRenderPagedScreen () {
        // given
        String expectedPage1 = "Accounts\n" +
                "1. Account A...\n" +
                "2. Account B...\n" +
                "0 Next page\n";
        String expectedPage2 = "Accounts\n" +
                "1. Account C...\n" +
                "2. Account D...\n" +
                "0 Next page\n" +
                "# Previous page\n";
        String expectedPage3 = "Accounts\n" +
                "1. Account E...\n" +
                "# Previous page\n";

        PagedScreenSupport pagedScreenSupport = new DefaultPagedScreenSupport();
        ExtendedState extendedState = new DefaultExtendedState();

        int pageSize = 2;
        String transationId = "transitionId";
        String itemA = "A";
        String itemB = "B";
        String itemC = "C";
        String itemD = "D";
        String itemE = "E";
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Account A12345", transationId, itemA));
        items.add(new MenuItem("Account B12345", transationId, itemB));
        items.add(new MenuItem("Account C12345", transationId, itemC));
        items.add(new MenuItem("Account D12345", transationId, itemD));
        items.add(new MenuItem("Account E12345", transationId, itemE));

        Text headerBlock = new Text("Accounts");
        NextPageMenuItem nextPageMenuItem = new NextPageMenuItem("0", "Next page");
        PreviousPageMenuItem previousPageMenuItem = new PreviousPageMenuItem("#", "Previous page");
        TextElide elide = new TextElide(TextElide.Mode.RIGHT, 15);
        PagedScreenSetup pagedScreenSetup = new PagedScreenSetup(items, elide, nextPageMenuItem, previousPageMenuItem, headerBlock, null, pageSize);

        pagedScreenSupport.initializePagedScreen(extendedState, pagedScreenSetup);

        // when ... then

        // page 1
        Screen screen = pagedScreenSupport.createScreen(extendedState);
        assertEquals(expectedPage1, screen.getOutput());

        // page 2
        pagedScreenSupport.incrementPage(extendedState);
        screen = pagedScreenSupport.createScreen(extendedState);
        assertEquals(expectedPage2, screen.getOutput());

        // page 3
        pagedScreenSupport.incrementPage(extendedState);
        screen = pagedScreenSupport.createScreen(extendedState);
        assertEquals(expectedPage3, screen.getOutput());

        // back to page 2
        pagedScreenSupport.decrementPage(extendedState);
        screen = pagedScreenSupport.createScreen(extendedState);
        assertEquals(expectedPage2, screen.getOutput());

        // back to page 1
        pagedScreenSupport.decrementPage(extendedState);
        screen = pagedScreenSupport.createScreen(extendedState);
        assertEquals(expectedPage1, screen.getOutput());
    }
}
