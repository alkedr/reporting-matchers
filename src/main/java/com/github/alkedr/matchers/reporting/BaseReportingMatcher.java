package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.check.results.CheckResults;
import com.github.alkedr.matchers.reporting.reporters.MatchesFlagRecordingReporter;
import org.hamcrest.BaseMatcher;

//прежде чем наследоваться от этого класса, убедись, что тебе не подходят ExtractingMatcher и IteratingMatcher
public abstract class BaseReportingMatcher<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    @Override
    public boolean matches(Object item) {
        MatchesFlagRecordingReporter reporter = new MatchesFlagRecordingReporter();
        CheckResults.runAll(getChecks(item), reporter);
        return reporter.getMatchesFlag();
    }


    // TODO: заимплементить describeTo и describeMismatch здесь?
}
