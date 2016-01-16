package com.github.alkedr.matchers.reporting.utility;

import com.github.alkedr.matchers.reporting.BaseReportingMatcher;
import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.hamcrest.Description;

public class MergingMatcher<T> extends BaseReportingMatcher<T> {
    private final ReportingMatcher<T> reportingMatcher;

    public MergingMatcher(ReportingMatcher<T> reportingMatcher) {
        this.reportingMatcher = reportingMatcher;
    }

    @Override
    public void run(Object item, CheckListener checkListener) {
        MergingCheckListener mergingCheckListener = new MergingCheckListener(checkListener);
        reportingMatcher.run(item, mergingCheckListener);
        mergingCheckListener.flush();
    }

    @Override
    public void runForMissingItem(CheckListener checkListener) {
        MergingCheckListener mergingCheckListener = new MergingCheckListener(checkListener);
        reportingMatcher.runForMissingItem(mergingCheckListener);
        mergingCheckListener.flush();
    }

    @Override
    public void describeTo(Description description) {
        reportingMatcher.describeTo(description);
    }
}
