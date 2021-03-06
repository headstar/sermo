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

import java.util.Optional;

/**
 * @author Per Johansson
 */
public class DefaultScreenRenderer implements ScreenRenderer {

    private final String newline = "\n";

    @Override
    public ScreenRenderResult renderScreen(ScreenBlock screenBlock) {
        InputMap.Builder inputMapBuilder = InputMap.builder();
        StringBuilder sb = new StringBuilder();

        renderBlock(screenBlock, sb, inputMapBuilder);
        return new ScreenRenderResult(inputMapBuilder.build(), sb.toString());
    }

    protected void renderBlock(ScreenBlock screenBlock, StringBuilder sb, InputMap.Builder inputMapBuilder) {
        if(screenBlock instanceof ScreenBlocksContainer) {
            renderScreenBlocksContainer((ScreenBlocksContainer) screenBlock, sb, inputMapBuilder);
        } else if(screenBlock instanceof EmptyLine) {
            renderEmptyLine(sb);
        } else if(screenBlock instanceof Text) {
            renderText(sb, (Text) screenBlock);
        } else if(screenBlock instanceof MenuGroup) {
            renderMenuGroup(sb, inputMapBuilder, (MenuGroup) screenBlock);
        } else if(screenBlock instanceof StaticMenuItem) {
            renderStaticMenuItem(sb, inputMapBuilder, (StaticMenuItem) screenBlock);
        } else {
            throw new IllegalStateException(String.format("unknown ScreenBlock type %s", screenBlock.getClass().getName()));
        }
    }

    protected void renderScreenBlocksContainer(ScreenBlocksContainer screenBlocksContainer, StringBuilder sb, InputMap.Builder inputMapBuilder) {
        screenBlocksContainer.getScreenBlocks().forEach(e -> renderBlock(e, sb, inputMapBuilder));
    }

    protected void renderEmptyLine(StringBuilder sb) {
        appendNewline(sb);
    }

    protected void renderText(StringBuilder sb, Text text) {
        if(!text.getText().isEmpty()) {
            appendNewlineIfScreenNotEmpty(sb);
            sb.append(text.getText());
        }
    }

    protected void renderMenuGroup(StringBuilder sb, InputMap.Builder inputMapBuilder, MenuGroup menuGroup) {
        int i = 0;
        for (MenuItem menuItem : menuGroup.getMenuItems()) {
            if(i > 0) {
                appendNewline(sb);
            } else {
                appendNewlineIfScreenNotEmpty(sb);
            }
            String input = getInput(menuGroup.getInputNumberingStartsAt(), i);
            String row = renderMenuGroupItemRow(input, menuItem.getLabel());
            String elidedRow = TextElide.elidedString(row, menuGroup.getElide());
            sb.append(elidedRow);
            addInputMapping(inputMapBuilder, input, menuItem.getTransition(), menuItem.getItemObject());
            ++i;
        }
    }

    protected String renderMenuGroupItemRow(String input, String label) {
        return String.format("%s. %s", input, label);
    }

    protected String getInput(int inputNumberingStartsAt, int menuItemIndex) {
        return String.format("%d", inputNumberingStartsAt + menuItemIndex );
    }

    protected void renderStaticMenuItem(StringBuilder sb, InputMap.Builder inputMapBuilder, StaticMenuItem staticMenuItem) {
        appendNewlineIfScreenNotEmpty(sb);
        sb.append(renderStaticMenuItemRow(staticMenuItem.getInput(), staticMenuItem.getLabel()));
        addInputMapping(inputMapBuilder, staticMenuItem.getInput(), staticMenuItem.getTransition(), staticMenuItem.getItemObject());
    }

    protected String renderStaticMenuItemRow(String input, String label) {
        return String.format("%s %s", input, label);
    }

    protected void appendNewlineIfScreenNotEmpty(StringBuilder sb) {
        if(sb.length() > 0) {
            appendNewline(sb);
        }
    }

    protected void appendNewline(StringBuilder sb) {
        sb.append(newline);
    }

    protected void addInputMapping(InputMap.Builder inputMapBuilder, String input, Object transition, Optional<Object> itemObject) {
        if(itemObject.isPresent()) {
            inputMapBuilder.addMapping(input, transition, itemObject.get());
        } else {
            inputMapBuilder.addMapping(input, transition);
        }
    }

}
