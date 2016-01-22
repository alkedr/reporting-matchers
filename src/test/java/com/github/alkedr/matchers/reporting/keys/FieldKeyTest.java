package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.keys.Keys.fieldKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FieldKeyTest {
    private static final Field FIELD1;
    private static final Field FIELD2;

    static {
        try {
            FIELD1 = MyClass.class.getDeclaredField("field1");
            FIELD2 = MyClass.class.getDeclaredField("field2");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = NullPointerException.class)
    public void nullField() {
        fieldKey(null);
    }

    @Test
    public void asStringTest() {
        assertEquals("field1", fieldKey(FIELD1).asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(fieldKey(FIELD1).hashCode(), fieldKey(FIELD1).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(fieldKey(FIELD1), fieldKey(FIELD1));
        assertNotEquals(fieldKey(FIELD1), fieldKey(FIELD2));
    }


    private static class MyClass {
        private final int field1 = 1;
        private final int field2 = 2;
    }
}
