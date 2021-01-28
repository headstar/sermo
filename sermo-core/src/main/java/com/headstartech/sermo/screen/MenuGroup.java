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

    private static int DEFAULT_INPUT_NUMBERING_STARTS_AT = 1;

    private final List<MenuItem> menuItems;
    private final TextElide elide;
    private final int inputNumberingStartsAt;

    public MenuGroup(List<MenuItem> menuItems, TextElide elide, int inputNumberingStartsAt) {
        Objects.requireNonNull(menuItems, "menuItems must be non-null");
        Objects.requireNonNull(elide, "elide must be non-null");
        this.menuItems = Collections.unmodifiableList(menuItems);
        this.elide = elide;
        this.inputNumberingStartsAt = inputNumberingStartsAt;
    }

    public MenuGroup(List<MenuItem> menuItems) {
        this(menuItems, new TextElide(), DEFAULT_INPUT_NUMBERING_STARTS_AT);
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public TextElide getElide() {
        return elide;
    }

    public int getInputNumberingStartsAt() {
        return inputNumberingStartsAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuGroup menuGroup = (MenuGroup) o;
        return inputNumberingStartsAt == menuGroup.inputNumberingStartsAt &&
                Objects.equals(menuItems, menuGroup.menuItems) &&
                Objects.equals(elide, menuGroup.elide);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuItems, elide, inputNumberingStartsAt);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MenuGroup{");
        sb.append("menuItems=").append(menuItems);
        sb.append(", elide=").append(elide);
        sb.append(", inputNumberingStartsAt=").append(inputNumberingStartsAt);
        sb.append('}');
        return sb.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<MenuItem> menuItems = new ArrayList<>();
        private TextElide elide = TextElide.NO_TEXT_ELIDE;
        private int inputNumberingStartsAt = DEFAULT_INPUT_NUMBERING_STARTS_AT;

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

        public Builder withInputNumberingStartAt(int inputNumberingStartsAt) {
            this.inputNumberingStartsAt = inputNumberingStartsAt;
            return this;
        }

        public MenuGroup build() {
            return new MenuGroup(menuItems, elide, inputNumberingStartsAt);
        }
    }

}
