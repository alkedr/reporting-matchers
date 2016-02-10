package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractors.listElement;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ListElementExtractorTest {
    private final SubValuesExtractor.SubValuesListener<Object> listener = mock(SubValuesExtractor.SubValuesListener.class);

    @Test(expected = IllegalArgumentException.class)
    public void negativeIndex() {
        listElement(-1);
    }

    @Test
    public void nullItem() {
        listElement(0).run(null, listener);
        verify(listener).absent(elementKey(0));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void indexIsGreaterThanSize() {
        listElement(1).run(singletonList(1), listener);
        verify(listener).absent(elementKey(1));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void elementIsPresent() {
        listElement(0).run(singletonList(1), listener);
        verify(listener).present(elementKey(0), 1);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void extractFromAbsentItem() {
        listElement(0).runForAbsentItem(listener);
        verify(listener).absent(elementKey(0));
        verifyNoMoreInteractions(listener);
    }
}
