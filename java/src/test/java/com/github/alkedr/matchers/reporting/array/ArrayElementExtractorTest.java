package com.github.alkedr.matchers.reporting.array;

import com.github.alkedr.matchers.reporting.ExtractingMatcher;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.missing;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.normal;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.BROKEN;
import static com.github.alkedr.matchers.reporting.array.ReportingMatchersForArrays.createArrayElementExtractor;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ArrayElementExtractorTest {
    @Test
    public void nullItem() {
        assertReflectionEquals(missing(), createArrayElementExtractor(1).extractFrom(null));
    }

    @Test
    public void itemIsNotArray() {
        ExtractingMatcher.Extractor.ExtractedValue actual = createArrayElementExtractor(1).extractFrom(new Object());
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("ClassCastException"));
        assertNull(actual.getValue());
    }

    @Test
    public void indexIsOutOfBounds() {
        Object[] array = {1};
        assertReflectionEquals(missing(), createArrayElementExtractor(-1).extractFrom(array));
        assertReflectionEquals(missing(), createArrayElementExtractor(1).extractFrom(array));
    }

    @Test
    public void elementIsPresent() {
        assertReflectionEquals(normal("2", 2), createArrayElementExtractor(0).extractFrom(new Object[]{2}));
    }
}
