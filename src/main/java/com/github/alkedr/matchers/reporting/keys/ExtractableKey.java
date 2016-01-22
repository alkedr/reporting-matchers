package com.github.alkedr.matchers.reporting.keys;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.check.results.CheckResults;
import com.github.alkedr.matchers.reporting.check.results.MergeableSubValueCheckResult;

// TODO: написать в доках интерфейсов как их реализовывать
public interface ExtractableKey extends Key {
    Result extractFrom(Object item);
    Result extractFromMissingItem();

    interface Result {
        // возвращает новый инстанс
        Result rename(String newName);
        MergeableSubValueCheckResult createCheckResult(ReportingMatcher<?> matcherForExtractedValue);

        class Present implements Result {
            private final Key key;
            private final Object value;

            public Present(Key key, Object value) {
                this.key = key;
                this.value = value;
            }

            @Override
            public Present rename(String newName) {
                return new Present(Keys.renamedKey(key, newName), value);
            }

            @Override
            public MergeableSubValueCheckResult createCheckResult(ReportingMatcher<?> matcherForExtractedValue) {
                return CheckResults.presentSubValue(key, value, matcherForExtractedValue.getChecks(value));
            }
        }

        class Missing implements Result {
            private final Key key;

            public Missing(Key key) {
                this.key = key;
            }

            @Override
            public Missing rename(String newName) {
                return new Missing(Keys.renamedKey(key, newName));
            }

            @Override
            public MergeableSubValueCheckResult createCheckResult(ReportingMatcher<?> matcherForExtractedValue) {
                return CheckResults.missingSubValue(key, matcherForExtractedValue.getChecksForMissingItem());
            }
        }

        class Broken implements Result {
            final Key key;
            final Throwable throwable;

            public Broken(Key key, Throwable throwable) {
                this.key = key;
                this.throwable = throwable;
            }

            @Override
            public Broken rename(String newName) {
                return new Broken(Keys.renamedKey(key, newName), throwable);
            }

            @Override
            public MergeableSubValueCheckResult createCheckResult(ReportingMatcher<?> matcherForExtractedValue) {
                return CheckResults.brokenSubValue(key, throwable, matcherForExtractedValue.getChecksForMissingItem());
            }
        }
    }
}
