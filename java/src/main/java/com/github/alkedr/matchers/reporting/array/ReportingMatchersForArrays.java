package com.github.alkedr.matchers.reporting.array;

import com.github.alkedr.matchers.reporting.ExtractingMatcher;
import com.github.alkedr.matchers.reporting.ExtractingMatcherBuilder;

import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.broken;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.missing;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.normal;
import static com.github.alkedr.matchers.reporting.ExtractingMatcherBuilder.extractedValue;

public class ReportingMatchersForArrays {
    public static <T> ExtractingMatcherBuilder<T[]> arrayElement(int index) {
        return extractedValue(String.valueOf(index), createArrayElementExtractor(index));
    }

    static ExtractingMatcher.Extractor createArrayElementExtractor(final int index) {
        return new ExtractingMatcher.Extractor() {
            @Override
            public ExtractedValue extractFrom(Object item) {
                try {
                    Object[] array = (Object[]) item;
                    return item == null || index < 0 || index >= array.length ? missing() : normal(array[index]);
                } catch (ClassCastException e) {
                    return broken(e);
                }
            }
        };
    }
}
