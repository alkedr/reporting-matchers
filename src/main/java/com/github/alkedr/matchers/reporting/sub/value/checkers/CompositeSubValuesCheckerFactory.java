package com.github.alkedr.matchers.reporting.sub.value.checkers;

import java.util.ArrayList;
import java.util.Collection;

class CompositeSubValuesCheckerFactory implements SubValuesCheckerFactory {
    private final Iterable<SubValuesCheckerFactory> elementCheckerFactories;

    CompositeSubValuesCheckerFactory(Iterable<SubValuesCheckerFactory> elementCheckerFactories) {
        this.elementCheckerFactories = elementCheckerFactories;
    }

    @Override
    public SubValuesChecker create() {
        Collection<SubValuesChecker> elementCheckers = new ArrayList<>();
        for (SubValuesCheckerFactory factory : elementCheckerFactories) {
            elementCheckers.add(factory.create());
        }
        return SubValueCheckers.compositeElementChecker(elementCheckers);
    }
}
