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

import javax.security.auth.callback.TextInputCallback;
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
        ScreenBlocksContainer sbc = new ScreenBlocksContainer(Arrays.asList(EmptyLine.INSTANCE));
        String expectedOutput = "\n";

        // when
        ScreenRenderResult srr = sr.renderScreen(sbc);

        // then
        assertTrue(srr.getInputMap().isEmpty());
        assertEquals(expectedOutput, srr.getOutput());
    }

    @Test
    public void canRenderText() {
        // given
        ScreenRenderer sr = new DefaultScreenRenderer();
        String text = "Apple banana";
        Text textItem = new Text(text);
        ScreenBlocksContainer sbc = new ScreenBlocksContainer(Arrays.asList(textItem));
        String expectedOutput = text + "\n";

        // when
        ScreenRenderResult srr = sr.renderScreen(sbc);

        // then
        assertTrue(srr.getInputMap().isEmpty());
        assertEquals(expectedOutput, srr.getOutput());
    }

    @Test
    public void canRenderStaticMenuItem() {
        // given
        ScreenRenderer sr = new DefaultScreenRenderer();
        String label = "Accounts";
        String input = "3";
        String transitionId = "foo";
        StaticMenuItem staticMenuItem = new StaticMenuItem(input, label, transitionId);
        ScreenBlocksContainer sbc = new ScreenBlocksContainer(Arrays.asList(staticMenuItem));
        String expectedOutput = input + " " + label + "\n";

        // when
        ScreenRenderResult srr = sr.renderScreen(sbc);

        // then
        InputMap im  = srr.getInputMap();
        assertFalse(im.isEmpty());
        assertTrue(im.hasTransitionForInput(transitionId, input));
        assertFalse(im.getItemDataForInput(input).isPresent());
        assertEquals(expectedOutput, srr.getOutput());
    }

}
