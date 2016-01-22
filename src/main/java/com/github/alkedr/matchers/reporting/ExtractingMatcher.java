package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.check.results.CheckResult;
import com.github.alkedr.matchers.reporting.extractors.Extractor;
import org.apache.commons.collections4.iterators.SingletonIterator;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Iterator;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.merge;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.noOp;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatcher;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatchers;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;

// TODO: описать зачем нужно, пример использования
// field("qwerty").displayedAs("12345").is(equalTo(1))
// is заменяет, не добавляет
// все fluent API методы возвращают новый инстанс
// TODO: найти способ сделать матчеры в is() типобезопасными в случаях, когда известен их тип
class ExtractingMatcher<T> extends BaseReportingMatcher<T> implements ExtractingMatcherBuilder<T> {
    private final String name;
    private final Extractor extractor;
    private final ReportingMatcher<?> matcher;

    ExtractingMatcher(Extractor extractor) {
        this(null, extractor, noOp());
    }

    // name и checks могут быть null
    ExtractingMatcher(String name, Extractor extractor, ReportingMatcher<?> matcher) {
        this.name = name;
        this.extractor = extractor;
        this.matcher = matcher;
    }


    @Override
    public Iterator<CheckResult> getChecks(Object item) {
        return new SingletonIterator<>(extractor.extractFrom(item).createCheckResult(matcher));
    }

    @Override
    public Iterator<CheckResult> getChecksForMissingItem() {
        return new SingletonIterator<>(extractor.extractFromMissingItem().createCheckResult(matcher));
    }


    @Override
    public void describeTo(Description description) {
//        description.appendText(name);
        // TODO: append matcher.describeTo()
    }


    @Override
    public ExtractingMatcher<T> displayedAs(String newName) {
        return new ExtractingMatcher<>(newName, extractor, matcher);
    }

    @Override
    public ExtractingMatcher<T> extractor(Extractor newExtractor) {
        return new ExtractingMatcher<>(name, newExtractor, matcher);
    }


    // Заменяет, а не добавляет матчеры?
    @Override
    public ExtractingMatcher<T> is(Object value) {
        return is(equalTo(value));
    }

    @Override
    public ExtractingMatcher<T> is(Matcher<?> matcher) {
        return new ExtractingMatcher<>(name, extractor, toReportingMatcher(matcher));
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

    // TODO: are, returns
    // TODO: isPresent(), isAbsent()


}
