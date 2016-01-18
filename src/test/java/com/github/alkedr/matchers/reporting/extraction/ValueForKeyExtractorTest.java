package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.keys.HashMapKey;
import org.junit.Test;

import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ValueForKeyExtractorTest {
    @Test
    public void nullItem() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new HashMapKey("1"), ReportingMatcher.Value.missing()),
                new ValueForKeyExtractor("1").extractFrom(null)
        );
    }

    @Test
    public void itemIsNotMap() {
        ExtractingMatcher.KeyValue actual = new ValueForKeyExtractor("1").extractFrom(new Object());
        assertEquals(new HashMapKey("1"), actual.key);
        assertEquals(ReportingMatcher.PresenceStatus.MISSING, actual.value.presenceStatus());
        assertNull(actual.value.get());
        assertSame(ClassCastException.class, actual.value.extractionThrowable().getClass());
    }

    @Test
    public void keyIsMissing() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new HashMapKey("1"), ReportingMatcher.Value.missing()),
                new ValueForKeyExtractor("1").extractFrom(singletonMap("2", "q"))
        );
    }

    @Test
    public void keyIsPresent() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new HashMapKey("1"), ReportingMatcher.Value.present("q")),
                new ValueForKeyExtractor("1").extractFrom(singletonMap("1", "q"))
        );
    }

    @Test
    public void keyIsPresentButValueIsNull() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new HashMapKey("1"), ReportingMatcher.Value.present(null)),
                new ValueForKeyExtractor("1").extractFrom(singletonMap("1", null))
        );
    }

    @Test
    public void extractFrom_missingItem() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new HashMapKey("1"), ReportingMatcher.Value.missing()),
                new ValueForKeyExtractor("1").extractFromMissingItem()
        );
    }
}
