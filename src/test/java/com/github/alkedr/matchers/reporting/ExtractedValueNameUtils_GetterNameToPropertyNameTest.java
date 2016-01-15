package com.github.alkedr.matchers.reporting;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.extraction.ExtractedValueNameUtils.getterNameToPropertyName;
import static org.junit.Assert.assertEquals;

public class ExtractedValueNameUtils_GetterNameToPropertyNameTest {
    @Test
    public void nullAndEmptyInput() {
        assertEquals("", getterNameToPropertyName(null));
        assertEquals("", getterNameToPropertyName(""));
    }

    @Test
    public void noChangeNecessary() {
        assertEquals("qwerty", getterNameToPropertyName("qwerty"));
        assertEquals("get", getterNameToPropertyName("get"));
        assertEquals("is", getterNameToPropertyName("is"));
        assertEquals("getaway", getterNameToPropertyName("getaway"));
        assertEquals("issue", getterNameToPropertyName("issue"));
    }

    @Test
    public void removeGet() {
        assertEquals("qwerTy", getterNameToPropertyName("getQwerTy"));
    }

    @Test
    public void removeIs() {
        assertEquals("qwerTy", getterNameToPropertyName("isQwerTy"));
    }
}
