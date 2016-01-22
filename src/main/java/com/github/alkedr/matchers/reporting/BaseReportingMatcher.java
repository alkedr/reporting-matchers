package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.check.results.CheckResults;
import com.github.alkedr.matchers.reporting.reporters.MatchesFlagRecordingReporter;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

//прежде чем наследоваться от этого класса, убедись, что тебе не подходят ExtractingMatcher и IteratingMatcher
public abstract class BaseReportingMatcher<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    @Override
    public boolean matches(Object item) {
        MatchesFlagRecordingReporter reporter = new MatchesFlagRecordingReporter();
        CheckResults.runAll(getChecks(item), reporter);
        return reporter.getMatchesFlag();
    }

    @Override
    public void describeTo(Description description) {
        // TODO
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        // TODO
        super.describeMismatch(item, description);
    }
}
