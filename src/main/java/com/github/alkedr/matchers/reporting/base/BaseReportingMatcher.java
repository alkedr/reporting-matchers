package com.github.alkedr.matchers.reporting.base;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.hamcrest.BaseMatcher;

import static com.github.alkedr.matchers.reporting.ReportingMatchersRunningUtils.runReportingMatcher;

public abstract class BaseReportingMatcher<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    @Override
    public boolean matches(Object item) {
        MatchesFlagRecordingReporter reporter = new MatchesFlagRecordingReporter();
        runReportingMatcher(reporter, item, this);
        return reporter.getMatchesFlag();
    }
}
