package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MethodKeyTest {
    private final Method method1;
    private final Method method2;

    public MethodKeyTest() throws NoSuchMethodException {
        method1 = MyClass.class.getDeclaredMethod("method1");
        method2 = MyClass.class.getDeclaredMethod("method2");
    }

    @Test(expected = NullPointerException.class)
    public void nullMethod() {
        new MethodKey(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArguments() {
        new MethodKey(method1, (Object[]) null);
    }

    @Test
    public void asStringTest() {
        assertEquals("method1()", new MethodKey(method1).asString());
        assertEquals("method1(1, 2, qwe)", new MethodKey(method1, 1, 2, "qwe").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(new MethodKey(method1).hashCode(), new MethodKey(method1).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(new MethodKey(method1), new MethodKey(method1));
        assertNotEquals(new MethodKey(method1), new MethodKey(method2));
        assertNotEquals(new MethodKey(method1, 1), new MethodKey(method1, 2));
    }


    private static class MyClass {
        private void method1() {}
        private void method2() {}
    }
}
