package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.keys.ExtractableKey;
import org.hamcrest.Matcher;

public interface ExtractingMatcherBuilder<T> extends ReportingMatcher<T> {

    ExtractingMatcherBuilder<T> displayedAs(String newName);

    ExtractingMatcherBuilder<T> key(ExtractableKey newExtractableKey);


    // Заменяет, а не добавляет матчеры?
    ExtractingMatcherBuilder<T> is(Object value);

    ExtractingMatcherBuilder<T> is(Matcher<?> matcher);

    <U> ExtractingMatcherBuilder<T> is(Matcher<? super U>... matchers);

    <U> ExtractingMatcherBuilder<T> is(Iterable<? extends Matcher<? super U>> matchers);

    // TODO: are, returns
    // TODO: isPresent(), isAbsent()

}
