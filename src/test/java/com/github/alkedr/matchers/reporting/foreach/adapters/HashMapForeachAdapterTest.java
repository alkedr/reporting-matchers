package com.github.alkedr.matchers.reporting.foreach.adapters;

import com.github.alkedr.matchers.reporting.keys.Key;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.github.alkedr.matchers.reporting.foreach.adapters.ForeachAdapters.hashMapForeachAdapter;
import static com.github.alkedr.matchers.reporting.keys.Keys.hashMapKey;
import static java.util.Collections.emptyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class HashMapForeachAdapterTest {
    private final BiConsumer<Key, Object> consumer = mock(BiConsumer.class);
    private final InOrder inOrder = inOrder(consumer);

    @Test
    public void empty() {
        hashMapForeachAdapter().run(emptyMap(), consumer);
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
        hashMapForeachAdapter().run(map, consumer);
        inOrder.verify(consumer).accept(eq(hashMapKey(keys[0])), same(values[0]));
        inOrder.verify(consumer).accept(eq(hashMapKey(keys[1])), same(values[1]));
        inOrder.verify(consumer).accept(eq(hashMapKey(keys[2])), same(values[2]));
        inOrder.verifyNoMoreInteractions();
    }
}
