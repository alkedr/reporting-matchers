package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MethodByNameKeyTest {
    @Test(expected = NullPointerException.class)
    public void nullMethod() {
        new MethodByNameKey(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArguments() {
        new MethodByNameKey("method1", (Object[]) null);
    }

    @Test
    public void asStringTest() {
        assertEquals("method1()", new MethodByNameKey("method1").asString());
        assertEquals("method1(1, 2, qwe)", new MethodByNameKey("method1", 1, 2, "qwe").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(new MethodByNameKey("method1").hashCode(), new MethodByNameKey("method1").hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(new MethodByNameKey("method1"), new MethodByNameKey("method1"));
        assertNotEquals(new MethodByNameKey("method1"), new MethodByNameKey("method2"));
        assertNotEquals(new MethodByNameKey("method1", 1), new MethodByNameKey("method1", 2));
    }
}
