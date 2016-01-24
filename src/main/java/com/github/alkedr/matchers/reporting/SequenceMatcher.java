package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

class SequenceMatcher<T> extends BaseReportingMatcher<T> {
    private final Iterable<? extends ReportingMatcher<? super T>> reportingMatchers;

    SequenceMatcher(Iterable<? extends ReportingMatcher<? super T>> reportingMatchers) {
        this.reportingMatchers = reportingMatchers;
    }

    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        for (ReportingMatcher<? super T> reportingMatcher : reportingMatchers) {
            reportingMatcher.run(item, safeTreeReporter);
        }
    }

    @Override
    public void runForMissingItem(SafeTreeReporter safeTreeReporter) {
        for (ReportingMatcher<? super T> reportingMatcher : reportingMatchers) {
            reportingMatcher.runForMissingItem(safeTreeReporter);
        }
    }
}
