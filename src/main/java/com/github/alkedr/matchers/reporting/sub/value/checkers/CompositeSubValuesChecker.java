package com.github.alkedr.matchers.reporting.sub.value.checkers;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

class CompositeSubValuesChecker implements SubValuesChecker {
    private final Iterable<SubValuesChecker> subValuesCheckers;

    CompositeSubValuesChecker(Iterable<SubValuesChecker> subValuesCheckers) {
        this.subValuesCheckers = subValuesCheckers;
    }

    @Override
    public void begin(SafeTreeReporter safeTreeReporter) {
        call(SubValuesChecker::begin, safeTreeReporter);
    }

    @Override
    public Consumer<SafeTreeReporter> present(Key key, Object value) {
        return getMergedConsumer(subValuesChecker -> subValuesChecker.present(key, value));
    }

    @Override
    public Consumer<SafeTreeReporter> absent(Key key) {
        return getMergedConsumer(subValuesChecker -> subValuesChecker.absent(key));
    }

    @Override
    public Consumer<SafeTreeReporter> broken(Key key, Throwable throwable) {
        return getMergedConsumer(subValuesChecker -> subValuesChecker.broken(key, throwable));
    }

    @Override
    public void end(SafeTreeReporter safeTreeReporter) {
        call(SubValuesChecker::end, safeTreeReporter);
    }

    private void call(BiConsumer<SubValuesChecker, SafeTreeReporter> method, SafeTreeReporter safeTreeReporter) {
        for (SubValuesChecker subValuesChecker : subValuesCheckers) {
            method.accept(subValuesChecker, safeTreeReporter);
        }
    }

    public Consumer<SafeTreeReporter> getMergedConsumer(Function<SubValuesChecker, Consumer<SafeTreeReporter>> function) {
        Collection<Consumer<SafeTreeReporter>> results = new ArrayList<>();
        for (SubValuesChecker subValuesChecker : subValuesCheckers) {
            results.add(function.apply(subValuesChecker));
        }
        return safeTreeReporter -> {
            for (Consumer<SafeTreeReporter> result : results) {
                result.accept(safeTreeReporter);
            }
        };
    }
}
