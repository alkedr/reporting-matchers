package com.github.alkedr.matchers.reporting;

import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Collection;

import static com.github.alkedr.matchers.reporting.NoOpMatcher.noOp;
import static com.github.alkedr.matchers.reporting.ReportingMatcherAdapter.toReportingMatcher;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;

public class ExtractingMatcherBuilder<T> extends ExtractingMatcher<T> {
    public ExtractingMatcherBuilder(String name, Extractor extractor, ReportingMatcher<?> matcher) {
        super(name, extractor, matcher);
    }


    public ExtractingMatcherBuilder<T> displayedAs(String name) {
        return new ExtractingMatcherBuilder<>(name, getExtractor(), getMatcher());
    }

    // этот метод обычно не нужен
    public ExtractingMatcherBuilder<T> extractor(Extractor extractor) {
        return new ExtractingMatcherBuilder<>(getName(), extractor, getMatcher());
    }

    public ExtractingMatcherBuilder<T> is(Object value) {
        return is(equalTo(value));
    }

    // Заменяет, а не добавляет матчеры?
    public ExtractingMatcherBuilder<T> is(Matcher<?> matcher) {
        return is(toReportingMatcher(matcher));
    }

    public ExtractingMatcherBuilder<T> is(ReportingMatcher<?> matcher) {
        return new ExtractingMatcherBuilder<>(getName(), getExtractor(), matcher);
    }

    @SafeVarargs
    public final <U> ExtractingMatcherBuilder<T> is(Matcher<? super U>... matchers) {
        return is(new SequenceMatcher<>(convertIterableOfMatchersToIterableOfReportingMatchers(asList(matchers))));
    }

    // TODO: сделать кучу перегрузок для is, в т. ч. для сравнения с конкретными значениями

    public static <T> ExtractingMatcherBuilder<T> extractedValue(String name, Extractor extractor) {
        return new ExtractingMatcherBuilder<>(name, extractor, noOp());
    }


    private static <U> Iterable<ReportingMatcher<? super U>> convertIterableOfMatchersToIterableOfReportingMatchers(
            Iterable<Matcher<? super U>> matchers) {
        Collection<ReportingMatcher<? super U>> result = new ArrayList<>();
        for (Matcher<? super U> matcher : matchers) {
            result.add(toReportingMatcher(matcher));
        }
        return result;
    }
}
