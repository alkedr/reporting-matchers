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
        Keys.methodKey(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArguments() {
        Keys.methodKey(method1, (Object[]) null);
    }

    @Test
    public void asStringTest() {
        assertEquals("method1()", Keys.methodKey(method1).asString());
        assertEquals("method1(1, 2, qwe)", Keys.methodKey(method1, 1, 2, "qwe").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(Keys.methodKey(method1).hashCode(), Keys.methodKey(method1).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(Keys.methodKey(method1), Keys.methodKey(method1));
        assertNotEquals(Keys.methodKey(method1), Keys.methodKey(method2));
        assertNotEquals(Keys.methodKey(method1, 1), Keys.methodKey(method1, 2));
    }


    private static class MyClass {
        private void method1() {}
        private void method2() {}
    }
}
