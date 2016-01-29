package com.github.alkedr.matchers.reporting.sub.value.extractors;

import java.util.Map;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.hashMapKey;

class HashMapEntriesExtractor implements SubValuesExtractor<Map<?,?>> {
    static final HashMapEntriesExtractor INSTANCE = new HashMapEntriesExtractor();

    private HashMapEntriesExtractor() {
    }

    @Override
    public void run(Map<?, ?> item, SubValuesListener subValuesListener) {
        if (item != null) {
            for (Map.Entry<?, ?> entry : item.entrySet()) {
                subValuesListener.present(hashMapKey(entry.getKey()), entry.getValue());
            }
        }
    }

    @Override
    public void runForAbsentItem(SubValuesListener subValuesListener) {
    }
}
