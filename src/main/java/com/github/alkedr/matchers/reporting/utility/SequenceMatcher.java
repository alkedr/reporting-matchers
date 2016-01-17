package com.github.alkedr.matchers.reporting.utility;

import com.github.alkedr.matchers.reporting.BaseReportingMatcher;
import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.hamcrest.Description;

/**
 * "Склеивает" несколько ReportingMatcher'ов (при вызове run или matches запускает их последовательно)
 */
public class SequenceMatcher<T> extends BaseReportingMatcher<T> {
    private final Iterable<? extends ReportingMatcher<? super T>> matchers;

    public SequenceMatcher(Iterable<? extends ReportingMatcher<? super T>> matchers) {
        this.matchers = matchers;
    }

    @Override
    public void run(Object item, CheckListener checkListener) {
        for (ReportingMatcher<? super T> reportingMatcher : matchers) {
            reportingMatcher.getChecks(item, checkListener);
        }
    }

    @Override
    public void runForMissingItem(CheckListener checkListener) {
        for (ReportingMatcher<? super T> reportingMatcher : matchers) {
            reportingMatcher.runForMissingItem(checkListener);
        }
    }

    @Override
    public void describeTo(Description description) {
        // TODO
    }
}
