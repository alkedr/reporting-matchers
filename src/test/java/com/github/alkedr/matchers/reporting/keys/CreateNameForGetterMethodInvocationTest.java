package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.keys.MethodNameUtils.createNameForGetterMethodInvocation;
import static org.junit.Assert.assertEquals;

public class CreateNameForGetterMethodInvocationTest {
    @Test
    public void nullAndEmptyInput() {
        assertEquals("", createNameForGetterMethodInvocation(null));
        verifyNotChanged("");
    }

    @Test
    public void noChangeNecessary() {
        verifyNotChanged("qwerty");
        verifyNotChanged("get");
        verifyNotChanged("is");
        verifyNotChanged("getaway");
        verifyNotChanged("issue");
        verifyNotChanged("is_x");
        verifyNotChanged("get_x");
        verifyNotChanged("ISX");
        verifyNotChanged("GETX");
    }

    @Test
    public void removeGet() {
        assertEquals("qwerTy", createNameForGetterMethodInvocation("getQwerTy"));
    }

    @Test
    public void removeIs() {
        assertEquals("qwerTy", createNameForGetterMethodInvocation("isQwerTy"));
    }


    private static void verifyNotChanged(String methodName) {
        assertEquals(methodName, createNameForGetterMethodInvocation(methodName));
    }
}
