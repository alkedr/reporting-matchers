package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RenamedExtractableKeyTest {
    @Test(expected = NullPointerException.class)
    public void nullKey() {
        Keys.renamedExtractableKey(null, "");
    }

    @Test(expected = NullPointerException.class)
    public void nullName() {
        Keys.renamedExtractableKey(Keys.elementKey(0), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyName() {
        Keys.renamedExtractableKey(Keys.elementKey(0), "");
    }

    @Test
    public void asStringTest() {
        assertEquals("123", Keys.renamedExtractableKey(Keys.elementKey(0), "123").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(Keys.renamedExtractableKey(Keys.elementKey(0), "123").hashCode(), Keys.renamedExtractableKey(Keys.elementKey(0), "123").hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(Keys.renamedExtractableKey(Keys.elementKey(0), "123"), Keys.renamedExtractableKey(Keys.elementKey(0), "123"));
        assertNotEquals(Keys.renamedExtractableKey(Keys.elementKey(0), "123"), Keys.renamedExtractableKey(Keys.elementKey(1), "123"));
        assertNotEquals(Keys.renamedExtractableKey(Keys.elementKey(0), "123"), Keys.renamedExtractableKey(Keys.elementKey(0), "234"));
    }
}
