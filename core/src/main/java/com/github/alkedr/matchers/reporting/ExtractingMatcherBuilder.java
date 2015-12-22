package com.github.alkedr.matchers.reporting;

import org.hamcrest.Matcher;

import static com.github.alkedr.matchers.reporting.NoOpMatcher.noOp;
import static com.github.alkedr.matchers.reporting.ReportingMatcherAdapter.toReportingMatcher;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;

// TODO: описать зачем нужно, пример использования
// field("qwerty").displayedAs("12345").is(equalTo(1))
// is заменяет, не добавляет
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
        return is(asList(matchers));
    }

    public final <U> ExtractingMatcherBuilder<T> is(Iterable<Matcher<? super U>> matchers) {
        return is(new SequenceMatcher<>(ReportingMatcherAdapter.toReportingMatchers(matchers)));
    }


    public static <T> ExtractingMatcherBuilder<T> extractedValue(String name, Extractor extractor) {
        return new ExtractingMatcherBuilder<>(name, extractor, noOp());
    }
}
