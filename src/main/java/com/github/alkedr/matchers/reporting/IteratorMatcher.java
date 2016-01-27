package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.element.checkers.ElementChecker;
import com.github.alkedr.matchers.reporting.keys.ExtractableKey;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

import java.util.Iterator;
import java.util.function.Supplier;

import static com.github.alkedr.matchers.reporting.keys.Keys.elementKey;

class IteratorMatcher<T> extends BaseReportingMatcher<Iterator<T>> {
    private final Supplier<ElementChecker> elementCheckerSupplier;

    IteratorMatcher(Supplier<ElementChecker> elementCheckerSupplier) {
        this.elementCheckerSupplier = elementCheckerSupplier;
    }

    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        ElementChecker elementChecker = elementCheckerSupplier.get();
        elementChecker.begin(safeTreeReporter);
        Iterator<?> iterator = (Iterator<?>) item;
        int i = 0;
        while (iterator.hasNext()) {
            ExtractableKey key = elementKey(i++);
            Object value = iterator.next();
            safeTreeReporter.presentNode(key, value, elementChecker.element(key, value));
        }
        elementChecker.end(safeTreeReporter);
    }

    @Override
    public void runForMissingItem(SafeTreeReporter safeTreeReporter) {
        ElementChecker elementChecker = elementCheckerSupplier.get();
        elementChecker.begin(safeTreeReporter);
        elementChecker.end(safeTreeReporter);
    }
}
