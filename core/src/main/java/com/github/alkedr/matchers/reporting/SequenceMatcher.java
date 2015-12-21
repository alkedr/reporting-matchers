package com.github.alkedr.matchers.reporting;

import org.hamcrest.Description;

/**
 * "Склеивает" несколько ReportingMatcher'ов (при вызове run или matches запускает их последовательно)
 */
public class SequenceMatcher<T> extends BaseReportingMatcher<T> {
    private final Iterable<ReportingMatcher<? extends T>> matchers;

    public SequenceMatcher(Iterable<ReportingMatcher<? extends T>> matchers) {
        this.matchers = matchers;
    }

    @Override
    public void run(Object item, Reporter reporter) {
        for (ReportingMatcher<? extends T> matcher : matchers) {
            matcher.run(item, reporter);
        }
    }

    @Override
    public void runForMissingItem(Reporter reporter) {
        for (ReportingMatcher<? extends T> matcher : matchers) {
            matcher.runForMissingItem(reporter);
        }
    }

    @Override
    public void describeTo(Description description) {
        // TODO
    }
}
