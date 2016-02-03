package com.github.alkedr.matchers.reporting.sub.value.checkers;

import java.util.ArrayList;
import java.util.Collection;

import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.compositeSubValuesChecker;

class CompositeSubValuesCheckerFactory<T> implements SubValuesCheckerFactory<T> {
    private final Iterable<SubValuesCheckerFactory<T>> subValuesCheckerFactories;

    CompositeSubValuesCheckerFactory(Iterable<SubValuesCheckerFactory<T>> subValuesCheckerFactories) {
        this.subValuesCheckerFactories = subValuesCheckerFactories;
    }

    @Override
    public SubValuesChecker createSubValuesChecker() {
        Collection<SubValuesChecker> subValuesCheckers = new ArrayList<>();
        for (SubValuesCheckerFactory<T> factory : subValuesCheckerFactories) {
            subValuesCheckers.add(factory.createSubValuesChecker());
        }
        return compositeSubValuesChecker(subValuesCheckers);
    }
}
