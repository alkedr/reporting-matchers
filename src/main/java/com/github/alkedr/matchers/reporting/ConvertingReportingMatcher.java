package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

import java.util.function.Function;

class ConvertingReportingMatcher<T, U> extends BaseReportingMatcher<T> {
    private final Function<T, U> converter;
    private final ReportingMatcher<U> matcher;

    ConvertingReportingMatcher(Function<T, U> converter, ReportingMatcher<U> matcher) {
        this.converter = converter;
        this.matcher = matcher;
    }

    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        matcher.run(converter.apply((T) item), safeTreeReporter);
    }

    @Override
    public void runForMissingItem(SafeTreeReporter safeTreeReporter) {
        matcher.runForMissingItem(safeTreeReporter);
    }
}
