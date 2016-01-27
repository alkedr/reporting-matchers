package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

abstract class BaseReportingMatcherBuilder<T> extends BaseReportingMatcher<T> implements ReportingMatcherBuilder<T> {
    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        build().run(item, safeTreeReporter);
    }

    @Override
    public void runForMissingItem(SafeTreeReporter safeTreeReporter) {
        build().runForMissingItem(safeTreeReporter);
    }
}
