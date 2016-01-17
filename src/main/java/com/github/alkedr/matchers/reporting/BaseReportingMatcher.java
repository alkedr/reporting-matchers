package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.utility.MatchesFlagRecordingReporter;
import org.hamcrest.BaseMatcher;

//прежде чем наследоваться от этого класса, убедись, что тебе не подходят ExtractingMatcher и IteratingMatcher
public abstract class BaseReportingMatcher<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    @Override
    public boolean matches(Object item) {
        MatchesFlagRecordingReporter reporter = new MatchesFlagRecordingReporter();
        getChecks(item).run(item, reporter);
        return reporter.getMatchesFlag();
    }


    // TODO: заимплементить describeTo и describeMismatch здесь?
}
