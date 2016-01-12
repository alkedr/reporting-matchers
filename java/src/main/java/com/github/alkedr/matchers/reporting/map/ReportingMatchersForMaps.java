package com.github.alkedr.matchers.reporting.map;

import com.github.alkedr.matchers.reporting.ExtractingMatcher;
import com.github.alkedr.matchers.reporting.ExtractingMatcherBuilder;

import java.util.Map;

import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.broken;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.missing;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.normal;
import static com.github.alkedr.matchers.reporting.ExtractingMatcherBuilder.extractedValue;

public class ReportingMatchersForMaps {
    public static <K, V> ExtractingMatcherBuilder<Map<K, V>> valueForKey(K key) {
        return extractedValue(String.valueOf(key), createValueForKeyExtractor(key));
    }

    static <K> ExtractingMatcher.Extractor createValueForKeyExtractor(final K key) {
        return new ExtractingMatcher.Extractor() {
            @Override
            public ExtractedValue extractFrom(Object item) {
                try {
                    if (item == null || !((Map<?, ?>) item).containsKey(key)) {
                        return missing();
                    }
                    return normal(((Map<?, ?>) item).get(key));
                } catch (ClassCastException e) {
                    return broken(e);
                }
            }
        };
    }
}
