package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

public class MissingMatcher<T> extends BaseReportingMatcher<T> {
    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        safeTreeReporter.incorrectlyPresent();
    }

    @Override
    public void runForMissingItem(SafeTreeReporter safeTreeReporter) {
        safeTreeReporter.correctlyMissing();
    }
}
