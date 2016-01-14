package com.github.alkedr.matchers.reporting;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.missing;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.normal;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.BROKEN;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class Extractors_ListElementExtractorTest {
    @Test
    public void nullItem() {
        assertReflectionEquals(missing(), new Extractors.ListElementExtractor(1).extractFrom(null));
    }

    @Test
    public void itemIsNotList() {
        ExtractingMatcher.Extractor.ExtractedValue actual = new Extractors.ListElementExtractor(1).extractFrom(new Object());
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("ClassCastException"));
        assertNull(actual.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void indexIsNegative() {
        new Extractors.ListElementExtractor(-1);
    }

    @Test
    public void indexIsGreaterThanSize() {
        assertReflectionEquals(missing(), new Extractors.ListElementExtractor(1).extractFrom(singletonList(1)));
    }

    @Test
    public void elementIsPresent() {
        assertReflectionEquals(normal("2", 2), new Extractors.ListElementExtractor(0).extractFrom(singletonList(2)));
    }
}
