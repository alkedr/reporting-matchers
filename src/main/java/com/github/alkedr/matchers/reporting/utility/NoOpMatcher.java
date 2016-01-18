package com.github.alkedr.matchers.reporting.utility;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

// matches всегда возвращает true, в отчёт никогда ничего не добавляется
public class NoOpMatcher<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    private static final NoOpMatcher<?> INSTANCE = new NoOpMatcher<>();

    @Override
    public Checks getChecks(Object item) {
        return Checks.noOp();
    }

    @Override
    public Checks getChecksForMissingItem() {
        return Checks.noOp();
    }


    @Override
    public boolean matches(Object item) {
        return true;
    }

    @Override
    public void describeTo(Description description) {
    }


    // TODO: rename?
    public static <T> NoOpMatcher<T> noOp() {
        return (NoOpMatcher<T>) INSTANCE;
    }
}
