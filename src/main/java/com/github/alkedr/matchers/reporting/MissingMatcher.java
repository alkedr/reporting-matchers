package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

class MissingMatcher<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    static final MissingMatcher<?> INSTANCE = new MissingMatcher<>();

    private MissingMatcher() {
    }

    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        safeTreeReporter.incorrectlyPresent();
    }

    @Override
    public void runForMissingItem(SafeTreeReporter safeTreeReporter) {
        safeTreeReporter.correctlyMissing();
    }

    @Override
    public boolean matches(Object item) {
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("missing");
    }
}
