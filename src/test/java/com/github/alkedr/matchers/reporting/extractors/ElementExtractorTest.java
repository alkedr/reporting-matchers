package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.ElementKey;
import org.junit.After;
import org.junit.Test;

import java.util.List;

import static com.github.alkedr.matchers.reporting.extractors.Extractors.elementExtractor;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ElementExtractorTest {
    private final Extractor.ResultListener result = mock(Extractor.ResultListener.class);
    private final Object[] array = {1};
    private final List<Object> list = asList(array);
    private final Iterable<Object> iterable = list::iterator;

    @After
    public void tearDown() {
        verifyNoMoreInteractions(result);
    }

    @Test
    public void nullItem() {
        ElementKey key = new ElementKey(0);
        elementExtractor(key).extractFrom(null, result);
        verify(result).missing(key);
    }

    @Test
    public void itemHasWrongClass() {
        ElementKey key = new ElementKey(0);
        elementExtractor(key).extractFrom(new Object(), result);
        verify(result).broken(same(key), isA(ClassCastException.class));
    }

    @Test
    public void array_indexIsGreaterThanSize() {
        ElementKey key = new ElementKey(1);
        elementExtractor(key).extractFrom(array, result);
        verify(result).missing(key);
    }

    @Test
    public void array_elementIsPresent() {
        ElementKey key = new ElementKey(0);
        elementExtractor(key).extractFrom(array, result);
        verify(result).present(key, 1);
    }

    @Test
    public void list_indexIsGreaterThanSize() {
        ElementKey key = new ElementKey(1);
        elementExtractor(key).extractFrom(list, result);
        verify(result).missing(key);
    }

    @Test
    public void list_elementIsPresent() {
        ElementKey key = new ElementKey(0);
        elementExtractor(key).extractFrom(list, result);
        verify(result).present(key, 1);
    }

    @Test
    public void iterable_indexIsGreaterThanSize() {
        ElementKey key = new ElementKey(1);
        elementExtractor(key).extractFrom(iterable, result);
        verify(result).missing(key);
    }

    @Test
    public void iterable_elementIsPresent() {
        ElementKey key = new ElementKey(0);
        elementExtractor(key).extractFrom(iterable, result);
        verify(result).present(key, 1);
    }

    @Test
    public void extractFromMissingItem() {
        ElementKey key = new ElementKey(1);
        elementExtractor(key).extractFromMissingItem(result);
        verify(result).missing(key);
    }
}
