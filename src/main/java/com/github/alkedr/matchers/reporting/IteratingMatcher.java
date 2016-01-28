package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.element.checkers.ElementChecker;
import com.github.alkedr.matchers.reporting.element.checkers.ElementCheckerFactory;
import com.github.alkedr.matchers.reporting.foreach.adapters.ForeachAdapter;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

class IteratingMatcher<T> extends BaseReportingMatcher<T> {
    private final ForeachAdapter<? super T> foreachAdapter;
    private final ElementCheckerFactory elementCheckerFactory;

    IteratingMatcher(ForeachAdapter<? super T> foreachAdapter, ElementCheckerFactory elementCheckerFactory) {
        this.foreachAdapter = foreachAdapter;
        this.elementCheckerFactory = elementCheckerFactory;
    }

    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        ElementChecker elementChecker = elementCheckerFactory.create();
        elementChecker.begin(safeTreeReporter);
        // TODO: ловить исключения (хотя бы проверить instanceof T)
        foreachAdapter.run(
                (T) item,
                (key, value) -> safeTreeReporter.presentNode(key, value, elementChecker.element(key, value))
        );
        elementChecker.end(safeTreeReporter);
    }

    @Override
    public void runForMissingItem(SafeTreeReporter safeTreeReporter) {
        ElementChecker elementChecker = elementCheckerFactory.create();
        elementChecker.begin(safeTreeReporter);
        elementChecker.end(safeTreeReporter);
    }
}
