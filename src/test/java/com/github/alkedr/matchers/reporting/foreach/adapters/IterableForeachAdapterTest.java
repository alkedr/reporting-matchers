package com.github.alkedr.matchers.reporting.foreach.adapters;

import com.github.alkedr.matchers.reporting.keys.Key;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.function.BiConsumer;

import static com.github.alkedr.matchers.reporting.foreach.adapters.ForeachAdapters.iterableForeachAdapter;
import static com.github.alkedr.matchers.reporting.keys.Keys.elementKey;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class IterableForeachAdapterTest {
    private final BiConsumer<Key, Object> consumer = mock(BiConsumer.class);
    private final InOrder inOrder = inOrder(consumer);

    @Test
    public void empty() {
        iterableForeachAdapter().run(emptyList(), consumer);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void threeElements() {
        Integer element1 = 1;
        Integer element2 = 2;
        Integer element3 = 3;
        iterableForeachAdapter().run(asList(element1, element2, element3), consumer);
        inOrder.verify(consumer).accept(eq(elementKey(0)), same(element1));
        inOrder.verify(consumer).accept(eq(elementKey(1)), same(element2));
        inOrder.verify(consumer).accept(eq(elementKey(2)), same(element3));
        inOrder.verifyNoMoreInteractions();
    }
}
