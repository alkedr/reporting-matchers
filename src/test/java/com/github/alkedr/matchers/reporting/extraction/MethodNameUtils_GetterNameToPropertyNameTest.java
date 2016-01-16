package com.github.alkedr.matchers.reporting.extraction;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.extraction.MethodNameUtils.createNameForGetterMethodInvocation;
import static org.junit.Assert.assertEquals;

public class MethodNameUtils_GetterNameToPropertyNameTest {
    @Test
    public void nullAndEmptyInput() {
        assertEquals("", createNameForGetterMethodInvocation(null));
        assertEquals("", createNameForGetterMethodInvocation(""));
    }

    @Test
    public void noChangeNecessary() {
        assertEquals("qwerty", createNameForGetterMethodInvocation("qwerty"));
        assertEquals("get", createNameForGetterMethodInvocation("get"));
        assertEquals("is", createNameForGetterMethodInvocation("is"));
        assertEquals("getaway", createNameForGetterMethodInvocation("getaway"));
        assertEquals("issue", createNameForGetterMethodInvocation("issue"));
    }

    @Test
    public void removeGet() {
        assertEquals("qwerTy", createNameForGetterMethodInvocation("getQwerTy"));
    }

    @Test
    public void removeIs() {
        assertEquals("qwerTy", createNameForGetterMethodInvocation("isQwerTy"));
    }
}
