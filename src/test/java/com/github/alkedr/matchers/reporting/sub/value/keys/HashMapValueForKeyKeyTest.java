package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.hashMapValueForKeyKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class HashMapValueForKeyKeyTest {
    @Test
    public void asStringTest() {
        assertEquals("123", hashMapValueForKeyKey(123).asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(hashMapValueForKeyKey(0).hashCode(), hashMapValueForKeyKey(0).hashCode());
        assertEquals(hashMapValueForKeyKey(null).hashCode(), hashMapValueForKeyKey(null).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(hashMapValueForKeyKey(0), hashMapValueForKeyKey(0));
        assertEquals(hashMapValueForKeyKey(null), hashMapValueForKeyKey(null));
        assertNotEquals(hashMapValueForKeyKey(0), hashMapValueForKeyKey(1));
    }
}
