package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.check.results.CheckResult;
import com.github.alkedr.matchers.reporting.keys.Key;
import com.google.common.collect.Iterators;

import java.util.Iterator;

class CompositeElementChecker implements ElementChecker {
    private final Iterable<ElementChecker> elementCheckers;

    CompositeElementChecker(Iterable<ElementChecker> elementCheckers) {
        this.elementCheckers = elementCheckers;
    }

    @Override
    public Iterator<CheckResult> begin() {
        return Iterators.concat(
                Iterators.transform(
                        elementCheckers.iterator(),
                        ElementChecker::begin
                )
        );
    }

    @Override
    public Iterator<CheckResult> element(Key key, Object value) {
        return Iterators.concat(
                Iterators.transform(
                        elementCheckers.iterator(),
                        elementChecker -> elementChecker.element(key, value)
                )
        );
    }

    @Override
    public Iterator<CheckResult> end() {
        return Iterators.concat(
                Iterators.transform(
                        elementCheckers.iterator(),
                        ElementChecker::end
                )
        );
    }
}
