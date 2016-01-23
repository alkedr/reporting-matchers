package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.Reporter;

class SequenceMatcher<T> extends BaseReportingMatcher<T> {
    private final Iterable<? extends ReportingMatcher<? super T>> reportingMatchers;

    SequenceMatcher(Iterable<? extends ReportingMatcher<? super T>> reportingMatchers) {
        this.reportingMatchers = reportingMatchers;
    }

    @Override
    public void run(Object item, Reporter reporter) {
        for (ReportingMatcher<? super T> reportingMatcher : reportingMatchers) {
            reportingMatcher.run(item, reporter);
        }
    }

    @Override
    public void runForMissingItem(Reporter reporter) {
        for (ReportingMatcher<? super T> reportingMatcher : reportingMatchers) {
            reportingMatcher.runForMissingItem(reporter);
        }
    }
}
