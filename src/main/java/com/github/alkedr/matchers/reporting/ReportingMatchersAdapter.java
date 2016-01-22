package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.check.results.CheckResult;
import org.apache.commons.collections4.iterators.SingletonIterator;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.Iterator;

import static com.github.alkedr.matchers.reporting.check.results.CheckResults.brokenMatcher;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.failedMatcher;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.matcherForMissingItem;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.passedMatcher;

/**
 * Обёртка для матчеров, которая позволяет сделать любой матчер ReportingMatcher'ом
 */
class ReportingMatchersAdapter<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    private final Matcher<T> regularMatcher;

    // Если заранее неизвестно является ли матчер ReportingMatcher'ом, то лучше использовать toReportingMatcher()
    ReportingMatchersAdapter(Matcher<T> regularMatcher) {
        this.regularMatcher = regularMatcher;
    }

    @Override
    public Iterator<CheckResult> getChecks(Object item) {
        boolean matches;
        try {
            matches = regularMatcher.matches(item);
        } catch (RuntimeException e) {
            return new SingletonIterator<>(brokenMatcher(StringDescription.toString(regularMatcher), e));
        }
        if (matches) {
            // TODO: подсовывать свой Description, который отлавливает equalTo и is
            return new SingletonIterator<>(passedMatcher(StringDescription.toString(regularMatcher)));
        }
        Description mismatchDescription = new StringDescription();
        regularMatcher.describeMismatch(item, mismatchDescription);
        return new SingletonIterator<>(failedMatcher(StringDescription.toString(regularMatcher), mismatchDescription.toString()));
    }

    @Override
    public Iterator<CheckResult> getChecksForMissingItem() {
        return new SingletonIterator<>(matcherForMissingItem(StringDescription.asString(regularMatcher)));
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
