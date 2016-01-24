package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.CloseableSafeTreeReporter;
import com.github.alkedr.matchers.reporting.reporters.Reporters;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

class MergingMatcher<T> extends BaseReportingMatcher<T> {
    private final ReportingMatcher<T> reportingMatcher;

    MergingMatcher(ReportingMatcher<T> reportingMatcher) {
        this.reportingMatcher = reportingMatcher;
    }

    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        try (CloseableSafeTreeReporter mergingReporter = Reporters.mergingReporter(safeTreeReporter)) {
            reportingMatcher.run(item, mergingReporter);
        }
    }

    @Override
    public void runForMissingItem(SafeTreeReporter safeTreeReporter) {
        try (CloseableSafeTreeReporter mergingReporter = Reporters.mergingReporter(safeTreeReporter)) {
            reportingMatcher.runForMissingItem(mergingReporter);
        }
    }
}
