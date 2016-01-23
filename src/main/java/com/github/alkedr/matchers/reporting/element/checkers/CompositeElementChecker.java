package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.Reporter;

class CompositeElementChecker implements ElementChecker {
    private final Iterable<ElementChecker> elementCheckers;

    CompositeElementChecker(Iterable<ElementChecker> elementCheckers) {
        this.elementCheckers = elementCheckers;
    }

    @Override
    public void begin(Reporter reporter) {
        for (ElementChecker elementChecker : elementCheckers) {
            elementChecker.begin(reporter);
        }
    }

    @Override
    public void element(Key key, Object value, Reporter reporter) {
        for (ElementChecker elementChecker : elementCheckers) {
            elementChecker.element(key, value, reporter);
        }
    }

    @Override
    public void end(Reporter reporter) {
        for (ElementChecker elementChecker : elementCheckers) {
            elementChecker.end(reporter);
        }
    }
}
