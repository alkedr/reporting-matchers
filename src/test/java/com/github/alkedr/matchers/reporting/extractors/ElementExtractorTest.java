package com.github.alkedr.matchers.reporting.extractors;

import org.junit.Test;

import java.util.List;

import static com.github.alkedr.matchers.reporting.extractors.ExtractingMatcherExtractors.elementExtractor;
import static com.github.alkedr.matchers.reporting.keys.Keys.elementKey;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ElementExtractorTest {
    private final Object[] array = {1};
    private final List<Object> list = asList(array);
    private final Iterable<Object> iterable = list::iterator;


    @Test
    public void nullItem() {
        Extractor.Result.Broken actual = (Extractor.Result.Broken) elementExtractor(1).extractFrom(null);
        assertEquals(elementKey(1), actual.key);
        assertSame(ClassCastException.class, actual.throwable.getClass());
    }

    @Test
    public void itemHasWrongClass() {
        Extractor.Result.Broken actual = (Extractor.Result.Broken) elementExtractor(1).extractFrom(new Object());
        assertEquals(elementKey(1), actual.key);
        assertSame(ClassCastException.class, actual.throwable.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void indexIsNegative() {
        elementExtractor(-1);
    }

    @Test
    public void array_indexIsGreaterThanSize() {
        assertReflectionEquals(
                new Extractor.Result.Missing(elementKey(1)),
                elementExtractor(1).extractFrom(array)
        );
    }

    @Test
    public void array_elementIsPresent() {
        assertReflectionEquals(
                new Extractor.Result.Present(elementKey(0), 1),
                elementExtractor(0).extractFrom(array)
        );
    }

    @Test
    public void list_indexIsGreaterThanSize() {
        assertReflectionEquals(
                new Extractor.Result.Missing(elementKey(1)),
                elementExtractor(1).extractFrom(list)
        );
    }

    @Test
    public void list_elementIsPresent() {
        assertReflectionEquals(
                new Extractor.Result.Present(elementKey(0), 1),
                elementExtractor(0).extractFrom(list)
        );
    }

    @Test
    public void iterable_indexIsGreaterThanSize() {
        assertReflectionEquals(
                new Extractor.Result.Missing(elementKey(1)),
                elementExtractor(1).extractFrom(iterable)
        );
    }

    @Test
    public void iterable_elementIsPresent() {
        assertReflectionEquals(
                new Extractor.Result.Present(elementKey(0), 1),
                elementExtractor(0).extractFrom(iterable)
        );
    }

    @Test
    public void extractFromMissingItem() {
        assertReflectionEquals(
                new Extractor.Result.Missing(elementKey(0)),
                elementExtractor(0).extractFromMissingItem()
        );
    }
}
