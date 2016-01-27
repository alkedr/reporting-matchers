package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.element.checkers.ElementChecker;
import com.github.alkedr.matchers.reporting.keys.ExtractableKey;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.function.Supplier;

import static com.github.alkedr.matchers.reporting.keys.Keys.fieldKey;

// TODO: убрать дублирование с IteratorMatcher
class GettersIteratingMatcher<T> extends BaseReportingMatcher<T> {
    private final Supplier<ElementChecker> elementCheckerSupplier;

    GettersIteratingMatcher(Supplier<ElementChecker> elementCheckerSupplier) {
        this.elementCheckerSupplier = elementCheckerSupplier;
    }

    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        ElementChecker elementChecker = elementCheckerSupplier.get();
        elementChecker.begin(safeTreeReporter);
        for (Field field : FieldUtils.getAllFieldsList(item.getClass())) {
            ExtractableKey key = fieldKey(field);
            try {
                elementChecker.element(key, key.extractFrom(item));
            } catch (ExtractableKey.MissingException | ExtractableKey.BrokenException e) {
                throw new RuntimeException(e);  // FIXME
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
