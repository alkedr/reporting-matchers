package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.keys.Keys.getterByNameKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class GetterByNameKeyTest {
    @Test(expected = NullPointerException.class)
    public void nullMethod() {
        getterByNameKey(null);
    }

    @Test
    public void asStringTest() {
        assertEquals("x", getterByNameKey("getX").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(getterByNameKey("getX").hashCode(), getterByNameKey("getX").hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(getterByNameKey("getX"), getterByNameKey("getX"));
        assertNotEquals(getterByNameKey("getX"), getterByNameKey("method2"));
    }


    private static class MyClass {
        private int getX() { return 0; }
        private void method2() {}
    }
}
