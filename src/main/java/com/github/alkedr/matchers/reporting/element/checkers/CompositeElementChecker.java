package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

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
    public Consumer<SafeTreeReporter> element(Key key, Object value) {
        Collection<Consumer<SafeTreeReporter>> results = new ArrayList<>();
        for (ElementChecker elementChecker : elementCheckers) {
            results.add(elementChecker.element(key, value));
        }
        return safeTreeReporter -> {
            for (Consumer<SafeTreeReporter> result : results) {
                result.accept(safeTreeReporter);
            }
        };
    }

    @Override
    public void end(SafeTreeReporter safeTreeReporter) {
        for (ElementChecker elementChecker : elementCheckers) {
            elementChecker.end(safeTreeReporter);
        }
    }
}
