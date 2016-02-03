package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.checkers.SubValuesChecker;
import com.github.alkedr.matchers.reporting.sub.value.checkers.SubValuesCheckerFactory;
import com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractor;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

// TODO: generic-параметр для типа элемента в SubValuesExtractor
// TODO: generic-параметр для типа элемента в SubValuesCheckerFactory
class SubValuesMatcher<T, S> extends BaseReportingMatcher<T> {
    private final SubValuesExtractor<T, S> subValuesExtractor;
    private final SubValuesCheckerFactory<S> subValuesCheckerFactory;

    SubValuesMatcher(SubValuesExtractor<T, S> subValuesExtractor, SubValuesCheckerFactory<S> subValuesCheckerFactory) {
        this.subValuesExtractor = subValuesExtractor;
        this.subValuesCheckerFactory = subValuesCheckerFactory;
    }

    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        SubValuesChecker subValuesChecker = subValuesCheckerFactory.createSubValuesChecker();
        subValuesChecker.begin(safeTreeReporter);
        // TODO: проверять instanceof T (ловить ClassCastException?)
        subValuesExtractor.run((T) item, new CheckingSubValuesListener<>(safeTreeReporter, subValuesChecker));
        subValuesChecker.end(safeTreeReporter);
    }

    @Override
    public void runForAbsentItem(SafeTreeReporter safeTreeReporter) {
        SubValuesChecker subValuesChecker = subValuesCheckerFactory.createSubValuesChecker();
        subValuesChecker.begin(safeTreeReporter);
        subValuesChecker.end(safeTreeReporter);
    }


    private static class CheckingSubValuesListener<S> implements SubValuesExtractor.SubValuesListener<S> {
        private final SafeTreeReporter safeTreeReporter;
        private final SubValuesChecker subValuesChecker;

        CheckingSubValuesListener(SafeTreeReporter safeTreeReporter, SubValuesChecker subValuesChecker) {
            this.safeTreeReporter = safeTreeReporter;
            this.subValuesChecker = subValuesChecker;
        }

        @Override
        public void present(Key key, Object value) {
            safeTreeReporter.presentNode(key, value, subValuesChecker.present(key, value));
        }

        @Override
        public void absent(Key key) {
            safeTreeReporter.absentNode(key, subValuesChecker.absent(key));
        }

        @Override
        public void broken(Key key, Throwable throwable) {
            safeTreeReporter.brokenNode(key, throwable, subValuesChecker.broken(key, throwable));
        }
    }
}
