package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.keys.Keys;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

import java.util.Iterator;

// TODO: пробовать пропускать элементы?
class ContainsInSpecifiedOrderChecker implements ElementChecker {
    private final Iterator<ReportingMatcher<?>> elementMatchers;
    private int index;

    ContainsInSpecifiedOrderChecker(Iterator<ReportingMatcher<?>> elementMatchers) {
        this.elementMatchers = elementMatchers;
    }

    @Override
    public void begin(SafeTreeReporter safeTreeReporter) {
    }

    @Override
    public void element(Key key, Object value, SafeTreeReporter safeTreeReporter) {
        index++;
        if (elementMatchers.hasNext()) {
            elementMatchers.next().run(value, safeTreeReporter);
        } else {
            safeTreeReporter.incorrectlyPresent();
        }
    }

    @Override
    public void end(SafeTreeReporter safeTreeReporter) {
        while (elementMatchers.hasNext()) {
            safeTreeReporter.missingNode(Keys.elementKey(index++), r -> elementMatchers.next().runForMissingItem(safeTreeReporter));
        }
    }
}
