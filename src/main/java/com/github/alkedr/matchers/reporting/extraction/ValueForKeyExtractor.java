package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;

import java.util.Map;
import java.util.Objects;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.broken;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.missing;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.present;

public class ValueForKeyExtractor implements ExtractingMatcher.Extractor, ReportingMatcher.Key {
    private final Object key;

    public ValueForKeyExtractor(Object key) {
        this.key = key;
    }

    @Override
    public KeyValue extractFrom(Object item) {
        try {
            if (item == null || !((Map<?, ?>) item).containsKey(key)) {
                return new KeyValue(this, missing());
            }
            return new KeyValue(this, present(((Map<?, ?>) item).get(key)));
        } catch (ClassCastException e) {
            return new KeyValue(this, broken(e));
        }
    }

    @Override
    public KeyValue extractFromMissingItem() {
        return new KeyValue(this, missing());
    }

    @Override
    public String asString() {
        return String.valueOf(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueForKeyExtractor that = (ValueForKeyExtractor) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
