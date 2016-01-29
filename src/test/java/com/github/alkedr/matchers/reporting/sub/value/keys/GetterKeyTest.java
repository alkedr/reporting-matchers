package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.getterKey;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.methodKey;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.renamedExtractableKey;
import static org.junit.Assert.assertEquals;

public class GetterKeyTest {
    private final Method getter = MyClass.class.getDeclaredMethod("getX");

    public GetterKeyTest() throws NoSuchMethodException {
    }

    @Test
    public void getterKeyTest() {
        assertEquals(renamedExtractableKey(methodKey(getter), "x"), getterKey(getter));
    }

    private static class MyClass {
        void getX() {}
    }
}
