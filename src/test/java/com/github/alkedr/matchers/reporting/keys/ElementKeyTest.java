package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import java.util.List;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.noOp;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.missingSubValue;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.presentSubValue;
import static com.github.alkedr.matchers.reporting.keys.Keys.elementKey;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyIterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

public class ElementKeyTest {
    private final Object[] array = {1};
    private final List<Object> list = asList(array);
    private final Iterable<Object> iterable = list::iterator;


    @Test(expected = IllegalArgumentException.class)
    public void negativeIndex() {
        elementKey(-1);
    }

    @Test
    public void asStringTest() {
        assertEquals("[1]", elementKey(0).asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(elementKey(0).hashCode(), elementKey(0).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(elementKey(0), elementKey(0));
        assertNotEquals(elementKey(0), elementKey(1));
    }

    @Test
    public void nullItem() {
        ExtractableKey.Result.Broken actual = (ExtractableKey.Result.Broken) elementKey(1).extractFrom(null);
        assertEquals(elementKey(1), actual.key);
        assertSame(ClassCastException.class, actual.throwable.getClass());   // TODO: missing?
    }

    @Test
    public void itemHasWrongClass() {
        ExtractableKey.Result.Broken actual = (ExtractableKey.Result.Broken) elementKey(1).extractFrom(new Object());
        assertEquals(elementKey(1), actual.key);
        assertSame(ClassCastException.class, actual.throwable.getClass());
    }

    @Test
    public void array_indexIsGreaterThanSize() {
        assertEquals(
                missingSubValue(elementKey(1), emptyIterator()),
                elementKey(1).extractFrom(array).createCheckResult(noOp())
        );
    }

    @Test
    public void array_elementIsPresent() {
        assertEquals(
                presentSubValue(elementKey(0), 1, emptyIterator()),
                elementKey(0).extractFrom(array).createCheckResult(noOp())
        );
    }

    @Test
    public void list_indexIsGreaterThanSize() {
        assertEquals(
                missingSubValue(elementKey(1), emptyIterator()),
                elementKey(1).extractFrom(list).createCheckResult(noOp())
        );
    }

    @Test
    public void list_elementIsPresent() {
        assertEquals(
                presentSubValue(elementKey(0), 1, emptyIterator()),
                elementKey(0).extractFrom(list).createCheckResult(noOp())
        );
    }

    @Test
    public void iterable_indexIsGreaterThanSize() {
        assertEquals(
                missingSubValue(elementKey(1), emptyIterator()),
                elementKey(1).extractFrom(iterable).createCheckResult(noOp())
        );
    }

    @Test
    public void iterable_elementIsPresent() {
        assertEquals(
                presentSubValue(elementKey(0), 1, emptyIterator()),
                elementKey(0).extractFrom(iterable).createCheckResult(noOp())
        );
    }

    @Test
    public void extractFromMissingItem() {
        assertEquals(
                missingSubValue(elementKey(0), emptyIterator()),
                elementKey(0).extractFromMissingItem().createCheckResult(noOp())
        );
    }
}
