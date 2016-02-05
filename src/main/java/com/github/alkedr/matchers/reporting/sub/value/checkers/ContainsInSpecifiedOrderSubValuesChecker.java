package com.github.alkedr.matchers.reporting.sub.value.checkers;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;

// TODO: пробовать пропускать элементы?
class ContainsInSpecifiedOrderSubValuesChecker<T> implements SubValuesChecker {
    private final Iterator<? extends ReportingMatcher<? super T>> elementMatchers;
    private int index = 0;

    ContainsInSpecifiedOrderSubValuesChecker(Iterator<? extends ReportingMatcher<? super T>> elementMatchers) {
        this.elementMatchers = elementMatchers;
    }

    @Override
    public void begin(SafeTreeReporter safeTreeReporter) {
    }

    @Override
    public Consumer<SafeTreeReporter> present(Key key, Object value) {
        return value(reportingMatcher -> reporter -> reportingMatcher.run(value, reporter));
    }

    @Override
    public Consumer<SafeTreeReporter> absent(Key key) {
        return value(reportingMatcher -> reportingMatcher::runForAbsentItem);
    }

    @Override
    public Consumer<SafeTreeReporter> broken(Key key, Throwable throwable) {
        return value(reportingMatcher -> reportingMatcher::runForAbsentItem);
    }

    private Consumer<SafeTreeReporter> value(Function<ReportingMatcher<?>, Consumer<SafeTreeReporter>> function) {
        index++;
        if (elementMatchers.hasNext()) {
            return function.apply(elementMatchers.next());
        }
        return reporter -> {};
    }

    @Override
    public void end(SafeTreeReporter safeTreeReporter) {
        while (elementMatchers.hasNext()) {
            ReportingMatcher<?> matcher = elementMatchers.next();
            safeTreeReporter.absentNode(elementKey(index++), r -> matcher.runForAbsentItem(safeTreeReporter));
        }
        if (index == 0) {
            safeTreeReporter.passedCheck("empty list");
        }
    }
}
