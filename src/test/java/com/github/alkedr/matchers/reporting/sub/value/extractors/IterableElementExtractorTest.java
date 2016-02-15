package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractors.iterableElement;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class IterableElementExtractorTest {
    private final SubValuesExtractor.SubValuesListener<Object> listener = mock(SubValuesExtractor.SubValuesListener.class);

    @Test(expected = IllegalArgumentException.class)
    public void negativeIndex() {
        iterableElement(-1);
    }

    @Test
    public void nullItem() {
        iterableElement(0).run(null, listener);
        verify(listener).absent(elementKey(0));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void indexIsGreaterThanSize() {
        iterableElement(1).run(singletonList(1), listener);
        verify(listener).absent(elementKey(1));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void elementIsPresent() {
        iterableElement(0).run(singletonList(1), listener);
        verify(listener).present(elementKey(0), 1);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void extractFromAbsentItem() {
        iterableElement(0).runForAbsentItem(listener);
        verify(listener).absent(elementKey(0));
        verifyNoMoreInteractions(listener);
    }
}
