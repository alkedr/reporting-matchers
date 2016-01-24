package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import java.util.List;

import static com.github.alkedr.matchers.reporting.keys.Keys.elementKey;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ElementKeyTest {
    private final ExtractableKey.ResultListener result = mock(ExtractableKey.ResultListener.class);
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
        key.extractFrom(null, result);
        verify(result).missing(same(key));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void itemHasWrongClass() {
        ExtractableKey key = elementKey(0);
        key.extractFrom(new Object(), result);
        verify(result).broken(same(key), isA(ClassCastException.class));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void array_indexIsGreaterThanSize() {
        ExtractableKey key = elementKey(1);
        key.extractFrom(array, result);
        verify(result).missing(same(key));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void array_elementIsPresent() {
        ExtractableKey key = elementKey(0);
        key.extractFrom(array, result);
        verify(result).present(same(key), eq(1));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void list_indexIsGreaterThanSize() {
        ExtractableKey key = elementKey(1);
        key.extractFrom(list, result);
        verify(result).missing(same(key));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void list_elementIsPresent() {
        ExtractableKey key = elementKey(0);
        key.extractFrom(list, result);
        verify(result).present(same(key), eq(1));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void iterable_indexIsGreaterThanSize() {
        ExtractableKey key = elementKey(1);
        key.extractFrom(iterable, result);
        verify(result).missing(same(key));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void iterable_elementIsPresent() {
        ExtractableKey key = elementKey(0);
        key.extractFrom(iterable, result);
        verify(result).present(same(key), eq(1));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void extractFromMissingItem() {
        ExtractableKey key = elementKey(1);
        key.extractFromMissingItem(result);
        verify(result).missing(same(key));
        verifyNoMoreInteractions(result);
    }
}
