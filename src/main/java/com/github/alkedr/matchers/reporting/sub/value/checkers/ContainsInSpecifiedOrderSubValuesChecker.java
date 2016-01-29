package com.github.alkedr.matchers.reporting.sub.value.checkers;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.reporters.FlatReporter;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.util.Iterator;
import java.util.function.Consumer;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;

// TODO: пробовать пропускать элементы?
class ContainsInSpecifiedOrderSubValuesChecker implements SubValuesChecker {
    private final Iterator<ReportingMatcher<?>> elementMatchers;
    private final boolean extraElementsAllowed;
    private int index = 0;

    ContainsInSpecifiedOrderSubValuesChecker(Iterator<ReportingMatcher<?>> elementMatchers, boolean extraElementsAllowed) {
        this.elementMatchers = elementMatchers;
        this.extraElementsAllowed = extraElementsAllowed;
    }

    @Override
    public void begin(SafeTreeReporter safeTreeReporter) {
    }

    @Override
    public Consumer<SafeTreeReporter> present(Key key, Object value) {
        index++;
        if (elementMatchers.hasNext()) {
            ReportingMatcher<?> matcher = elementMatchers.next();
            return safeTreeReporter -> matcher.run(value, safeTreeReporter);
        }
        return extraElementsAllowed ? reporter -> {} : FlatReporter::incorrectlyPresent;
    }

    @Override
    public Consumer<SafeTreeReporter> absent(Key key) {
        return null;
    }

    @Override
    public Consumer<SafeTreeReporter> broken(Key key, Throwable throwable) {
        return null;
    }

    @Override
    public void end(SafeTreeReporter safeTreeReporter) {
        while (elementMatchers.hasNext()) {
            ReportingMatcher<?> matcher = elementMatchers.next();
            safeTreeReporter.absentNode(elementKey(index++), r -> matcher.runForAbsentItem(safeTreeReporter));
        }
    }
}
