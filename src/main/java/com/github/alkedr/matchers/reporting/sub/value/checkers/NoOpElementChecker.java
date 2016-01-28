package com.github.alkedr.matchers.reporting.sub.value.checkers;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.util.function.Consumer;

class NoOpElementChecker implements SubValuesChecker {
    static final NoOpElementChecker INSTANCE = new NoOpElementChecker();

    private NoOpElementChecker() {
    }

    @Override
    public void begin(SafeTreeReporter safeTreeReporter) {
    }

    @Override
    public Consumer<SafeTreeReporter> present(Key key, Object value) {
        return safeTreeReporter -> {};
    }

    @Override
    public Consumer<SafeTreeReporter> missing(Key key) {
        return safeTreeReporter -> {};
    }

    @Override
    public Consumer<SafeTreeReporter> broken(Key key, Throwable throwable) {
        return safeTreeReporter -> {};
    }

    @Override
    public void end(SafeTreeReporter safeTreeReporter) {
    }
}
