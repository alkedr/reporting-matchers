package com.github.alkedr.matchers.reporting.iteration;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.google.common.collect.Iterators;

import java.util.Iterator;

import static com.github.alkedr.matchers.reporting.utility.NoOpMatcher.noOp;
import static java.util.Collections.emptyIterator;

// TODO: возможность пропускать элементы если это позволяет уменьшить кол-во красного
public class InSpecifiedOrderIteratorMatcher<T> extends IteratorMatcher<T> {
    private final Iterable<ReportingMatcher<T>> matchers;

    public InSpecifiedOrderIteratorMatcher(Iterable<ReportingMatcher<T>> matchers) {
        this.matchers = matchers;
    }

    @Override
    protected Checker createChecker() {
        return new InSpecifiedOrderChecker<>(matchers.iterator());
    }

    private static class InSpecifiedOrderChecker<T> implements Checker {
        private final Iterator<ReportingMatcher<T>> matchers;

        InSpecifiedOrderChecker(Iterator<ReportingMatcher<T>> matchers) {
            this.matchers = matchers;
        }

        @Override
        public ReportingMatcher.Checks element(ReportingMatcher.Key key, ReportingMatcher.Value value) {
            if (matchers.hasNext()) {
                return new ReportingMatcher.Checks(ReportingMatcher.PresenceStatus.PRESENT, matchers.next());
            }
            return new ReportingMatcher.Checks(ReportingMatcher.PresenceStatus.MISSING, noOp());
        }

        @Override
        public Iterator<Object> end() {
            return Iterators.concat(Iterators.transform(matchers, ReportingMatcher::runForMissingItem));
        }

        @Override
        public Iterator<Object> end2() {
            return emptyIterator();
        }
    }
}
