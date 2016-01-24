package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

public class FieldKeyTest {
    private final Field myField1;
    private final Field myField2;

    public FieldKeyTest() throws NoSuchFieldException {
        myField1 = MyClass.class.getDeclaredField("myField1");
        myField2 = MyClass.class.getDeclaredField("myField2");
    }

    @Test(expected = NullPointerException.class)
    public void nullField() {
        new FieldKey(null);
    }

    @Test
    public void getters() {
        assertSame(myField1, new FieldKey(myField1).getField());
    }

    @Test
    public void asStringTest() {
        assertEquals("myField1", new FieldKey(myField1).asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(new FieldKey(myField1).hashCode(), new FieldKey(myField1).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(new FieldKey(myField1), new FieldKey(myField1));
        assertNotEquals(new FieldKey(myField1), new FieldKey(myField2));
    }


    private static class MyClass {
        private final int myField1 = 1;
        private final int myField2 = 2;
    }
}
