package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.keys.HashMapKey;

import java.util.Map;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.broken;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.missing;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.present;

public class ValueForKeyExtractor extends HashMapKey implements ExtractingMatcher.Extractor {
    public ValueForKeyExtractor(Object key) {
        super(key);
    }

    @Override
    public ExtractingMatcher.KeyValue extractFrom(Object item) {
        try {
            if (item == null || !((Map<?, ?>) item).containsKey(getKey())) {
                return new ExtractingMatcher.KeyValue(this, missing());
            }
            return new ExtractingMatcher.KeyValue(this, present(((Map<?, ?>) item).get(getKey())));
        } catch (ClassCastException e) {
            return new ExtractingMatcher.KeyValue(this, broken(e));
        }
    }

    @Override
    public ExtractingMatcher.KeyValue extractFromMissingItem() {
        return new ExtractingMatcher.KeyValue(this, missing());
    }
}
