package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.check.results.CheckResult;
import com.github.alkedr.matchers.reporting.keys.ExtractableKey;
import org.apache.commons.collections4.iterators.SingletonIterator;
import org.hamcrest.Matcher;

import java.util.Iterator;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.merge;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.noOp;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatcher;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatchers;
import static com.github.alkedr.matchers.reporting.keys.Keys.renamedKey;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;

class ExtractingMatcher<T> extends BaseReportingMatcher<T> implements ExtractingMatcherBuilder<T> {
    private final ExtractableKey extractableKey;
    private final ReportingMatcher<?> matcher;

    ExtractingMatcher(ExtractableKey extractableKey) {
        this(extractableKey, noOp());
    }

    ExtractingMatcher(ExtractableKey extractableKey, ReportingMatcher<?> matcher) {
        this.extractableKey = extractableKey;
        this.matcher = matcher;
    }


    @Override
    public Iterator<CheckResult> getChecks(Object item) {
        return new SingletonIterator<>(extractableKey.extractFrom(item).createCheckResult(matcher));
    }

    @Override
    public Iterator<CheckResult> getChecksForMissingItem() {
        return new SingletonIterator<>(extractableKey.extractFromMissingItem().createCheckResult(matcher));
    }


    @Override
    public ExtractingMatcher<T> displayedAs(String newName) {
        return new ExtractingMatcher<>(renamedKey(extractableKey, newName), matcher);
    }

    @Override
    public ExtractingMatcher<T> key(ExtractableKey newExtractableKey) {
        return new ExtractingMatcher<>(newExtractableKey, matcher);
    }


    @Override
    public ExtractingMatcher<T> is(Object value) {
        return is(equalTo(value));
    }

    @Override
    public ExtractingMatcher<T> is(Matcher<?> matcher) {
        return new ExtractingMatcher<>(extractableKey, toReportingMatcher(matcher));
    }

    @Override
    @SafeVarargs
    public final <U> ExtractingMatcher<T> is(Matcher<? super U>... matchers) {
        return is(asList(matchers));
    }

    @Override
    public <U> ExtractingMatcher<T> is(Iterable<? extends Matcher<? super U>> matchers) {
        return is(merge(toReportingMatchers(matchers)));
    }
}
