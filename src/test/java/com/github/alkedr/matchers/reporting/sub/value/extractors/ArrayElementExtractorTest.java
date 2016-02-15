package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractors.arrayElement;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ArrayElementExtractorTest {
    private final SubValuesExtractor.SubValuesListener<Object> listener = mock(SubValuesExtractor.SubValuesListener.class);

    @Test(expected = IllegalArgumentException.class)
    public void negativeIndex() {
        arrayElement(-1);
    }

    @Test
    public void nullItem() {
        arrayElement(0).run(null, listener);
        verify(listener).absent(elementKey(0));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void indexIsGreaterThanSize() {
        arrayElement(1).run(new Object[]{1}, listener);
        verify(listener).absent(elementKey(1));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void elementIsPresent() {
        arrayElement(0).run(new Object[]{1}, listener);
        verify(listener).present(elementKey(0), 1);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void extractFromAbsentItem() {
        arrayElement(0).runForAbsentItem(listener);
        verify(listener).absent(elementKey(0));
        verifyNoMoreInteractions(listener);
    }
}
