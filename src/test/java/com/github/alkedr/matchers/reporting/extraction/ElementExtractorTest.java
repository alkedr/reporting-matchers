package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ElementExtractorTest {
    private final Object[] array = {1};
    private final List<Object> list = asList(array);
    private final Iterable<Object> iterable = list::iterator;


    @Test
    public void nullItem() {
        ExtractingMatcher.KeyValue actual = new ElementExtractor(1).extractFrom(null);
        assertEquals(new ElementExtractor(1), actual.key);
        assertEquals(ReportingMatcher.PresenceStatus.MISSING, actual.value.presenceStatus());
        assertNull(actual.value.get());
        assertSame(ClassCastException.class, actual.value.extractionThrowable().getClass());
    }

    @Test
    public void itemHasWrongClass() {
        ExtractingMatcher.KeyValue actual = new ElementExtractor(1).extractFrom(new Object());
        assertEquals(new ElementExtractor(1), actual.key);
        assertEquals(ReportingMatcher.PresenceStatus.MISSING, actual.value.presenceStatus());
        assertNull(actual.value.get());
        assertSame(ClassCastException.class, actual.value.extractionThrowable().getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void indexIsNegative() {
        new ElementExtractor(-1);
    }

    @Test
    public void array_indexIsGreaterThanSize() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new ElementExtractor(1), ReportingMatcher.Value.missing()),
                new ElementExtractor(1).extractFrom(array)
        );
    }

    @Test
    public void array_elementIsPresent() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new ElementExtractor(0), ReportingMatcher.Value.present(1)),
                new ElementExtractor(0).extractFrom(array)
        );
    }

    @Test
    public void list_indexIsGreaterThanSize() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new ElementExtractor(1), ReportingMatcher.Value.missing()),
                new ElementExtractor(1).extractFrom(list)
        );
    }

    @Test
    public void list_elementIsPresent() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new ElementExtractor(0), ReportingMatcher.Value.present(1)),
                new ElementExtractor(0).extractFrom(list)
        );
    }

    @Test
    public void iterable_indexIsGreaterThanSize() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new ElementExtractor(1), ReportingMatcher.Value.missing()),
                new ElementExtractor(1).extractFrom(iterable)
        );
    }

    @Test
    public void iterable_elementIsPresent() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new ElementExtractor(0), ReportingMatcher.Value.present(1)),
                new ElementExtractor(0).extractFrom(iterable)
        );
    }

    @Test
    public void extractFromMissingItem() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new ElementExtractor(0), ReportingMatcher.Value.missing()),
                new ElementExtractor(0).extractFromMissingItem()
        );
    }
}
