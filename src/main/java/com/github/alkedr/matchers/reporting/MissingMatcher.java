package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

class MissingMatcher<T> extends BaseReportingMatcher<T> {
    static final MissingMatcher<?> INSTANCE = new MissingMatcher<>();

    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        safeTreeReporter.incorrectlyPresent();
    }

    @Override
    public void runForMissingItem(SafeTreeReporter safeTreeReporter) {
        safeTreeReporter.correctlyMissing();
    }
}
