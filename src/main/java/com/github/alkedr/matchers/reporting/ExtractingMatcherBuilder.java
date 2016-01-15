package com.github.alkedr.matchers.reporting;

import org.hamcrest.Matcher;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;

// TODO: описать зачем нужно, пример использования
// field("qwerty").displayedAs("12345").is(equalTo(1))
// is заменяет, не добавляет
public class ExtractingMatcherBuilder<T> extends ExtractingMatcher<T> {
    public ExtractingMatcherBuilder(Extractor extractor, ReportingMatcher.Checks checks) {
        super(extractor, checks);
    }

    public ExtractingMatcherBuilder<T> displayedAs(String name) {
        return this;
//        return new ExtractingMatcherBuilder<>(name, getExtractor(), getMatcher());
    }

    // этот метод обычно не нужен
    public ExtractingMatcherBuilder<T> extractor(Extractor extractor) {
        return new ExtractingMatcherBuilder<>(extractor, getChecks());
    }

    public ExtractingMatcherBuilder<T> is(Object value) {
        return is(equalTo(value));
    }

    // Заменяет, а не добавляет матчеры?
    public ExtractingMatcherBuilder<T> is(Matcher<?> matcher) {
        return new ExtractingMatcherBuilder<>(getExtractor(), new ReportingMatcher.Checks(ReportingMatcher.PresenceStatus.PRESENT, ReportingMatchersAdapter.toReportingMatcher(matcher)));
    }

    @SafeVarargs
    public final <U> ExtractingMatcherBuilder<T> is(Matcher<? super U>... matchers) {
        return is(asList(matchers));
    }

    public final <U> ExtractingMatcherBuilder<T> is(Iterable<? extends Matcher<? super U>> matchers) {
        return is(new MergingMatcher<>(ReportingMatchers.sequence(ReportingMatchersAdapter.toReportingMatchers(matchers))));
    }


    // TODO: are, returns


    public static <T> ExtractingMatcherBuilder<T> extractedValue(Extractor extractor) {
        return new ExtractingMatcherBuilder<>(extractor, new ReportingMatcher.Checks(ReportingMatcher.PresenceStatus.PRESENT, NoOpMatcher.noOp()));
    }
}
