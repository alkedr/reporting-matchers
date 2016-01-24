package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.MatchesFlagRecordingReporter;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.matchesFlagRecordingReporter;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;

//прежде чем наследоваться от этого класса, убедись, что тебе не подходят ExtractingMatcher и IteratingMatcher
public abstract class BaseReportingMatcher<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    @Override
    public boolean matches(Object item) {
        MatchesFlagRecordingReporter reporter = matchesFlagRecordingReporter();
        run(item, simpleTreeReporterToSafeTreeReporter(reporter));
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
