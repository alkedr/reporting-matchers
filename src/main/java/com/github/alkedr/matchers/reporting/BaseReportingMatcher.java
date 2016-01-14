package com.github.alkedr.matchers.reporting;

import org.hamcrest.BaseMatcher;

public abstract class BaseReportingMatcher<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    @Override
    public boolean matches(Object item) {
        return false;
//        CheckListener.Checks checks = getChecks(item);
//        return runMatchers(item, checks.getMatcherIterator()) && runKeyValueChecks(checks.getKeyValueChecksIterator());
    }

    /*private static boolean runMatchers(Object item, Iterator<? extends Matcher<?>> matcherIterator) {
        while (matcherIterator.hasNext()) {
            if (!matcherIterator.next().matches(item)) {
                return false;
            }
        }
        return true;
    }

    private static boolean runKeyValueChecks(Iterator<KeyValueChecks> keyValueChecksIterator) {
        while (keyValueChecksIterator.hasNext()) {
            KeyValueChecks keyValueChecks = keyValueChecksIterator.next();
            if (keyValueChecks.getValue().getExtractionThrowable() != null
                    || keyValueChecks.getValue().getPresenceStatus() != keyValueChecks.getChecks().getExpectedPresenceStatus()
                    || !runMatchers(keyValueChecks.getValue().get(), keyValueChecks.getChecks().getMatcherIterator())) {
                return false;
            }
        }
        return true;
    }*/
}
