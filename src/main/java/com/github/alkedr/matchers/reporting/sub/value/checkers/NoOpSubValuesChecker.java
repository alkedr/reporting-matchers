package com.github.alkedr.matchers.reporting.sub.value.checkers;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.util.function.Consumer;

class NoOpSubValuesChecker implements SubValuesChecker {
    static final NoOpSubValuesChecker INSTANCE = new NoOpSubValuesChecker();
    private static final Consumer<SafeTreeReporter> NO_OP_SAFE_TREE_REPORTER_CONSUMER = safeTreeReporter -> {};

    private NoOpSubValuesChecker() {
    }

    @Override
    public void begin(SafeTreeReporter safeTreeReporter) {
    }

    @Override
    public Consumer<SafeTreeReporter> present(Key key, Object value) {
        return NO_OP_SAFE_TREE_REPORTER_CONSUMER;
    }

    @Override
    public Consumer<SafeTreeReporter> absent(Key key) {
        return NO_OP_SAFE_TREE_REPORTER_CONSUMER;
    }

    @Override
    public Consumer<SafeTreeReporter> broken(Key key, Throwable throwable) {
        return NO_OP_SAFE_TREE_REPORTER_CONSUMER;
    }

    @Override
    public void end(SafeTreeReporter safeTreeReporter) {
    }
}
