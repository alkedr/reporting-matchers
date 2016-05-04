package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.uncheckedToIncorrectlyPresentConvertingReporter;

public class UncheckedElementsAreFailsMatcher<T> extends BaseReportingMatcher<T> {
    private final ReportingMatcher<? super T> matcher;

    public UncheckedElementsAreFailsMatcher(ReportingMatcher<? super T> matcher) {
        this.matcher = matcher;
    }

    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        matcher.run(item, uncheckedToIncorrectlyPresentConvertingReporter(safeTreeReporter));
    }

    @Override
    public void runForAbsentItem(SafeTreeReporter safeTreeReporter) {
        matcher.runForAbsentItem(uncheckedToIncorrectlyPresentConvertingReporter(safeTreeReporter));
    }
}
