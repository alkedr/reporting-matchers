package com.github.alkedr.matchers.reporting.sub.value.checkers;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.util.function.Consumer;

public class AllMatchSubValuesChecker implements SubValuesChecker {
    private final ReportingMatcher<?> matcherForExtractedValue;

    public AllMatchSubValuesChecker(ReportingMatcher<?> matcherForExtractedValue) {
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
    public Consumer<SafeTreeReporter> missing(Key key) {
        return matcherForExtractedValue::runForMissingItem;
    }

    @Override
    public Consumer<SafeTreeReporter> broken(Key key, Throwable throwable) {
        return matcherForExtractedValue::runForMissingItem;
    }

    @Override
    public void end(SafeTreeReporter safeTreeReporter) {
    }
}
