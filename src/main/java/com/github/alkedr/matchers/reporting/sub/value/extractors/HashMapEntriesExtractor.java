package com.github.alkedr.matchers.reporting.sub.value.extractors;

import java.util.Map;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.hashMapKey;

class HashMapEntriesExtractor<K, V> implements SubValuesExtractor<Map<K, V>, V> {
    static final HashMapEntriesExtractor INSTANCE = new HashMapEntriesExtractor<>();

    private HashMapEntriesExtractor() {
    }

    @Override
    public void run(Map<K, V> item, SubValuesListener<V> subValuesListener) {
        if (item != null) {
            for (Map.Entry<K, V> entry : item.entrySet()) {
                subValuesListener.present(hashMapKey(entry.getKey()), entry.getValue());
            }
        }
    }

    @Override
    public void runForAbsentItem(SubValuesListener<V> subValuesListener) {
    }
}
