package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

class AbsentMatcher<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    static final AbsentMatcher<?> INSTANCE = new AbsentMatcher<>();

    private AbsentMatcher() {
    }

    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        safeTreeReporter.incorrectlyPresent();
    }

    @Override
    public void runForAbsentItem(SafeTreeReporter safeTreeReporter) {
        safeTreeReporter.correctlyAbsent();
    }

    @Override
    public boolean matches(Object item) {
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("absent");
    }
}
