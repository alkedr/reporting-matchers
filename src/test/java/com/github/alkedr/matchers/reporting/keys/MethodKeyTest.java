package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.keys.Keys.methodKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MethodKeyTest {
    private static final Method METHOD1;
    private static final Method METHOD2;

    static {
        try {
            METHOD1 = MyClass.class.getDeclaredMethod("method1");
            METHOD2 = MyClass.class.getDeclaredMethod("method2");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    @Test(expected = NullPointerException.class)
    public void nullMethod() {
        methodKey(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArguments() {
        methodKey(METHOD1, (Object[]) null);
    }

    @Test
    public void asStringTest() {
        assertEquals("method1()", methodKey(METHOD1).asString());
        assertEquals("method1(1, 2, qwe)", methodKey(METHOD1, 1, 2, "qwe").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(methodKey(METHOD1).hashCode(), methodKey(METHOD1).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(methodKey(METHOD1), methodKey(METHOD1));
        assertNotEquals(methodKey(METHOD1), methodKey(METHOD2));
        assertNotEquals(methodKey(METHOD1, 1), methodKey(METHOD1, 2));
    }


    private static class MyClass {
        private void method1() {}
        private void method2() {}
    }
}
