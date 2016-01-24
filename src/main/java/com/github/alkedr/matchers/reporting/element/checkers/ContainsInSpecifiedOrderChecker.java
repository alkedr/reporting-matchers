package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.keys.Keys;
import com.github.alkedr.matchers.reporting.reporters.Reporter;

import java.util.Iterator;

// TODO: пробовать пропускать элементы?
class ContainsInSpecifiedOrderChecker implements ElementChecker {
    private final Iterator<ReportingMatcher<?>> elementMatchers;
    private int index;

    ContainsInSpecifiedOrderChecker(Iterator<ReportingMatcher<?>> elementMatchers) {
        this.elementMatchers = elementMatchers;
    }

    @Override
    public void begin(Reporter reporter) {
    }

    @Override
    public void element(Key key, Object value, Reporter reporter) {
        index++;
        if (elementMatchers.hasNext()) {
            elementMatchers.next().run(value, reporter);
        } else {
            reporter.incorrectlyPresent();
        }
    }

    @Override
    public void end(Reporter reporter) {
        while (elementMatchers.hasNext()) {
            reporter.missingNode(Keys.elementKey(index++), r -> elementMatchers.next().runForMissingItem(reporter));
        }
    }
}
