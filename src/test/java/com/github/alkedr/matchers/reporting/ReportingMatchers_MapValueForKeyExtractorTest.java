package com.github.alkedr.matchers.reporting;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.missing;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.normal;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.BROKEN;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.createValueForKeyExtractor;
import static java.util.Collections.singletonMap;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ReportingMatchers_MapValueForKeyExtractorTest {
    @Test
    public void nullItem() {
        assertReflectionEquals(missing(), createValueForKeyExtractor("1").extractFrom(null));
    }

    @Test
    public void itemIsNotMap() {
        ExtractingMatcher.Extractor.ExtractedValue actual = createValueForKeyExtractor("1").extractFrom(new Object());
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("ClassCastException"));
        assertNull(actual.getValue());
    }

    @Test
    public void keyIsMissing() {
        assertReflectionEquals(missing(), createValueForKeyExtractor("2").extractFrom(singletonMap("1", "q")));
    }

    @Test
    public void keyIsPresent() {
        assertReflectionEquals(normal("q", "q"), createValueForKeyExtractor("1").extractFrom(singletonMap("1", "q")));
    }

    @Test
    public void keyIsPresentButValueIsNull() {
        assertReflectionEquals(normal("null", null), createValueForKeyExtractor("1").extractFrom(singletonMap("1", null)));
    }
}
