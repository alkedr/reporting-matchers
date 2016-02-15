package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.junit.Test;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.fieldKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FieldKeyTest {
    private final Field field1 = MyClass.class.getDeclaredField("field1");
    private final Field field2 = MyClass.class.getDeclaredField("field2");

    public FieldKeyTest() throws NoSuchFieldException {
    }

    @Test(expected = NullPointerException.class)
    public void nullField() {
        fieldKey(null);
    }

    @Test
    public void asStringTest() {
        assertEquals("field1", fieldKey(field1).asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(fieldKey(field1).hashCode(), fieldKey(field1).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(fieldKey(field1), fieldKey(field1));
        assertNotEquals(fieldKey(field1), fieldKey(field2));
    }

    private static class MyClass {
        private final int field1 = 1;
        private final int field2 = 2;
    }
}
