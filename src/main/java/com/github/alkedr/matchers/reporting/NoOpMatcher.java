package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

// matches всегда возвращает true, в отчёт никогда ничего не добавляется
class NoOpMatcher<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    static final NoOpMatcher<?> INSTANCE = new NoOpMatcher<>();

    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
    }

    @Override
    public void runForMissingItem(SafeTreeReporter safeTreeReporter) {
    }


    @Override
    public boolean matches(Object item) {
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("anything");
    }
}
