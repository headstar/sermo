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

import java.util.*;

/**
 * @author Per Johansson
 */
public class MenuGroup implements ScreenBlock {

    private final List<MenuItem> menuItems;
    private final TextElide elide;

    public MenuGroup(List<MenuItem> menuItems, TextElide elide) {
        Objects.requireNonNull(menuItems, "menuItems must be non-null");
        Objects.requireNonNull(elide, "elide must be non-null");
        this.menuItems = Collections.unmodifiableList(menuItems);
        this.elide = elide;
    }

    public MenuGroup(List<MenuItem> menuItems) {
        this(menuItems, new TextElide());
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public TextElide getElide() {
        return elide;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuGroup menuGroup = (MenuGroup) o;
        return menuItems.equals(menuGroup.menuItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuItems);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MenuGroup.class.getSimpleName() + "[", "]")
                .add("menuItems=" + menuItems)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<MenuItem> menuItems = new ArrayList<>();
        private TextElide elide = new TextElide();

        public Builder withMenuItem(MenuItem menuItem) {
            menuItems.add(menuItem);
            return this;
        }

        public Builder withMenuItem(String label, Object transitionKey) {
            menuItems.add(new MenuItem(label, transitionKey));
            return this;
        }

        public Builder withMenuItem(String label, Object transitionKey, Object itemKey) {
            menuItems.add(new MenuItem(label, transitionKey, itemKey));
            return this;
        }

        public Builder withElide(TextElide elide) {
            this.elide = elide;
            return this;
        }

        public MenuGroup build() {
            return new MenuGroup(menuItems, elide);
        }
    }

}
