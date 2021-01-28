package com.headstartech.sermo.screen;

import java.util.Objects;

import static com.headstartech.sermo.screen.TextElide.Mode.NONE;
import static com.headstartech.sermo.screen.TextElide.Mode.RIGHT;

/**
 * Class representing elided text setting (i.e. a string with "..." in it)
 *
 */
public class TextElide {

    public static final TextElide NO_TEXT_ELIDE = new TextElide(NONE, 0, 3);

    public enum Mode {
        NONE, RIGHT
    }

    private static final int DEFAULT_NUM_ELLIPSIS = 3;

    private final Mode mode;
    private final int maxLen;
    private final int numEllipsis;

    public TextElide() {
        this(NONE, 0, 3);
    }

    public TextElide(Mode mode, int maxLen) {
        this(mode, maxLen, DEFAULT_NUM_ELLIPSIS);
    }

    public TextElide(Mode mode, int maxLen, int numEllipsis) {
        Objects.requireNonNull(mode, "mode must be non-null");
        if(numEllipsis < 2 && numEllipsis > 3) {
            throw new IllegalArgumentException("numEllipsis must be 2 or 3");
        }
        if(!NONE.equals(mode)) {
            int maxLenMin = (1 + numEllipsis);
            if(maxLen < maxLenMin) {
                throw new IllegalArgumentException(String.format("maxLen must be >= %s", maxLenMin));
            }
        }
        this.mode = mode;
        this.maxLen = maxLen;
        this.numEllipsis = numEllipsis;
    }

    public Mode getMode() {
        return mode;
    }

    public int getMaxLen() {
        return maxLen;
    }

    public int getNumEllipsis() {
        return numEllipsis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextElide textElide = (TextElide) o;
        return maxLen == textElide.maxLen &&
                numEllipsis == textElide.numEllipsis &&
                mode == textElide.mode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode, maxLen, numEllipsis);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TextElide{");
        sb.append("mode=").append(mode);
        sb.append(", maxLen=").append(maxLen);
        sb.append(", numEllipsis=").append(numEllipsis);
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
            return str.substring(0, elide.getMaxLen() - elide.getNumEllipsis()) + repeat(".", elide.getNumEllipsis());
        } else {
            throw new RuntimeException(String.format("Unknown elide mode: %s", elide.getMode()));
        }
    }

    private static String repeat(String str, int n) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<n; ++i) {
            sb.append(str);
        }
        return sb.toString();
    }
}
