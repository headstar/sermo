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

package com.headstartech.sermo;

import com.headstartech.sermo.screen.*;

/**
 * @author Per Johansson
 */
public class DefaultScreenRenderer implements ScreenRenderer {

    private InputMap inputMap = new InputMap();
    private StringBuilder sb = new StringBuilder();

    @Override
    public InputMap getInputMap() {
        return inputMap;
    }

    @Override
    public String getScreenOutput() {
        return sb.toString();
    }

    @Override
    public void visit(EmptyLine emptyLine) {
        sb.append("\n");
    }

    @Override
    public void visit(Text text) {
        sb.append(text.getText())
                .append("\n");
    }

    @Override
    public void visit(MenuGroup menuGroup) {
        int i = 1;
        for (MenuItem menuItem : menuGroup.getMenuItems()) {
            String input = String.format("%d", i);
            sb.append(String.format("%s. %s\n", input, menuItem.getLabel()));
            inputMap.addMapping(input, menuItem.getTransition(), menuItem.getItemData());
            ++i;
        }
    }

    @Override
    public void visit(StaticMenuItem staticMenuItem) {
        sb.append(String.format("%s %s\n", staticMenuItem.getInput(), staticMenuItem.getLabel()));
        inputMap.addMapping(staticMenuItem.getInput(), staticMenuItem.getTransition(), staticMenuItem.getItemData());
    }
}
