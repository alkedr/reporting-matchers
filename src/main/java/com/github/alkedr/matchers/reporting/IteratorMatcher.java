package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.check.results.CheckResult;
import com.github.alkedr.matchers.reporting.check.results.CheckResults;
import com.github.alkedr.matchers.reporting.element.checkers.ElementChecker;
import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.keys.Keys;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import static java.util.Collections.emptyIterator;

// TODO: заюзать группы KeyValueChecks
// По хорошему надо объединять missing?
class IteratorMatcher<T> extends BaseReportingMatcher<Iterator<T>> {
    private final Supplier<ElementChecker> elementCheckerSupplier;

    IteratorMatcher(Supplier<ElementChecker> elementCheckerSupplier) {
        this.elementCheckerSupplier = elementCheckerSupplier;
    }

    @Override
    public Iterator<CheckResult> getChecks(Object item) {
        return new ChecksIterator(elementCheckerSupplier.get(), (Iterator<Object>) item);
    }

    @Override
    public Iterator<CheckResult> getChecksForMissingItem() {
        return new ChecksIterator(elementCheckerSupplier.get(), emptyIterator());
    }


    private static class ChecksIterator implements Iterator<CheckResult> {
        private final ElementChecker elementChecker;
        private Iterator<CheckResult> begin;
        private Iterator<CheckResult> end;
        private int index = 0;
        private final Iterator<Object> iterator;

        ChecksIterator(ElementChecker elementChecker, Iterator<Object> iterator) {
            this.elementChecker = elementChecker;
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            if (begin == null) {
                begin = elementChecker.begin();
            }
            if (begin.hasNext()) {
                return true;
            }
            if (iterator.hasNext()) {
                return true;
            }
            if (end == null) {
                end = elementChecker.end();
            }
            if (end.hasNext()) {
                return true;
            }
            return false;
        }

        @Override
        public CheckResult next() {
            if (begin == null) {
                begin = elementChecker.begin();
            }
            if (begin.hasNext()) {
                return begin.next();
            }
            if (iterator.hasNext()) {
                Key key = Keys.elementKey(index++);
                Object value = iterator.next();
                Iterator<CheckResult> checks = elementChecker.element(key, value);
                return CheckResults.presentSubValue(key, value, checks);
            }
            if (end == null) {
                end = elementChecker.end();
            }
            if (end.hasNext()) {
                return end.next();
            }
            throw new NoSuchElementException();  // FIXME
        }
    }
}
