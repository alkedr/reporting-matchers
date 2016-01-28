package com.github.alkedr.matchers.reporting.sub.value.extractors;

import java.util.Map;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.hashMapKey;

class HashMapForeachAdapter implements SubValuesExtractor<Map<?,?>> {
    static final HashMapForeachAdapter INSTANCE = new HashMapForeachAdapter();

    private HashMapForeachAdapter() {
    }

    @Override
    public void run(Map<?, ?> item, SubValuesListener subValuesListener) {
        for (Map.Entry<?, ?> entry : item.entrySet()) {
            subValuesListener.present(hashMapKey(entry.getKey()), entry.getValue());
        }
    }

    @Override
    public void runForMissingItem(SubValuesListener subValuesListener) {
    }
}
