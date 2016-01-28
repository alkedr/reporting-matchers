package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

import java.util.function.Function;

@Deprecated
class ConvertingMatcher<T, U> extends BaseReportingMatcher<T> {
    private final Function<T, U> converter;
    private final ReportingMatcher<U> matcher;

    ConvertingMatcher(Function<T, U> converter, ReportingMatcher<U> matcher) {
        this.converter = converter;
        this.matcher = matcher;
    }

    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        U convertedItem;
        try {
            convertedItem = converter.apply((T) item);
        } catch (Exception e) {
            safeTreeReporter.brokenCheck("Ошибка при преобразовании", e);
            runForMissingItem(safeTreeReporter);
            return;
        }
        matcher.run(convertedItem, safeTreeReporter);
    }

    @Override
    public void runForMissingItem(SafeTreeReporter safeTreeReporter) {
        matcher.runForMissingItem(safeTreeReporter);
    }
}
