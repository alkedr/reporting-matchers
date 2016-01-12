package com.github.alkedr.matchers.reporting.list;

import com.github.alkedr.matchers.reporting.ExtractingMatcher;
import org.junit.Test;

import java.util.List;

import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.missing;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.normal;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.BROKEN;
import static com.github.alkedr.matchers.reporting.list.ReportingMatchersForLists.createListElementExtractor;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ListElementExtractorTest {
    @Test
    public void nullItem() {
        assertReflectionEquals(missing(), createListElementExtractor(1).extractFrom(null));
    }

    @Test
    public void itemIsNotList() {
        ExtractingMatcher.Extractor.ExtractedValue actual = createListElementExtractor(1).extractFrom(new Object());
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("ClassCastException"));
        assertNull(actual.getValue());
    }

    @Test
    public void indexIsOutOfBounds() {
        List<?> list = singletonList(1);
        assertReflectionEquals(missing(), createListElementExtractor(-1).extractFrom(list));
        assertReflectionEquals(missing(), createListElementExtractor(1).extractFrom(list));
    }

    @Test
    public void elementIsPresent() {
        assertReflectionEquals(normal("2", 2), createListElementExtractor(0).extractFrom(singletonList(2)));
    }
}
