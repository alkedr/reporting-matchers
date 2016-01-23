package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.keys.ElementKey;
import com.github.alkedr.matchers.reporting.keys.Key;
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
            int index1 = index++;
            reporter.beginMissingNode(new ElementKey(index1));
            elementMatchers.next().runForMissingItem(reporter);
            reporter.endNode();
        }
    }
}
