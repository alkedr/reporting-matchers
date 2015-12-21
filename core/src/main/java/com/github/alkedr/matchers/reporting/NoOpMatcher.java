package com.github.alkedr.matchers.reporting;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class NoOpMatcher<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    private static final NoOpMatcher<?> INSTANCE = new NoOpMatcher<>();

    private NoOpMatcher() {
    }

    @Override
    public void run(Object item, Reporter reporter) {
    }

    @Override
    public void runForMissingItem(Reporter reporter) {
    }

    @Override
    public boolean matches(Object item) {
        return true;
    }

    @Override
    public void describeTo(Description description) {
        // TODO
    }

    // TODO: rename
    public static <T> NoOpMatcher<T> noOp() {
        return (NoOpMatcher<T>) INSTANCE;
    }
}
