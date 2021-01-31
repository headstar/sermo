package com.headstartech.sermo.screen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TextElideTest {

    @Test
    public void nullReturnedIfStringIsNull() {
        // given

        // when
        String elided = TextElide.elidedString(null, TextElide.NO_TEXT_ELIDE);

        // then
        assertNull(elided);
    }

    @Test
    public void originalStringReturnedIfElideModeNone() {
        // given
        String orig = "abcdefghijklmno";
        TextElide elide = TextElide.NO_TEXT_ELIDE;
        assertEquals(TextElideMode.NONE, elide.getMode());

        // when
        String elided = TextElide.elidedString(orig, elide);

        // then
        assertEquals(orig, elided);
    }

    @Test
    public void originalStringReturnedWhenLengthIsEqualToMax() {
        // given
        String orig = "abcde";
        TextElide elide = new TextElide(TextElideMode.RIGHT, orig.length());

        // when
        String elided = TextElide.elidedString(orig, elide);

        // then
        assertEquals(orig, elided);
    }

    @Test
    public void stringElidedWhenStringIsGreaterThanMaxLength() {
        // given
        String orig = "abcdefghijklmno";
        TextElide elide = new TextElide(TextElideMode.RIGHT, 6);

        // when
        String elided = TextElide.elidedString(orig, elide);

        // then
        assertEquals("abc...", elided);
    }

    @Test
    public void originalStringReturnedWhenLengthIsEqualToMaxUsing2Ellipsis() {
        // given
        String orig = "abcde";
        int maxLen = orig.length();
        TextElide elide = new TextElide(TextElideMode.RIGHT, maxLen, 2);

        // when
        String elided = TextElide.elidedString(orig, elide);

        // then
        assertEquals(orig, elided);
    }

    @Test
    public void stringElidedWithWhenStringIsGreaterThanMaxLengthUsing2Ellipsis() {
        // given
        String orig = "abcdefghijklmno";
        int maxLen = orig.length();
        TextElide elide = new TextElide(TextElideMode.RIGHT, 6, 2);

        // when
        String elided = TextElide.elidedString(orig, elide);

        // then
        assertEquals("abcd..", elided);
    }
}
