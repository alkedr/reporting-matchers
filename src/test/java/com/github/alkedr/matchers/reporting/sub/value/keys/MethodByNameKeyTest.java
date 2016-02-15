package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.methodByNameKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MethodByNameKeyTest {
    @Test(expected = NullPointerException.class)
    public void nullMethod() {
        methodByNameKey(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArguments() {
        methodByNameKey("method1", (Object[]) null);
    }

    @Test
    public void asStringTest() {
        assertEquals("method1()", methodByNameKey("method1").asString());
        assertEquals("method1(1, 2, qwe)", methodByNameKey("method1", 1, 2, "qwe").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(methodByNameKey("method1").hashCode(), methodByNameKey("method1").hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(methodByNameKey("method1"), methodByNameKey("method1"));
        assertEquals(methodByNameKey("method1", 1), methodByNameKey("method1", 1));
        assertNotEquals(methodByNameKey("method1"), methodByNameKey("method2"));
        assertNotEquals(methodByNameKey("method1", 1), methodByNameKey("method1", 2));
    }
}
