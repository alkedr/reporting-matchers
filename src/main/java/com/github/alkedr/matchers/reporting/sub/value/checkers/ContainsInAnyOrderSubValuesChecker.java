package com.github.alkedr.matchers.reporting.sub.value.checkers;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;

class ContainsInAnyOrderSubValuesChecker implements SubValuesChecker {
    private final Iterable<ReportingMatcher<?>> elementMatchers;
    private int index = 0;

    ContainsInAnyOrderSubValuesChecker(Collection<ReportingMatcher<?>> elementMatchers) {
        this.elementMatchers = new ArrayList<>(elementMatchers);
    }

    @Override
    public void begin(SafeTreeReporter safeTreeReporter) {
    }

    @Override
    public Consumer<SafeTreeReporter> present(Key key, Object value) {
        index++;
        Iterator<ReportingMatcher<?>> iterator = elementMatchers.iterator();
        while (iterator.hasNext()) {
            ReportingMatcher<?> matcher = iterator.next();
            if (matcher.matches(value)) {
                iterator.remove();
                return safeTreeReporter -> matcher.run(value, safeTreeReporter);
            }
        }
        return reporter -> {};
    }

    @Override
    public Consumer<SafeTreeReporter> absent(Key key) {
        index++;
        return reporter -> {};
    }

    @Override
    public Consumer<SafeTreeReporter> broken(Key key, Throwable throwable) {
        index++;
        return reporter -> {};
    }

    @Override
    public void end(SafeTreeReporter safeTreeReporter) {
        for (ReportingMatcher<?> matcher : elementMatchers) {
            safeTreeReporter.absentNode(elementKey(index++), r -> matcher.runForAbsentItem(safeTreeReporter));
        }
    }
}
