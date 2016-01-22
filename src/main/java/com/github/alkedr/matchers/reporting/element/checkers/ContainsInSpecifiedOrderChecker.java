package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.check.results.CheckResult;
import com.github.alkedr.matchers.reporting.check.results.CheckResults;
import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.keys.Keys;
import com.google.common.collect.Iterators;
import org.apache.commons.collections4.iterators.SingletonIterator;

import java.util.Iterator;

import static com.github.alkedr.matchers.reporting.check.results.CheckResults.incorrectlyPresent;
import static java.util.Collections.emptyIterator;

// TODO: пробовать пропускать элементы?
class ContainsInSpecifiedOrderChecker implements ElementChecker {
    private final Iterator<ReportingMatcher<?>> elementMatchers;
    private int index;

    ContainsInSpecifiedOrderChecker(Iterator<ReportingMatcher<?>> elementMatchers) {
        this.elementMatchers = elementMatchers;
    }

    @Override
    public Iterator<CheckResult> begin() {
        return emptyIterator();
    }

    @Override
    public Iterator<CheckResult> element(Key key, Object value) {
        index++;
        if (elementMatchers.hasNext()) {
            return elementMatchers.next().getChecks(value);
        }
        return new SingletonIterator<>(incorrectlyPresent());
    }

    @Override
    public Iterator<CheckResult> end() {
        return Iterators.transform(
                elementMatchers,
                matcher -> {
                    Key key = Keys.elementKey(index++);
                    return CheckResults.missingSubValue(key, matcher.getChecksForMissingItem());
                }
        );
    }
}
