package com.github.alkedr.matchers.reporting.sub.value.checkers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.compositeSubValuesChecker;

class CompositeSubValuesCheckerSupplier implements Supplier<SubValuesChecker> {
    private final Iterable<Supplier<SubValuesChecker>> subValuesCheckerSuppliers;

    CompositeSubValuesCheckerSupplier(Iterable<Supplier<SubValuesChecker>> subValuesCheckerSuppliers) {
        this.subValuesCheckerSuppliers = subValuesCheckerSuppliers;
    }

    @Override
    public SubValuesChecker get() {
        Collection<SubValuesChecker> subValuesCheckers = new ArrayList<>();
        for (Supplier<SubValuesChecker> factory : subValuesCheckerSuppliers) {
            subValuesCheckers.add(factory.get());
        }
        return compositeSubValuesChecker(subValuesCheckers);
    }
}
