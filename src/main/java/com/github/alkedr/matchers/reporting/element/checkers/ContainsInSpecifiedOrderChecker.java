package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.FlatReporter;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

import java.util.Iterator;
import java.util.function.Consumer;

import static com.github.alkedr.matchers.reporting.keys.Keys.elementKey;

// TODO: пробовать пропускать элементы?
class ContainsInSpecifiedOrderChecker implements ElementChecker {
    private final Iterator<ReportingMatcher<?>> elementMatchers;
    private final boolean extraElementsAllowed;
    private int index = 0;

    ContainsInSpecifiedOrderChecker(Iterator<ReportingMatcher<?>> elementMatchers, boolean extraElementsAllowed) {
        this.elementMatchers = elementMatchers;
        this.extraElementsAllowed = extraElementsAllowed;
    }

    @Override
    public void begin(SafeTreeReporter safeTreeReporter) {
    }

    @Override
    public Consumer<SafeTreeReporter> element(Key key, Object value) {
        index++;
        if (elementMatchers.hasNext()) {
            ReportingMatcher<?> matcher = elementMatchers.next();
            return safeTreeReporter -> matcher.run(value, safeTreeReporter);
        }
        return extraElementsAllowed ? reporter -> {} : FlatReporter::incorrectlyPresent;
    }

    @Override
    public void end(SafeTreeReporter safeTreeReporter) {
        while (elementMatchers.hasNext()) {
            ReportingMatcher<?> matcher = elementMatchers.next();
            safeTreeReporter.missingNode(elementKey(index++), r -> matcher.runForMissingItem(safeTreeReporter));
        }
    }
}
