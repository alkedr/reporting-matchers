package com.github.alkedr.matchers.reporting.utility;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static org.apache.commons.collections4.IteratorUtils.singletonIterator;

/**
 * Обёртка для матчеров, которая позволяет сделать любой матчер ReportingMatcher'ом
 */
public class ReportingMatchersAdapter<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    private final Matcher<T> regularMatcher;

    // Если заранее неизвестно является ли матчер ReportingMatcher'ом, то лучше использовать toReportingMatcher()
    public ReportingMatchersAdapter(Matcher<T> regularMatcher) {
        this.regularMatcher = regularMatcher;
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

    @Override
    public Iterator<Object> run(Object item) {
        return singletonIterator(regularMatcher);
    }

    @Override
    public Iterator<Object> runForMissingItem() {
        return singletonIterator(regularMatcher);
    }


    // оборачивает переданный ему матчер если он не reporting.
    public static <T> ReportingMatcher<T> toReportingMatcher(Matcher<T> matcher) {
        return matcher instanceof ReportingMatcher ? (ReportingMatcher<T>) matcher : new ReportingMatchersAdapter<>(matcher);
    }

    public static <U> Iterable<ReportingMatcher<? super U>> toReportingMatchers(Iterable<? extends Matcher<? super U>> matchers) {
        // TODO: конвертировать на лету?
        Collection<ReportingMatcher<? super U>> result = new ArrayList<>();
        for (Matcher<? super U> matcher : matchers) {
            result.add(toReportingMatcher(matcher));
        }
        return result;
    }
}
