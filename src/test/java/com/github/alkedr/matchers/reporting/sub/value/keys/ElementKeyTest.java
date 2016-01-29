package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.junit.Test;

import java.util.List;

import static com.github.alkedr.matchers.reporting.sub.value.keys.ExtractorVerificationUtils.verifyAbsent;
import static com.github.alkedr.matchers.reporting.sub.value.keys.ExtractorVerificationUtils.verifyBroken;
import static com.github.alkedr.matchers.reporting.sub.value.keys.ExtractorVerificationUtils.verifyPresent;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;
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
        verifyAbsent(
                listener -> key.run(null, listener),
                sameInstance(key)
        );
    }

    @Test
    public void itemHasWrongClass() {
        ExtractableKey key = elementKey(0);
        verifyBroken(
                listener -> key.run(new Object(), listener),
                sameInstance(key),
                ClassCastException.class
        );
    }

    @Test
    public void array_indexIsGreaterThanSize() {
        ExtractableKey key = elementKey(1);
        verifyAbsent(
                listener -> key.run(array, listener),
                sameInstance(key)
        );
    }

    @Test
    public void array_elementIsPresent() {
        ExtractableKey key = elementKey(0);
        verifyPresent(
                listener -> key.run(array, listener),
                sameInstance(key),
                equalTo(1)
        );
    }

    @Test
    public void list_indexIsGreaterThanSize() {
        ExtractableKey key = elementKey(1);
        verifyAbsent(
                listener -> key.run(list, listener),
                sameInstance(key)
        );
    }

    @Test
    public void list_elementIsPresent() {
        ExtractableKey key = elementKey(0);
        verifyPresent(
                listener -> key.run(list, listener),
                sameInstance(key),
                equalTo(1)
        );
    }

    @Test
    public void iterable_indexIsGreaterThanSize() {
        ExtractableKey key = elementKey(1);
        verifyAbsent(
                listener -> key.run(iterable, listener),
                sameInstance(key)
        );
    }

    @Test
    public void iterable_elementIsPresent() {
        ExtractableKey key = elementKey(0);
        verifyPresent(
                listener -> key.run(iterable, listener),
                sameInstance(key),
                equalTo(1)
        );
    }

    @Test
    public void extractFromAbsentItem() {
        ExtractableKey key = elementKey(0);
        verifyAbsent(
                key::runForAbsentItem,
                sameInstance(key)
        );
    }
}
