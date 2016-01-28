package com.github.alkedr.matchers.reporting.element.checkers;

import java.util.ArrayList;
import java.util.Collection;

import static com.github.alkedr.matchers.reporting.element.checkers.ElementCheckers.compositeElementChecker;

public class CompositeElementCheckerFactory implements ElementCheckerFactory {
    private final Iterable<ElementCheckerFactory> elementCheckerFactories;

    public CompositeElementCheckerFactory(Iterable<ElementCheckerFactory> elementCheckerFactories) {
        this.elementCheckerFactories = elementCheckerFactories;
    }

    @Override
    public ElementChecker create() {
        Collection<ElementChecker> elementCheckers = new ArrayList<>();
        for (ElementCheckerFactory factory : elementCheckerFactories) {
            elementCheckers.add(factory.create());
        }
        return compositeElementChecker(elementCheckers);
    }
}
