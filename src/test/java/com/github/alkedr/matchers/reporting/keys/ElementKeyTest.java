package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.keys.Keys.elementKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ElementKeyTest {
    @Test(expected = IllegalArgumentException.class)
    public void negativeIndex() {
        elementKey(-1);
    }

    @Test
    public void asStringTest() {
        assertEquals("[1]", elementKey(0).asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(elementKey(0).hashCode(), elementKey(0).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(elementKey(0), elementKey(0));
        assertNotEquals(elementKey(0), elementKey(1));
    }
}
