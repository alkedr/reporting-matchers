package com.github.alkedr.matchers.reporting.sub.value.checkers;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class CompositeSubValuesChecker implements SubValuesChecker {
    private final Iterable<SubValuesChecker> elementCheckers;

    CompositeSubValuesChecker(Iterable<SubValuesChecker> elementCheckers) {
        this.elementCheckers = elementCheckers;
    }

    @Override
    public void begin(SafeTreeReporter safeTreeReporter) {
        call(SubValuesChecker::begin, safeTreeReporter);
    }

    @Override
    public Consumer<SafeTreeReporter> present(Key key, Object value) {
        Collection<Consumer<SafeTreeReporter>> results = new ArrayList<>();
        for (SubValuesChecker elementChecker : elementCheckers) {
            results.add(elementChecker.present(key, value));
        }
        return safeTreeReporter -> {
            for (Consumer<SafeTreeReporter> result : results) {
                result.accept(safeTreeReporter);
            }
        };
    }

    @Override
    public Consumer<SafeTreeReporter> missing(Key key) {
        // TODO
        return null;
    }

    @Override
    public Consumer<SafeTreeReporter> broken(Key key, Throwable throwable) {
        // TODO
        return null;
    }

    @Override
    public void end(SafeTreeReporter safeTreeReporter) {
        call(SubValuesChecker::end, safeTreeReporter);
    }

    private void call(BiConsumer<SubValuesChecker, SafeTreeReporter> method, SafeTreeReporter safeTreeReporter) {
        for (SubValuesChecker elementChecker : elementCheckers) {
            method.accept(elementChecker, safeTreeReporter);
        }
    }
}
