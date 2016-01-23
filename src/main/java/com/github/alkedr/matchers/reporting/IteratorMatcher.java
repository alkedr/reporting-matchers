package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.element.checkers.ElementChecker;
import com.github.alkedr.matchers.reporting.keys.ElementKey;
import com.github.alkedr.matchers.reporting.reporters.Reporter;

import java.util.Iterator;
import java.util.function.Supplier;

// TODO: заюзать SequenceOfMergedSubValueCheckResults
// По-хорошему надо объединять missing?
class IteratorMatcher<T> extends BaseReportingMatcher<Iterator<T>> {
    private final Supplier<ElementChecker> elementCheckerSupplier;

    IteratorMatcher(Supplier<ElementChecker> elementCheckerSupplier) {
        this.elementCheckerSupplier = elementCheckerSupplier;
    }

    @Override
    public void run(Object item, Reporter reporter) {
        ElementChecker elementChecker = elementCheckerSupplier.get();
        elementChecker.begin(reporter);
        Iterator<?> iterator = (Iterator<?>) item;
        int i = 0;
        while (iterator.hasNext()) {
            int index = i++;
            elementChecker.element(new ElementKey(index), iterator.next(), reporter);
        }
        elementChecker.end(reporter);
    }

    @Override
    public void runForMissingItem(Reporter reporter) {
        ElementChecker elementChecker = elementCheckerSupplier.get();
        elementChecker.begin(reporter);
        elementChecker.end(reporter);
    }
}
