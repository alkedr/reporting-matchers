package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

class PresentMatcher<T> extends BaseReportingMatcher<T> {
    static final PresentMatcher<?> INSTANCE = new PresentMatcher<>();

    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        safeTreeReporter.correctlyPresent();
    }

    @Override
    public void runForMissingItem(SafeTreeReporter safeTreeReporter) {
        safeTreeReporter.incorrectlyMissing();
    }
}
