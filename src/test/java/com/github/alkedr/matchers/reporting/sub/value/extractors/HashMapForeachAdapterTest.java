package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.junit.Test;
import org.mockito.InOrder;

import java.util.HashMap;
import java.util.Map;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.ForeachAdapters.hashMapForeachAdapter;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.hashMapKey;
import static java.util.Collections.emptyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class HashMapForeachAdapterTest {
    private final SubValuesExtractor.SubValuesListener listener = mock(SubValuesExtractor.SubValuesListener.class);
    private final InOrder inOrder = inOrder(listener);

    @Test
    public void empty() {
        hashMapForeachAdapter().run(emptyMap(), listener);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void threeElements() {
        Integer[] keys = {1, 2, 3};
        Integer[] values = {4, 5, 6};
        Map<Integer, Integer> map = new HashMap<>();
        map.put(keys[0], values[0]);
        map.put(keys[1], values[1]);
        map.put(keys[2], values[2]);
        hashMapForeachAdapter().run(map, listener);
        inOrder.verify(listener).present(eq(hashMapKey(keys[0])), same(values[0]));
        inOrder.verify(listener).present(eq(hashMapKey(keys[1])), same(values[1]));
        inOrder.verify(listener).present(eq(hashMapKey(keys[2])), same(values[2]));
        inOrder.verifyNoMoreInteractions();
    }
}
