package com.headstartech.sermo.screen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TextElideTest {

    @Test
    public void nullReturnedIfStringIsNull() {
        // given

        // when
        String elided = TextElide.elidedString(null, new TextElide());

        // then
        assertNull(elided);
    }

    @Test
    public void originalStringReturnedIfElideModeNone() {
        // given
        String orig = "abcdefghijklmno";
        TextElide elide = new TextElide();
        assertEquals(TextElide.Mode.NONE, elide.getMode());

        // when
        String elided = TextElide.elidedString(orig, elide);

        // then
        assertEquals(orig, elided);
    }

    @Test
    public void originalStringReturnedWhenLengthIsEqualToMax() {
        // given
        String orig = "abcde";
        TextElide elide = new TextElide(TextElide.Mode.RIGHT, orig.length());

        // when
        String elided = TextElide.elidedString(orig, elide);

        // then
        assertEquals(orig, elided);
    }

    @Test
    public void stringElidedWhenStringIsGreaterThanMaxLength() {
        // given
        String orig = "abcdefghijklmno";
        TextElide elide = new TextElide(TextElide.Mode.RIGHT, 6);

        // when
        String elided = TextElide.elidedString(orig, elide);

        // then
        assertEquals("abc...", elided);
    }
}
