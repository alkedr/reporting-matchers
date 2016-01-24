package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RenamedKeyTest {
    @Test(expected = NullPointerException.class)
    public void nullKey() {
        Keys.renamedKey(null, "");
    }

    @Test(expected = NullPointerException.class)
    public void nullName() {
        Keys.renamedKey(Keys.elementKey(0), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyName() {
        Keys.renamedKey(Keys.elementKey(0), "");
    }

    @Test
    public void asStringTest() {
        assertEquals("123", Keys.renamedKey(Keys.elementKey(0), "123").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(Keys.renamedKey(Keys.elementKey(0), "123").hashCode(), Keys.renamedKey(Keys.elementKey(0), "123").hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(Keys.renamedKey(Keys.elementKey(0), "123"), Keys.renamedKey(Keys.elementKey(0), "123"));
        assertNotEquals(Keys.renamedKey(Keys.elementKey(0), "123"), Keys.renamedKey(Keys.elementKey(1), "123"));
        assertNotEquals(Keys.renamedKey(Keys.elementKey(0), "123"), Keys.renamedKey(Keys.elementKey(0), "234"));
    }
}
