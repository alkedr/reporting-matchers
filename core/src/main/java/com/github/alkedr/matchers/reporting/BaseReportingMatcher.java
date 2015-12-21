package com.github.alkedr.matchers.reporting;

import org.hamcrest.BaseMatcher;

public abstract class BaseReportingMatcher<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    @Override
    public boolean matches(Object item) {
        // TODO: бросать исключение если нужно прекращать проверки после первой неуспешной
        // TODO: (можно, потому что от ReportingMatcher'ов исключения не ловим)
        MatchesFlagRecordingReporter matchesFlagRecordingReporter = new MatchesFlagRecordingReporter();
        run(item, matchesFlagRecordingReporter);
        return matchesFlagRecordingReporter.getMatchesFlag();
    }
}
