package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

public class PresentMatcher<T> extends BaseReportingMatcher<T> {
    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        safeTreeReporter.correctlyPresent();
    }

    @Override
    public void runForMissingItem(SafeTreeReporter safeTreeReporter) {
        safeTreeReporter.incorrectlyMissing();
    }
}
