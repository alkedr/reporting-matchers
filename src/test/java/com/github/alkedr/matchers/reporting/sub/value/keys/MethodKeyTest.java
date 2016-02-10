package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.methodKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MethodKeyTest {
    private final Method method1 = MyClass.class.getDeclaredMethod("method1");
    private final Method method2 = MyClass.class.getDeclaredMethod("method2");

    public MethodKeyTest() throws NoSuchMethodException {
    }

    @Test(expected = NullPointerException.class)
    public void nullMethod() {
        methodKey(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArguments() {
        methodKey(method1, (Object[]) null);
    }

    @Test
    public void asStringTest() {
        assertEquals("method1()", methodKey(method1).asString());
        assertEquals("method1(1, 2, qwe)", methodKey(method1, 1, 2, "qwe").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(methodKey(method1).hashCode(), methodKey(method1).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(methodKey(method1), methodKey(method1));
        assertEquals(methodKey(method1, 1), methodKey(method1, 1));
        assertNotEquals(methodKey(method1), methodKey(method2));
        assertNotEquals(methodKey(method1, 1), methodKey(method1, 2));
    }

    private static class MyClass {
        private void method1() {}
        private void method2() {}
    }
}
