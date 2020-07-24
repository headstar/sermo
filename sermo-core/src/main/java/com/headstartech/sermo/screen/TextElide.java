package com.headstartech.sermo.screen;

import java.util.Objects;

import static com.headstartech.sermo.screen.TextElide.Mode.NONE;
import static com.headstartech.sermo.screen.TextElide.Mode.RIGHT;

/**
 * Class representing elided text setting (i.e. a string with "..." in it)
 *
 */
public class TextElide {

    public enum Mode {
        NONE, RIGHT
    }

    private final Mode mode;
    private final int maxLen;

    public TextElide() {
        this(NONE, 0);
    }

    public TextElide(Mode mode, int maxLen) {
        Objects.requireNonNull(mode, "mode must be non-null");
        if(!NONE.equals(mode) && maxLen < 4) {
            throw new IllegalArgumentException("maxLen must be >= 4");
        }
        this.mode = mode;
        this.maxLen = maxLen;
    }

    public Mode getMode() {
        return mode;
    }

    public int getMaxLen() {
        return maxLen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextElide elide = (TextElide) o;
        return maxLen == elide.maxLen &&
                mode == elide.mode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode, maxLen);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TextElide{");
        sb.append("mode=").append(mode);
        sb.append(", maxLen=").append(maxLen);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Returns an elided version of the string (i.e. a string with "..." in it) if the string is wider than the max length.
     * Otherwise, returns the original string.
     *
     * Mode RIGHT specifies the text is elided on the right (e.g. "abcdefghijk" -> "abcd...").
     *
     * The width is specified in pixels, not characters.
     * @param str
     * @param elide
     * @return
     */
    public static String elidedString(String str, TextElide elide) {
        if(str == null || NONE.equals(elide.getMode())) {
            return str;
        }

        if (str.length() <= elide.getMaxLen()) {
            return str;
        }

        if(RIGHT.equals(elide.getMode())) {
            return str.substring(0, elide.getMaxLen() - 3) + "...";
        } else {
            throw new RuntimeException(String.format("Unknown elide mode: %s", elide.getMode()));
        }
    }
}
