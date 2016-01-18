package com.github.alkedr.matchers.reporting.utility;

import com.github.alkedr.matchers.reporting.BaseReportingMatcher;
import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.hamcrest.Description;

import java.util.function.Function;

public class ConvertingReportingMatcher<T, U> extends BaseReportingMatcher<T> {
    private final Function<T, U> converter;
    private final ReportingMatcher<U> matcher;

    public ConvertingReportingMatcher(Function<T, U> converter, ReportingMatcher<U> matcher) {
        this.converter = converter;
        this.matcher = matcher;
    }

    @Override
    public Checks getChecks(Object item) {
        // TODO: catch ClassCastException?
        return matcher.getChecks(converter.apply((T) item));
    }

    @Override
    public Checks getChecksForMissingItem() {
        return matcher.getChecksForMissingItem();
    }

    @Override
    public void describeTo(Description description) {
    }
}
