package com.headstartech.sermo.screen;

import com.headstartech.sermo.statemachine.actions.PagedMenuItemsUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultPagedScreenSetupTest {

    @Test
    public void canGetScreenSetup() {
        // given
        Text headerBlock = new Text("Header");
        Text footerBlock = new Text("Footer");
        NextPageMenuItem nextPageMenuItem = new NextPageMenuItem("0", "Next page");
        PreviousPageMenuItem previousPageMenuItem = new PreviousPageMenuItem("#", "Previous page");
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("A", "tA", "itemA"));
        items.add(new MenuItem("B", "tB", "itemB"));
        items.add(new MenuItem("C", "tC", "itemC"));
        items.add(new MenuItem("D", "tD", "itemD"));
        List<ScreenBlock> pages = PagedMenuItemsUtil.getScreenBlockForMenuItems(items, 2, new TextElide());
        assertEquals(2, pages.size());    // 2 items per page

        PagedScreenSetup pagedScreenSetup = DefaultPagedScreenSetup.builder()
                .withPages(pages)
                .withHeaderBlock(headerBlock)
                .withFooterBlock(footerBlock)
                .withNextPageMenuItem(nextPageMenuItem)
                .withPreviousPageMenuItem(previousPageMenuItem)
                .build();

        // when / then

          // first page
        assertFalse(pagedScreenSetup.hasPreviousPage());
        assertTrue(pagedScreenSetup.hasNextPage());
        assertSame(pages.get(0), pagedScreenSetup.getScreenBlockForCurrentPage());
        assertSame(headerBlock, pagedScreenSetup.getHeaderBlock());
        assertSame(footerBlock, pagedScreenSetup.getFooterBlock());
        assertSame(nextPageMenuItem, pagedScreenSetup.getNextPageMenuItem());
        assertSame(previousPageMenuItem, pagedScreenSetup.getPreviousPageMenuItem());

          // second page
        pagedScreenSetup.incrementPage();

        assertTrue(pagedScreenSetup.hasPreviousPage());
        assertFalse(pagedScreenSetup.hasNextPage());
        assertSame(pages.get(1), pagedScreenSetup.getScreenBlockForCurrentPage());
        assertSame(headerBlock, pagedScreenSetup.getHeaderBlock());
        assertSame(footerBlock, pagedScreenSetup.getFooterBlock());
        assertSame(nextPageMenuItem, pagedScreenSetup.getNextPageMenuItem());
        assertSame(previousPageMenuItem, pagedScreenSetup.getPreviousPageMenuItem());
    }

    @Test
    public void canGetScreenSetupWithDifferentFirstPageHeaderAndFooter() {
        // given
        Text headerBlock = new Text("Header");
        Text footerBlock = new Text("Footer");
        Text firstPageHeaderBlock = new Text("Header F");
        Text firstPageFooterBlock = new Text("Footer F");

        NextPageMenuItem nextPageMenuItem = new NextPageMenuItem("0", "Next page");
        PreviousPageMenuItem previousPageMenuItem = new PreviousPageMenuItem("#", "Previous page");
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("A", "tA", "itemA"));
        items.add(new MenuItem("B", "tB", "itemB"));
        items.add(new MenuItem("C", "tC", "itemC"));
        items.add(new MenuItem("D", "tD", "itemD"));
        List<ScreenBlock> pages = PagedMenuItemsUtil.getScreenBlockForMenuItems(items, 2, new TextElide());
        assertEquals(2, pages.size());    // 2 items per page

        PagedScreenSetup pagedScreenSetup = DefaultPagedScreenSetup.builder()
                .withPages(pages)
                .withHeaderBlock(headerBlock)
                .withFooterBlock(footerBlock)
                .withFirstPageHeaderBlock(firstPageHeaderBlock)
                .withFirstPageFooterBlock(firstPageFooterBlock)
                .withNextPageMenuItem(nextPageMenuItem)
                .withPreviousPageMenuItem(previousPageMenuItem)
                .build();

        // when / then

        // first page
        assertFalse(pagedScreenSetup.hasPreviousPage());
        assertTrue(pagedScreenSetup.hasNextPage());
        assertSame(pages.get(0), pagedScreenSetup.getScreenBlockForCurrentPage());
        assertSame(firstPageHeaderBlock, pagedScreenSetup.getHeaderBlock());
        assertSame(firstPageFooterBlock, pagedScreenSetup.getFooterBlock());
        assertSame(nextPageMenuItem, pagedScreenSetup.getNextPageMenuItem());
        assertSame(previousPageMenuItem, pagedScreenSetup.getPreviousPageMenuItem());

        // second page
        pagedScreenSetup.incrementPage();

        assertTrue(pagedScreenSetup.hasPreviousPage());
        assertFalse(pagedScreenSetup.hasNextPage());
        assertSame(pages.get(1), pagedScreenSetup.getScreenBlockForCurrentPage());
        assertSame(headerBlock, pagedScreenSetup.getHeaderBlock());
        assertSame(footerBlock, pagedScreenSetup.getFooterBlock());
        assertSame(nextPageMenuItem, pagedScreenSetup.getNextPageMenuItem());
        assertSame(previousPageMenuItem, pagedScreenSetup.getPreviousPageMenuItem());
    }
}
