package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

/**
 * Обёртка для матчеров, которая позволяет сделать любой матчер ReportingMatcher'ом
 */
class ReportingMatcherAdapter<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    private final Matcher<T> regularMatcher;

    ReportingMatcherAdapter(Matcher<T> regularMatcher) {
        this.regularMatcher = regularMatcher;
    }

    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        boolean matches;
        try {
            matches = regularMatcher.matches(item);
        } catch (RuntimeException e) {
            safeTreeReporter.brokenCheck(StringDescription.toString(regularMatcher), e);
            return;
        }
        if (matches) {
            // TODO: отлавливать equalTo и is
            safeTreeReporter.passedCheck(StringDescription.toString(regularMatcher));
        } else {
            Description mismatchDescription = new StringDescription();
            regularMatcher.describeMismatch(item, mismatchDescription);
            safeTreeReporter.failedCheck(StringDescription.toString(regularMatcher), mismatchDescription.toString());
        }
    }

    @Override
    public void runForMissingItem(SafeTreeReporter safeTreeReporter) {
        safeTreeReporter.checkForMissingItem(StringDescription.asString(regularMatcher));
    }

    @Override
    public boolean matches(Object item) {
        return regularMatcher.matches(item);
    }

    @Override
    public void describeTo(Description description) {
        regularMatcher.describeTo(description);
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        regularMatcher.describeMismatch(item, description);
    }
}
