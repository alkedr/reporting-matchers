package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.MergingReporter;
import com.github.alkedr.matchers.reporting.reporters.Reporter;

class MergingMatcher<T> extends BaseReportingMatcher<T> {
    private final ReportingMatcher<T> reportingMatcher;

    MergingMatcher(ReportingMatcher<T> reportingMatcher) {
        this.reportingMatcher = reportingMatcher;
    }

    @Override
    public void run(Object item, Reporter reporter) {
        try (MergingReporter mergingReporter = new MergingReporter(reporter)) {
            reportingMatcher.run(item, mergingReporter);
        }
    }

    @Override
    public void runForMissingItem(Reporter reporter) {
        try (MergingReporter mergingReporter = new MergingReporter(reporter)) {
            reportingMatcher.runForMissingItem(mergingReporter);
        }
    }
}
