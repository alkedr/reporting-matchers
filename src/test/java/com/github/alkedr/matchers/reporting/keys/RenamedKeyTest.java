package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RenamedKeyTest {
    @Test(expected = NullPointerException.class)
    public void nullKey() {
        new RenamedKey<>(null, "");
    }

    @Test(expected = NullPointerException.class)
    public void nullName() {
        new RenamedKey<>(new ElementKey(0), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyName() {
        new RenamedKey<>(new ElementKey(0), "");
    }

    @Test
    public void asStringTest() {
        assertEquals("123", new RenamedKey<>(new ElementKey(0), "123").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(new RenamedKey<>(new ElementKey(0), "123").hashCode(), new RenamedKey<>(new ElementKey(0), "123").hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(new RenamedKey<>(new ElementKey(0), "123"), new RenamedKey<>(new ElementKey(0), "123"));
        assertNotEquals(new RenamedKey<>(new ElementKey(0), "123"), new RenamedKey<>(new ElementKey(1), "123"));
        assertNotEquals(new RenamedKey<>(new ElementKey(0), "123"), new RenamedKey<>(new ElementKey(0), "234"));
    }
}
