package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.broken;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.missing;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.normal;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.BROKEN;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.MISSING;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.NORMAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class ExtractingMatcherExtractedValueTest {
    @Test
    public void extractedValue_normalWithCustomValueAsString() {
        String valueAsString = "123";
        Object value = new Object();
        ExtractedValue<Object> extractor = normal(valueAsString, value);
        assertEquals(NORMAL, extractor.getStatus());
        assertSame(valueAsString, extractor.getValueAsString());
        assertSame(value, extractor.getValue());
    }

    // TODO: normalWithDefaultValueAsString

    @Test
    public void extractedValue_missing() {
        ExtractedValue<Object> extractor = missing();
        assertEquals(MISSING, extractor.getStatus());
        assertEquals("", extractor.getValueAsString());
        assertNull(extractor.getValue());
    }

    @Test
    public void extractedValue_brokenWithCustomErrorMessage() {
        String errorMessage = "123";
        ExtractedValue<Object> extractor = broken(errorMessage);
        assertEquals(BROKEN, extractor.getStatus());
        assertSame(errorMessage, extractor.getValueAsString());
        assertNull(extractor.getValue());
    }

    // TODO: brokenWithException
}
