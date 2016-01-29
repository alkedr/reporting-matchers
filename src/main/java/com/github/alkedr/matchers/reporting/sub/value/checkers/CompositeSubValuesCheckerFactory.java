package com.github.alkedr.matchers.reporting.sub.value.checkers;

import java.util.ArrayList;
import java.util.Collection;

import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.compositeElementChecker;

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
        return compositeElementChecker(elementCheckers);
    }
}
