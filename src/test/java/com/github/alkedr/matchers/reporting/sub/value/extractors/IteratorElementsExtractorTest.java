package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValueExtractors.iteratorElementsExtractor;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class IteratorElementsExtractorTest {
    private final SubValuesExtractor.SubValuesListener listener = mock(SubValuesExtractor.SubValuesListener.class);
    private final InOrder inOrder = inOrder(listener);

    @Test
    public void run_null() {
        iteratorElementsExtractor().run(null, listener);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void run_empty() {
        iteratorElementsExtractor().run(emptyList().iterator(), listener);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void run_threeElements() {
        Integer element1 = 1;
        Integer element2 = 2;
        Integer element3 = 3;
        iteratorElementsExtractor().run(asList(element1, element2, element3).iterator(), listener);
        inOrder.verify(listener).present(eq(elementKey(0)), same(element1));
        inOrder.verify(listener).present(eq(elementKey(1)), same(element2));
        inOrder.verify(listener).present(eq(elementKey(2)), same(element3));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void runForAbsentItem() {
        iteratorElementsExtractor().runForAbsentItem(listener);
        inOrder.verifyNoMoreInteractions();
    }
}
