package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

class PresentMatcher<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    static final PresentMatcher<?> INSTANCE = new PresentMatcher<>();

    private PresentMatcher() {
    }

    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        safeTreeReporter.correctlyPresent();
    }

    @Override
    public void runForMissingItem(SafeTreeReporter safeTreeReporter) {
        safeTreeReporter.incorrectlyMissing();
    }

    @Override
    public boolean matches(Object item) {
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("present");
    }
}
