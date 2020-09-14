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

/**
 * @author Per Johansson
 */
public class DefaultScreenRenderer implements ScreenRenderer {


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
        sb.append("\n");
    }

    protected void renderText(StringBuilder sb, Text text) {
        sb.append(text.getText())
                .append("\n");

    }

    protected void renderMenuGroup(StringBuilder sb, InputMap.Builder inputMapBuilder, MenuGroup menuGroup) {
        int i = 0;
        for (MenuItem menuItem : menuGroup.getMenuItems()) {
            String input = getInput(i);
            String row = String.format("%s. %s", input, menuItem.getLabel());
            String elidedRow = TextElide.elidedString(row, menuGroup.getElide());
            sb.append(elidedRow);
            sb.append("\n");
            inputMapBuilder.addMapping(input, menuItem.getTransition(), menuItem.getItemObject());
            ++i;
        }
    }

    protected String getInput(int menuItemIndex) {
        return String.format("%d", menuItemIndex + 1);
    }

    protected void renderStaticMenuItem(StringBuilder sb, InputMap.Builder inputMapBuilder, StaticMenuItem staticMenuItem) {
        sb.append(String.format("%s %s\n", staticMenuItem.getInput(), staticMenuItem.getLabel()));
        inputMapBuilder.addMapping(staticMenuItem.getInput(), staticMenuItem.getTransition(), staticMenuItem.getItemObject());

    }

}
