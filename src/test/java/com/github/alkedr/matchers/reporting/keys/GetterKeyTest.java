package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.keys.Keys.getterKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class GetterKeyTest {
    private static final Method METHOD1;
    private static final Method METHOD2;

    static {
        try {
            METHOD1 = MyClass.class.getDeclaredMethod("getX");
            METHOD2 = MyClass.class.getDeclaredMethod("method2");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    @Test(expected = NullPointerException.class)
    public void nullMethod() {
        getterKey(null);
    }

    @Test
    public void asStringTest() {
        assertEquals("x", getterKey(METHOD1).asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(getterKey(METHOD1).hashCode(), getterKey(METHOD1).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(getterKey(METHOD1), getterKey(METHOD1));
        assertNotEquals(getterKey(METHOD1), getterKey(METHOD2));
    }

    // TODO: протестировать извлечение


    private static class MyClass {
        private int getX() { return 0; }
        private void method2() {}
    }
}
