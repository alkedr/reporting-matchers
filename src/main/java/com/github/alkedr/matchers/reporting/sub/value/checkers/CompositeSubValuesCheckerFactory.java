package com.github.alkedr.matchers.reporting.sub.value.checkers;

import java.util.ArrayList;
import java.util.Collection;

import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.compositeSubValuesChecker;

class CompositeSubValuesCheckerFactory implements SubValuesCheckerFactory {
    private final Iterable<SubValuesCheckerFactory> subValuesCheckerFactories;

    CompositeSubValuesCheckerFactory(Iterable<SubValuesCheckerFactory> subValuesCheckerFactories) {
        this.subValuesCheckerFactories = subValuesCheckerFactories;
    }

    @Override
    public SubValuesChecker create() {
        Collection<SubValuesChecker> subValuesCheckers = new ArrayList<>();
        for (SubValuesCheckerFactory factory : subValuesCheckerFactories) {
            subValuesCheckers.add(factory.create());
        }
        return compositeSubValuesChecker(subValuesCheckers);
    }
}
