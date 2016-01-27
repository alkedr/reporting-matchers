package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

import java.util.function.Consumer;

public class NoOpElementChecker implements ElementChecker {
    static final ElementChecker INSTANCE = new NoOpElementChecker();

    private NoOpElementChecker() {
    }

    @Override
    public void begin(SafeTreeReporter safeTreeReporter) {
    }

    @Override
    public Consumer<SafeTreeReporter> element(Key key, Object value) {
        return safeTreeReporter -> {};
    }

    @Override
    public void end(SafeTreeReporter safeTreeReporter) {
    }
}
