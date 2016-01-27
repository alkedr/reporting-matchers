package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.element.checkers.ElementChecker;
import com.github.alkedr.matchers.reporting.keys.ExtractableKey;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

import java.lang.reflect.Method;
import java.util.function.Supplier;

import static com.github.alkedr.matchers.reporting.keys.Keys.methodKey;

// TODO: убрать дублирование с IteratorMatcher
class FieldsIteratingMatcher<T> extends BaseReportingMatcher<T> {
    private final Supplier<ElementChecker> elementCheckerSupplier;

    FieldsIteratingMatcher(Supplier<ElementChecker> elementCheckerSupplier) {
        this.elementCheckerSupplier = elementCheckerSupplier;
    }

    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        ElementChecker elementChecker = elementCheckerSupplier.get();
        elementChecker.begin(safeTreeReporter);
        for (Method method : item.getClass().getMethods()) {
            if (method.getParameterCount() == 0) {
                ExtractableKey key =  methodKey(method);
                try {
                    elementChecker.element(key, key.extractFrom(item));
                } catch (ExtractableKey.MissingException | ExtractableKey.BrokenException e) {
                    throw new RuntimeException(e);  // FIXME
                }
            }
        }
        elementChecker.end(safeTreeReporter);
    }

    @Override
    public void runForMissingItem(SafeTreeReporter safeTreeReporter) {
        ElementChecker elementChecker = elementCheckerSupplier.get();
        elementChecker.begin(safeTreeReporter);
        elementChecker.end(safeTreeReporter);
    }
}
