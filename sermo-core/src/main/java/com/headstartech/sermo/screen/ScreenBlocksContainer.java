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
public class ScreenBlocksContainer implements ScreenBlock {

    private final List<ScreenBlock> screenBlocks;

    public ScreenBlocksContainer(List<ScreenBlock> screenBlocks) {
        this.screenBlocks = new ArrayList<>(screenBlocks);
    }

    public List<ScreenBlock> getScreenBlocks() {
        return Collections.unmodifiableList(screenBlocks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScreenBlocksContainer that = (ScreenBlocksContainer) o;
        return screenBlocks.equals(that.screenBlocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(screenBlocks);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ScreenBlocksContainer.class.getSimpleName() + "[", "]")
                .add("screenBlocks=" + screenBlocks)
                .toString();
    }
}
