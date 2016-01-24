package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

class CompositeElementChecker implements ElementChecker {
    private final Iterable<ElementChecker> elementCheckers;

    CompositeElementChecker(Iterable<ElementChecker> elementCheckers) {
        this.elementCheckers = elementCheckers;
    }

    @Override
    public void begin(SafeTreeReporter safeTreeReporter) {
        for (ElementChecker elementChecker : elementCheckers) {
            elementChecker.begin(safeTreeReporter);
        }
    }

    @Override
    public void element(Key key, Object value, SafeTreeReporter safeTreeReporter) {
        for (ElementChecker elementChecker : elementCheckers) {
            elementChecker.element(key, value, safeTreeReporter);
        }
    }

    @Override
    public void end(SafeTreeReporter safeTreeReporter) {
        for (ElementChecker elementChecker : elementCheckers) {
            elementChecker.end(safeTreeReporter);
        }
    }
}
