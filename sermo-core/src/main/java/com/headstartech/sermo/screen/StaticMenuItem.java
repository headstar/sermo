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
public class StaticMenuItem extends MenuItem implements ScreenBlock {

    private final String input;

    public StaticMenuItem(String input, String label, Object transitionKey) {
        super(label, transitionKey);
        Objects.requireNonNull(input, "input must be non-null");
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    @Override
    public void accept(ScreenBlockVisitor visitor) {
        visitor.visit(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StaticMenuItem that = (StaticMenuItem) o;
        return input.equals(that.input);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), input);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StaticMenuItem.class.getSimpleName() + "[", "]")
                .add("input='" + input + "'")
                .toString();
    }
}
