package com.github.alkedr.matchers.reporting.sub.value.checkers;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.util.function.Consumer;

class MatcherSubValuesChecker implements SubValuesChecker {
    private final ReportingMatcher<?> matcherForExtractedValue;

    MatcherSubValuesChecker(ReportingMatcher<?> matcherForExtractedValue) {
        this.matcherForExtractedValue = matcherForExtractedValue;
    }

    @Override
    public void begin(SafeTreeReporter safeTreeReporter) {
    }

    @Override
    public Consumer<SafeTreeReporter> present(Key key, Object value) {
        return safeTreeReporter -> matcherForExtractedValue.run(value, safeTreeReporter);
    }

    @Override
    public Consumer<SafeTreeReporter> absent(Key key) {
        return matcherForExtractedValue::runForAbsentItem;
    }

    @Override
    public Consumer<SafeTreeReporter> broken(Key key, Throwable throwable) {
        return matcherForExtractedValue::runForAbsentItem;
    }

    @Override
    public void end(SafeTreeReporter safeTreeReporter) {
    }
}
