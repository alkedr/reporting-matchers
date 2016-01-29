package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.getterByNameKey;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.methodByNameKey;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.renamedExtractableKey;
import static org.junit.Assert.assertEquals;

public class GetterByNameKeyTest {
    @Test
    public void getterByNameKeyTest() {
        assertEquals(renamedExtractableKey(methodByNameKey("getX"), "x"), getterByNameKey("getX"));
    }
}
