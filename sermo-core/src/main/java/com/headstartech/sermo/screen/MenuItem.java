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

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Per Johansson
 */
public class MenuItem {

    private final String label;
    private final Object transitionId;
    private final Object itemData;

    public MenuItem(String label, Object transitionId, Object itemData) {
        Objects.requireNonNull(label, "label must be non-null");
        Objects.requireNonNull(transitionId, "transitionId must be non-null");
        this.transitionId = transitionId;
        this.label = label;
        this.itemData = itemData;
    }

    public MenuItem(String label, Object transitionId) {
        this(label, transitionId, null);
    }

    public Object getTransition() {
        return transitionId;
    }

    public String getLabel() {
        return label;
    }

    public Object getItemData() {
        return itemData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return label.equals(menuItem.label) &&
                transitionId.equals(menuItem.transitionId) &&
                Objects.equals(itemData, menuItem.itemData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, transitionId, itemData);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MenuItem.class.getSimpleName() + "[", "]")
                .add("label='" + label + "'")
                .add("transitionId=" + transitionId)
                .add("itemData=" + itemData)
                .toString();
    }
}
