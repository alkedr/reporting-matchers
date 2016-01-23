package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.HashMapKey;

import java.util.Map;

class HashMapKeyExtractor implements Extractor {
    private final HashMapKey key;

    HashMapKeyExtractor(HashMapKey key) {
        this.key = key;
    }

    @Override
    public void extractFrom(Object item, ResultListener result) {
        try {
            if (item == null || !((Map<?, ?>) item).containsKey(key.getKey())) {
                result.missing(key);
            } else {
                result.present(key, ((Map<?, ?>) item).get(key.getKey()));
            }
        } catch (ClassCastException e) {
            result.broken(key, e);
        }
    }

    @Override
    public void extractFromMissingItem(ResultListener result) {
        extractFrom(null, result);
    }
}
