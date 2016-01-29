package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.sub.value.keys.ExtractableKey;
import org.hamcrest.Matcher;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.merge;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.noOp;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatcher;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatchers;
import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.matcherSubValuesChecker;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.renamedExtractableKey;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;

// TODO: описать зачем нужно, пример использования
// field("qwerty").displayedAs("12345").is(equalTo(1))
// все fluent API методы возвращают новый инстанс
// TODO: найти способ сделать матчеры в is() типобезопасными в случаях, когда известен их тип
public class ExtractingMatcherBuilder<T> extends BaseReportingMatcherBuilder<T> {
    private final ExtractableKey extractor;
    private final String name;
    private final ReportingMatcher<?> matcherForExtractedValue;

    ExtractingMatcherBuilder(ExtractableKey extractor) {
        this(extractor, null, noOp());
    }

    private ExtractingMatcherBuilder(ExtractableKey extractor, String name, ReportingMatcher<?> matcherForExtractedValue) {
        this.extractor = extractor;
        this.name = name;
        this.matcherForExtractedValue = matcherForExtractedValue;
    }


    public ExtractingMatcherBuilder<T> displayedAs(String newName) {
        return new ExtractingMatcherBuilder<>(extractor, newName, matcherForExtractedValue);
    }

    public ExtractingMatcherBuilder<T> is(Object value) {
        return is(equalTo(value));
    }

    public ExtractingMatcherBuilder<T> is(Matcher<?> matcher) {
        return new ExtractingMatcherBuilder<>(extractor, name, toReportingMatcher(matcher));
    }

    @SafeVarargs
    public final <U> ExtractingMatcherBuilder<T> is(Matcher<? super U>... matchers) {
        return is(asList(matchers));
    }

    public <U> ExtractingMatcherBuilder<T> is(Iterable<? extends Matcher<? super U>> matchers) {
        return is(merge(toReportingMatchers(matchers)));
    }

    // TODO: returns ?


    @Override
    public ReportingMatcher<T> build() {
        return new SubValuesMatcher<>(
                name == null ? extractor : renamedExtractableKey(extractor, name),
                () -> matcherSubValuesChecker(matcherForExtractedValue)
        );
    }
}
