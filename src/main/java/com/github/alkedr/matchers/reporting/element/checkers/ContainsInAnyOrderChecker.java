package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.FlatReporter;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

import static com.github.alkedr.matchers.reporting.keys.Keys.elementKey;

class ContainsInAnyOrderChecker implements ElementChecker {
    private final Iterable<ReportingMatcher<?>> elementMatchers;
    private int index = 0;

    ContainsInAnyOrderChecker(Collection<ReportingMatcher<?>> elementMatchers) {
        this.elementMatchers = new ArrayList<>(elementMatchers);
    }

    @Override
    public void begin(SafeTreeReporter safeTreeReporter) {
    }

    @Override
    public Consumer<SafeTreeReporter> element(Key key, Object value) {
        index++;
        Iterator<ReportingMatcher<?>> iterator = elementMatchers.iterator();
        while (iterator.hasNext()) {
            ReportingMatcher<?> matcher = iterator.next();
            if (matcher.matches(value)) {
                iterator.remove();
                return safeTreeReporter -> matcher.run(value, safeTreeReporter);
            }
        }
        return FlatReporter::incorrectlyPresent;
    }

    @Override
    public void end(SafeTreeReporter safeTreeReporter) {
        for (ReportingMatcher<?> matcher : elementMatchers) {
            safeTreeReporter.missingNode(elementKey(index++), r -> matcher.runForMissingItem(safeTreeReporter));
        }
    }
}
