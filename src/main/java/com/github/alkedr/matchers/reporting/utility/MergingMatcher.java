package com.github.alkedr.matchers.reporting.utility;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.base.BaseReportingMatcher;
import org.hamcrest.Description;

import java.util.Iterator;

import static com.github.alkedr.matchers.reporting.ReportingMatchersRunningUtils.mergeReportingMatcherChecks;

public class MergingMatcher<T> extends BaseReportingMatcher<T> {
    private final ReportingMatcher<T> reportingMatcher;

    public MergingMatcher(ReportingMatcher<T> reportingMatcher) {
        this.reportingMatcher = reportingMatcher;
    }

    @Override
    public Iterator<Object> run(Object item) {
        return mergeReportingMatcherChecks(reportingMatcher.run(item));
    }

    @Override
    public Iterator<Object> runForMissingItem() {
        return mergeReportingMatcherChecks(reportingMatcher.runForMissingItem());
    }

    @Override
    public void describeTo(Description description) {
        reportingMatcher.describeTo(description);
    }
}
