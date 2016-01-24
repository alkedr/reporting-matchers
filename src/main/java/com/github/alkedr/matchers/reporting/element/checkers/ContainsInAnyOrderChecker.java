package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

import java.util.function.Consumer;

@Deprecated
class ContainsInAnyOrderChecker implements ElementChecker {
    @Override
    public void begin(SafeTreeReporter safeTreeReporter) {

    }

    @Override
    public Consumer<SafeTreeReporter> element(Key key, Object value) {

        return null;
    }

    @Override
    public void end(SafeTreeReporter safeTreeReporter) {

    }
}
