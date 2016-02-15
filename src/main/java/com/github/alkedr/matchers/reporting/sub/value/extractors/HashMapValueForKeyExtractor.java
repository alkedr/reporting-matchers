package com.github.alkedr.matchers.reporting.sub.value.extractors;

import java.util.Map;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.hashMapValueForKeyKey;

class HashMapValueForKeyExtractor<K, V> implements SubValuesExtractor<Map<K, V>, V> {
    private final K key;

    HashMapValueForKeyExtractor(K key) {
        this.key = key;
    }

    @Override
    public void run(Map<K, V> item, SubValuesListener<V> subValuesListener) {
        try {
            if (item == null || !item.containsKey(key)) {
                subValuesListener.absent(hashMapValueForKeyKey(key));
            } else {
                subValuesListener.present(hashMapValueForKeyKey(key), item.get(key));
            }
        } catch (ClassCastException e) {
            subValuesListener.broken(hashMapValueForKeyKey(key), e);
        }
    }

    @Override
    public void runForAbsentItem(SubValuesListener<V> subValuesListener) {
        subValuesListener.absent(hashMapValueForKeyKey(key));
    }
}
