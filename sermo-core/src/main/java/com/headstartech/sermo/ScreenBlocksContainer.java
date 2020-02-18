package com.headstartech.sermo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Per Johansson
 */
public class ScreenBlocksContainer implements ScreenBlock {

    private final List<ScreenBlock> screenBlocks;

    public ScreenBlocksContainer(List<ScreenBlock> screenBlocks) {
        Objects.requireNonNull("screenBlock must be non-null");
        this.screenBlocks = Collections.unmodifiableList(screenBlocks);
    }

    public List<ScreenBlock> getScreenBlocks() {
        return screenBlocks;
    }

    @Override
    public void accept(ScreenBlockVisitor visitor) {
        screenBlocks.forEach(e -> e.accept(visitor));
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
}
