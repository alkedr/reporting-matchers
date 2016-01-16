package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;

import java.util.function.Function;

public class SimpleMethodByLambdaExtractingMatcher<T> extends ExtractingMatcher<T> implements ReportingMatcher.Key {
    private final Function<T, ?> function;

    public SimpleMethodByLambdaExtractingMatcher(Function<T, ?> function) {
        this.function = function;
    }

    @Override
    protected KeyValue extractFrom(Object item) {
        return null;
    }

    @Override
    protected KeyValue extractFromMissingItem() {
        return null;
    }

    @Override
    public String asString() {
        return null;
    }


    // Теоретически можно объединиться с Method'ом, потому что у Method'а есть Class
}
