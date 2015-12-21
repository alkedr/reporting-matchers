package com.github.alkedr.matchers.reporting;

import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Collection;

import static com.github.alkedr.matchers.reporting.NoOpMatcher.noOp;
import static com.github.alkedr.matchers.reporting.ReportingMatcherAdapter.toReportingMatcher;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;

public class ExtractingMatcherBuilder<T, U> extends ExtractingMatcher<T, U> {
    public ExtractingMatcherBuilder(String name, Extractor<U> extractor, ReportingMatcher<U> matcher) {
        super(name, extractor, matcher);
    }


    public ExtractingMatcherBuilder<T, U> displayedAs(String name) {
        return new ExtractingMatcherBuilder<>(name, getExtractor(), getMatcher());
    }

    // этот метод обычно не нужен
    public ExtractingMatcherBuilder<T, U> extractor(Extractor<U> extractor) {
        return new ExtractingMatcherBuilder<>(getName(), extractor, getMatcher());
    }

    public ExtractingMatcherBuilder<T, U> is(U value) {
        return is(equalTo(value));
    }

    // Заменяет, а не добавляет матчеры?
    public ExtractingMatcherBuilder<T, U> is(Matcher<U> matcher) {
        return is(toReportingMatcher(matcher));
    }

    public ExtractingMatcherBuilder<T, U> is(ReportingMatcher<U> matcher) {
        return new ExtractingMatcherBuilder<>(getName(), getExtractor(), matcher);
    }

    @SafeVarargs
    public final ExtractingMatcherBuilder<T, U> is(Matcher<? extends U>... matchers) {
        return is(new SequenceMatcher<>(convertIterableOfMatchersToIterableOfReportingMatchers(asList(matchers))));
    }

    // TODO: сделать кучу перегрузок для is, в т. ч. для сравнения с конкретными значениями


    public static <T, U> ExtractingMatcherBuilder<T, U> extractedValue(String name, Extractor<U> extractor) {
        return new ExtractingMatcherBuilder<>(name, extractor, noOp());
    }


    private static <U> Iterable<ReportingMatcher<? extends U>> convertIterableOfMatchersToIterableOfReportingMatchers(
            Iterable<Matcher<? extends U>> matchers) {
        Collection<ReportingMatcher<? extends U>> result = new ArrayList<>();
        for (Matcher<? extends U> matcher : matchers) {
            result.add(toReportingMatcher(matcher));
        }
        return result;
    }
}
