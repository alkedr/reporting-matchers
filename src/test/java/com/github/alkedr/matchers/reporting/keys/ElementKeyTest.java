package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import java.util.List;

import static com.github.alkedr.matchers.reporting.keys.ExtractorVerificationUtils.verifyBroken;
import static com.github.alkedr.matchers.reporting.keys.ExtractorVerificationUtils.verifyMissing;
import static com.github.alkedr.matchers.reporting.keys.ExtractorVerificationUtils.verifyPresent;
import static com.github.alkedr.matchers.reporting.keys.Keys.elementKey;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
        ExtractableKey key = elementKey(0);
        verifyMissing(
                () -> key.extractFrom(null),
                sameInstance(key)
        );
    }

    @Test
    public void itemHasWrongClass() {
        ExtractableKey key = elementKey(0);
        verifyBroken(
                () -> key.extractFrom(new Object()),
                sameInstance(key),
                ClassCastException.class
        );
    }

    @Test
    public void array_indexIsGreaterThanSize() {
        ExtractableKey key = elementKey(1);
        verifyMissing(
                () -> key.extractFrom(array),
                sameInstance(key)
        );
    }

    @Test
    public void array_elementIsPresent() {
        ExtractableKey key = elementKey(0);
        verifyPresent(
                () -> key.extractFrom(array),
                sameInstance(key),
                equalTo(1)
        );
    }

    @Test
    public void list_indexIsGreaterThanSize() {
        ExtractableKey key = elementKey(1);
        verifyMissing(
                () -> key.extractFrom(list),
                sameInstance(key)
        );
    }

    @Test
    public void list_elementIsPresent() {
        ExtractableKey key = elementKey(0);
        verifyPresent(
                () -> key.extractFrom(list),
                sameInstance(key),
                equalTo(1)
        );
    }

    @Test
    public void iterable_indexIsGreaterThanSize() {
        ExtractableKey key = elementKey(1);
        verifyMissing(
                () -> key.extractFrom(iterable),
                sameInstance(key)
        );
    }

    @Test
    public void iterable_elementIsPresent() {
        ExtractableKey key = elementKey(0);
        verifyPresent(
                () -> key.extractFrom(iterable),
                sameInstance(key),
                equalTo(1)
        );
    }

    @Test
    public void extractFromMissingItem() {
        ExtractableKey key = elementKey(0);
        verifyMissing(
                key::extractFromMissingItem,
                sameInstance(key)
        );
    }
}
