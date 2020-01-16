package com.headstartech.sermo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Per Johansson
 */
public class Menu {

    private final Map<String, Object> inputTransitionKeyMap;
    private final Map<String, Object> inputItemKeyMap;
    private final String menu;

    public Menu(Map<String, Object> inputTransitionKeyMap, Map<String, Object> inputItemKeyMap, String menu) {
        this.inputTransitionKeyMap = inputTransitionKeyMap;
        this.inputItemKeyMap = inputItemKeyMap;
        this.menu = menu;
    }

    public Map<String, Object> getInputTransitionKeyMap() {
        return inputTransitionKeyMap;
    }

    public Map<String, Object> getInputItemKeyMap() {
        return inputItemKeyMap;
    }

    public String render() {
        return menu;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private List<MenuItem> menuItems = new ArrayList<>();

        public Builder withMenuItem(String label, Object transitionKey) {
            menuItems.add(new MenuItem(label, transitionKey));
            return this;
        }

        public Builder withMenuItem(String label, Object transitionKey, Object itemKey) {
            menuItems.add(new MenuItem(label, transitionKey, itemKey));
            return this;
        }

        public Menu build() {
            Map<String, Object> inputTransitionKeyMap = new HashMap<>();
            Map<String, Object> inputItemKeyMap = new HashMap<>();
            String menu = buildMenu(menuItems, inputTransitionKeyMap, inputItemKeyMap);
            return new Menu(inputTransitionKeyMap, inputItemKeyMap, menu);
        }

        private String buildMenu(List<MenuItem> menuItems, Map<String, Object> inputTransitionKeyMap, Map<String, Object> inputItemKeyMap) {
            StringBuilder sb = new StringBuilder();
            int i = 1;
            for(MenuItem menuItem : menuItems) {
                if(i > 1) {
                    sb.append("\n");
                }
                String input = String.format("%d", i);
                sb.append(String.format("%s. %s", input, menuItem.getLabel()));
                inputTransitionKeyMap.put(input, menuItem.getTransitionKey());
                inputItemKeyMap.put(input, menuItem.getItemKey());
                ++i;
            }
            return sb.toString();
        }


    }

}
