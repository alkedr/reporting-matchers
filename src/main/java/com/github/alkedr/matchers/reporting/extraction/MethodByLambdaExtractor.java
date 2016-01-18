package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;

import java.util.function.Function;

@Deprecated
public class MethodByLambdaExtractor implements ExtractingMatcher.Extractor, ReportingMatcher.Key {
    private final Function<Object, Object> function;

    public MethodByLambdaExtractor(Function<Object, Object> function) {
        this.function = function;
    }

    @Override
    public ExtractingMatcher.KeyValue extractFrom(Object item) {
        if (item == null) {
            return new ExtractingMatcher.KeyValue(this, ReportingMatcher.Value.missing());
        }

        return null;
    }

    @Override
    public ExtractingMatcher.KeyValue extractFromMissingItem() {
        return new ExtractingMatcher.KeyValue(this, ReportingMatcher.Value.missing());
    }

    @Override
    public String asString() {
        return null;   // TODO: что делать здесь?
    }
}
