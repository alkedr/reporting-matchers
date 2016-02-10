package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.renamedKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RenamedKeyTest {
    @Test(expected = NullPointerException.class)
    public void nullKey() {
        renamedKey(null, "");
    }

    @Test(expected = NullPointerException.class)
    public void nullName() {
        renamedKey(elementKey(0), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyName() {
        renamedKey(elementKey(0), "");
    }

    @Test
    public void asStringTest() {
        assertEquals("1", renamedKey(elementKey(0), "1").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(renamedKey(elementKey(0), "1").hashCode(), renamedKey(elementKey(0), "1").hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(renamedKey(elementKey(0), "1"), renamedKey(elementKey(0), "1"));
        assertNotEquals(renamedKey(elementKey(0), "1"), renamedKey(elementKey(1), "1"));
        assertNotEquals(renamedKey(elementKey(0), "1"), renamedKey(elementKey(0), "2"));
    }
}
