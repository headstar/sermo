package com.headstartech.sermo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Per Johansson
 */
public class DefaultScreenRenderer implements ScreenRenderer {

    private Map<String, Object> inputTransitionKeyMap = new HashMap<>();
    private Map<String, Object> inputItemKeyMap = new HashMap<>();
    private StringBuilder sb = new StringBuilder();

    @Override
    public Map<String, Object> getInputTransitionKeyMap() {
        return inputTransitionKeyMap;
    }

    @Override
    public Map<String, Object> getInputItemKeyMap() {
        return inputItemKeyMap;
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
            inputTransitionKeyMap.put(input, menuItem.getTransitionKey());
            inputItemKeyMap.put(input, menuItem.getItemKey());
            ++i;
        }
    }

    @Override
    public void visit(StaticMenuItem staticMenuItem) {
        sb.append(String.format("%s %s\n", staticMenuItem.getInput(), staticMenuItem.getLabel()));
        inputTransitionKeyMap.put(staticMenuItem.getInput(), staticMenuItem.getTransitionKey());
        inputItemKeyMap.put(staticMenuItem.getInput(), staticMenuItem.getItemKey());
    }
}
