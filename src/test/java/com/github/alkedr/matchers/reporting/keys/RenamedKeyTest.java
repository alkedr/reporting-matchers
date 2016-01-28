package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.keys.Keys.elementKey;
import static com.github.alkedr.matchers.reporting.keys.Keys.renamedExtractableKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RenamedKeyTest {
    @Test(expected = NullPointerException.class)
    public void nullKey() {
        renamedExtractableKey(null, "");
    }

    @Test(expected = NullPointerException.class)
    public void nullName() {
        renamedExtractableKey(elementKey(0), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyName() {
        renamedExtractableKey(elementKey(0), "");
    }

    @Test
    public void asStringTest() {
        assertEquals("123", renamedExtractableKey(elementKey(0), "123").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(renamedExtractableKey(elementKey(0), "123").hashCode(), renamedExtractableKey(elementKey(0), "123").hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(renamedExtractableKey(elementKey(0), "123"), renamedExtractableKey(elementKey(0), "123"));
        assertNotEquals(renamedExtractableKey(elementKey(0), "123"), renamedExtractableKey(elementKey(1), "123"));
        assertNotEquals(renamedExtractableKey(elementKey(0), "123"), renamedExtractableKey(elementKey(0), "234"));
    }

    @Test
    public void extractFromMethod() throws Exception {
        Object item = new Object();
        ExtractableKey key = mock(ExtractableKey.class);
        ExtractableKey.ExtractionResult extractionResult = new ExtractableKey.ExtractionResult(key, new Object());
        when(key.extractFrom(item)).thenReturn(extractionResult);
        assertSame(extractionResult, renamedExtractableKey(key, "123").extractFrom(item));
    }

    @Test
    public void extractFromMissingItemMethod() throws Exception {
        ExtractableKey key = mock(ExtractableKey.class);
        ExtractableKey.ExtractionResult extractionResult = new ExtractableKey.ExtractionResult(key, new Object());
        when(key.extractFromMissingItem()).thenReturn(extractionResult);
        assertSame(extractionResult, renamedExtractableKey(key, "123").extractFromMissingItem());
    }
}
