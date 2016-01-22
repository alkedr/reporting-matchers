package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.keys.Keys.hashMapKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class HashMapKeyTest {
    @Test
    public void asStringTest() {
        assertEquals("123", hashMapKey("123").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(hashMapKey(0).hashCode(), hashMapKey(0).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(hashMapKey(0), hashMapKey(0));
        assertNotEquals(hashMapKey(0), hashMapKey(1));
    }
}
