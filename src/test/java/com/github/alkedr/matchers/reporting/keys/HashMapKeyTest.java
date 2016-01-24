package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

public class HashMapKeyTest {
    @Test
    public void getters() {
        assertSame("123", new HashMapKey("123").getKey());
    }

    @Test
    public void asStringTest() {
        assertEquals("123", new HashMapKey(123).asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(new HashMapKey(0).hashCode(), new HashMapKey(0).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(new HashMapKey(0), new HashMapKey(0));
        assertNotEquals(new HashMapKey(0), new HashMapKey(1));
    }
}
