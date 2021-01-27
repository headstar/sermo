/*
 *  Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.headstartech.sermo.screen;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Per Johansson
 */
public class DefaultScreenRendererTest {

    @Test
    public void canRenderEmptyLine() {
        // given
        ScreenRenderer sr = new DefaultScreenRenderer();
        String expectedOutput = "\n";

        // when
        ScreenRenderResult srr = sr.renderScreen(EmptyLine.INSTANCE);

        // then
        assertEquals(expectedOutput, srr.getOutput());

        assertTrue(srr.getInputMap().isEmpty());
    }

    @Test
    public void canRenderText() {
        // given
        ScreenRenderer sr = new DefaultScreenRenderer();
        String text = "Apple banana";
        Text textItem = new Text(text);
        String expectedOutput = text;

        // when
        ScreenRenderResult srr = sr.renderScreen(textItem);

        // then
        assertEquals(expectedOutput, srr.getOutput());

        assertTrue(srr.getInputMap().isEmpty());
    }

    @Test
    public void canRenderStaticMenuItem() {
        // given
        ScreenRenderer sr = new DefaultScreenRenderer();
        String label = "Accounts";
        String input = "3";
        String transitionId = "foo";
        StaticMenuItem staticMenuItem = new StaticMenuItem(input, label, transitionId);
        String expectedOutput = input + " " + label;

        // when
        ScreenRenderResult srr = sr.renderScreen(staticMenuItem);

        // then
        assertEquals(expectedOutput, srr.getOutput());

        InputMap im  = srr.getInputMap();
        assertFalse(im.isEmpty());
        assertTrue(im.hasTransitionForInput(transitionId, input));
        assertFalse(im.getItemObjectForInput(input).isPresent());
    }

    @Test
    public void canRenderMenuGroup() {
        // given
        ScreenRenderer sr = new DefaultScreenRenderer();

        MenuItem mi1 = new MenuItem("Option 1", "tr1", "myItem1");
        MenuItem mi2 = new MenuItem("Option 2", "tr2", "myItem2");

        String expectedOutput =
                "1. " + mi1.getLabel() + "\n" +
                "2. " + mi2.getLabel();

        MenuGroup mg = new MenuGroup(Arrays.asList(mi1, mi2));

        // when
        ScreenRenderResult srr = sr.renderScreen(mg);

        // then
        assertEquals(expectedOutput, srr.getOutput());

        InputMap im  = srr.getInputMap();
        assertFalse(im.isEmpty());

        assertTrue(im.hasTransitionForInput(mi1.getTransition(), "1"));
        assertTrue(im.getItemObjectForInput("1").isPresent());
        assertEquals("myItem1", im.getItemObjectForInput("1").get());

        assertTrue(im.hasTransitionForInput(mi2.getTransition(), "2"));
        assertTrue(im.getItemObjectForInput("2").isPresent());
        assertEquals("myItem2", im.getItemObjectForInput("2").get());
    }

    @Test
    public void emptyMenuGroupRendersEmptyString() {
        // given
        ScreenRenderer sr = new DefaultScreenRenderer();

        MenuGroup mg = new MenuGroup(Arrays.asList());

        // when
        ScreenRenderResult srr = sr.renderScreen(mg);

        // then
        assertEquals("", srr.getOutput());

        InputMap im  = srr.getInputMap();
        assertTrue(im.isEmpty());
    }

    @Test
    public void canRenderMenuGroupWithElidedText() {
        // given
        ScreenRenderer sr = new DefaultScreenRenderer();

        MenuItem mi1 = new MenuItem("ABCDEFGHI", "tr1", "myItem1");
        MenuItem mi2 = new MenuItem("JKLMNOPQR", "tr2", "myItem2");

        TextElide textElide = new TextElide(TextElide.Mode.RIGHT, 9);
        int itemNumberingStartsAt = 1;

        String expectedOutput = "1. " + "ABC..." + "\n" +
                        "2. " + "JKL...";

        MenuGroup mg = new MenuGroup(Arrays.asList(mi1, mi2), textElide, itemNumberingStartsAt);

        // when
        ScreenRenderResult srr = sr.renderScreen(mg);

        // then
        assertEquals(expectedOutput, srr.getOutput());

        InputMap im  = srr.getInputMap();
        assertFalse(im.isEmpty());

        assertTrue(im.hasTransitionForInput(mi1.getTransition(), "1"));
        assertTrue(im.getItemObjectForInput("1").isPresent());
        assertEquals("myItem1", im.getItemObjectForInput("1").get());

        assertTrue(im.hasTransitionForInput(mi2.getTransition(), "2"));
        assertTrue(im.getItemObjectForInput("2").isPresent());
        assertEquals("myItem2", im.getItemObjectForInput("2").get());
    }

    @Test
    public void canScreenBlocksContainer() {
        // given
        ScreenRenderer sr = new DefaultScreenRenderer();

        String text1 = "Apple banana";
        Text textItem1 = new Text(text1);

        String text2 = "orange pear";
        Text textItem2 = new Text(text2);

        MenuItem mi1 = new MenuItem("ABCDEFGHI", "tr1", "myItem1");
        MenuGroup mg = new MenuGroup(Arrays.asList(mi1));

        ScreenBlocksContainer sbc = new ScreenBlocksContainer(
                Arrays.asList(textItem1, textItem2, mg, EmptyLine.INSTANCE));

        String expectedOutput = text1 + "\n" + text2 + "\n" + "1. " + mi1.getLabel() + "\n";

        // when
        ScreenRenderResult srr = sr.renderScreen(sbc);

        // then
        assertEquals(expectedOutput, srr.getOutput());
    }

    @Test
    public void canRenderMenuGroupWithItemNumberingGreaterThanOne() {
        // given
        ScreenRenderer sr = new DefaultScreenRenderer();

        MenuItem mi1 = new MenuItem("Option 1", "tr1", "myItem1");
        MenuItem mi2 = new MenuItem("Option 2", "tr2", "myItem2");

        MenuGroup mg = MenuGroup.builder()
                .withMenuItem(mi1)
                .withMenuItem(mi2)
                .withInputNumberingStartAt(7)
                .build();

        String expectedOutput =
                "7. " + mi1.getLabel() + "\n" +
                        "8. " + mi2.getLabel();

        // when
        ScreenRenderResult srr = sr.renderScreen(mg);

        // then
        assertEquals(expectedOutput, srr.getOutput());
    }

}
