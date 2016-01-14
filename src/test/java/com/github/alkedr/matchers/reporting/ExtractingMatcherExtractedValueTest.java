package com.github.alkedr.matchers.reporting;

public class ExtractingMatcherExtractedValueTest {
    /*@Test
    public void extractedValue_normalWithCustomValueAsString() {
        String valueAsString = "123";
        Object value = new Object();
        ExtractedValue extractor = normal(valueAsString, value);
        assertEquals(NORMAL, extractor.getStatus());
        assertSame(valueAsString, extractor.getValueAsString());
        assertSame(value, extractor.getValue());
    }

    // TODO: normalWithDefaultValueAsString

    @Test
    public void extractedValue_missing() {
        ExtractedValue extractor = missing();
        assertEquals(MISSING, extractor.getStatus());
        assertEquals("", extractor.getValueAsString());
        assertNull(extractor.getValue());
    }

    @Test
    public void extractedValue_brokenWithCustomErrorMessage() {
        String errorMessage = "123";
        ExtractedValue extractor = broken(errorMessage);
        assertEquals(BROKEN, extractor.getStatus());
        assertSame(errorMessage, extractor.getValueAsString());
        assertNull(extractor.getValue());
    }*/

    // TODO: brokenWithException
}
