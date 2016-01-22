package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.Map;

import static com.github.alkedr.matchers.reporting.keys.Keys.hashMapKey;

class ValueForKeyExtractor implements Extractor {
    private final Object mapKey;
    private final Key key;

    ValueForKeyExtractor(Object mapKey) {
        this.mapKey = mapKey;
        this.key = hashMapKey(mapKey);
    }

    @Override
    public Result extractFrom(Object item) {
        try {
            if (item == null || !((Map<?, ?>) item).containsKey(mapKey)) {
                return new Result.Missing(key);
            }
            return new Result.Present(key, ((Map<?, ?>) item).get(mapKey));
        } catch (ClassCastException e) {
            return new Result.Broken(key, e);
        }
    }

    @Override
    public Result extractFromMissingItem() {
        return new Result.Missing(key);
    }
}
