package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ElementKeyTest {
    @Test(expected = IllegalArgumentException.class)
    public void negativeIndex() {
        new ElementKey(-1);
    }

    @Test
    public void getters() {
        assertEquals(123, new ElementKey(123).getIndex());
    }

    @Test
    public void asStringTest() {
        assertEquals("[1]", new ElementKey(0).asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(new ElementKey(0).hashCode(), new ElementKey(0).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(new ElementKey(0), new ElementKey(0));
        assertNotEquals(new ElementKey(0), new ElementKey(1));
    }
}
