package com.github.alkedr.matchers.reporting.list;

import com.github.alkedr.matchers.reporting.ExtractingMatcher;
import com.github.alkedr.matchers.reporting.ExtractingMatcherBuilder;

import java.util.List;

import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.broken;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.missing;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.normal;
import static com.github.alkedr.matchers.reporting.ExtractingMatcherBuilder.extractedValue;

public class ReportingMatchersForLists {
    public static <T> ExtractingMatcherBuilder<List<T>> element(int index) {
        return extractedValue(String.valueOf(index), createListElementExtractor(index));
    }

    static ExtractingMatcher.Extractor createListElementExtractor(final int index) {
        return new ExtractingMatcher.Extractor() {
            @Override
            public ExtractedValue extractFrom(Object item) {
                try {
                    List<?> list = (List<?>) item;
                    return item == null || index < 0 || index >= list.size() ? missing() : normal(list.get(index));
                } catch (ClassCastException e) {
                    return broken(e);
                }
            }
        };
    }
}
