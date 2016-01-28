package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.checkers.SubValuesChecker;
import com.github.alkedr.matchers.reporting.sub.value.checkers.SubValuesCheckerFactory;
import com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractor;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

// TODO: generic-параметр для типа элемента в SubValuesExtractor
// TODO: generic-параметр для типа элемента в SubValuesCheckerFactory
class SubValuesMatcher<T> extends BaseReportingMatcher<T> {
    private final SubValuesExtractor<? super T> subValuesExtractor;
    private final SubValuesCheckerFactory subValuesCheckerFactory;

    SubValuesMatcher(SubValuesExtractor<? super T> subValuesExtractor, SubValuesCheckerFactory subValuesCheckerFactory) {
        this.subValuesExtractor = subValuesExtractor;
        this.subValuesCheckerFactory = subValuesCheckerFactory;
    }

    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        SubValuesChecker subValuesChecker = subValuesCheckerFactory.create();
        subValuesChecker.begin(safeTreeReporter);
        // TODO: проверять instanceof T
        subValuesExtractor.run((T) item, new CheckingSubValuesListener(safeTreeReporter, subValuesChecker));
        subValuesChecker.end(safeTreeReporter);
    }

    @Override
    public void runForMissingItem(SafeTreeReporter safeTreeReporter) {
        SubValuesChecker subValuesChecker = subValuesCheckerFactory.create();
        subValuesChecker.begin(safeTreeReporter);
        subValuesChecker.end(safeTreeReporter);
    }


    private static class CheckingSubValuesListener implements SubValuesExtractor.SubValuesListener {
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
        public void missing(Key key) {
            safeTreeReporter.missingNode(key, subValuesChecker.missing(key));
        }

        @Override
        public void broken(Key key, Throwable throwable) {
            safeTreeReporter.brokenNode(key, throwable, subValuesChecker.broken(key, throwable));
        }
    }
}
