package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.check.results.CheckResult;

import java.util.Iterator;
import java.util.function.Function;

class ConvertingReportingMatcher<T, U> extends BaseReportingMatcher<T> {
    private final Function<T, U> converter;
    private final ReportingMatcher<U> matcher;

    ConvertingReportingMatcher(Function<T, U> converter, ReportingMatcher<U> matcher) {
        this.converter = converter;
        this.matcher = matcher;
    }

    @Override
    public Iterator<CheckResult> getChecks(Object item) {
        // TODO: catch ClassCastException?
        return matcher.getChecks(converter.apply((T) item));
    }

    @Override
    public Iterator<CheckResult> getChecksForMissingItem() {
        return matcher.getChecksForMissingItem();
    }
}
