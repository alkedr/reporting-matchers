package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.Reporter;

import java.util.function.Function;

class ConvertingReportingMatcher<T, U> extends BaseReportingMatcher<T> {
    private final Function<T, U> converter;
    private final ReportingMatcher<U> matcher;

    ConvertingReportingMatcher(Function<T, U> converter, ReportingMatcher<U> matcher) {
        this.converter = converter;
        this.matcher = matcher;
    }

    @Override
    public void run(Object item, Reporter reporter) {
        matcher.run(converter.apply((T) item), reporter);
    }

    @Override
    public void runForMissingItem(Reporter reporter) {
        matcher.runForMissingItem(reporter);
    }
}
