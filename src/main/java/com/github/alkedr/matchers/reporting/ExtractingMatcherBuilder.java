package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.keys.ExtractableKey;
import org.hamcrest.Matcher;

// TODO: описать зачем нужно, пример использования
// field("qwerty").displayedAs("12345").is(equalTo(1))
// все fluent API методы возвращают новый инстанс
// TODO: найти способ сделать матчеры в is() типобезопасными в случаях, когда известен их тип
public interface ExtractingMatcherBuilder<T> extends ReportingMatcher<T> {

    ExtractingMatcherBuilder<T> displayedAs(String newName);

    @Deprecated // ???
    ExtractingMatcherBuilder<T> key(ExtractableKey newExtractableKey);


    // Заменяет, а не добавляет матчеры?
    ExtractingMatcherBuilder<T> is(Object value);

    ExtractingMatcherBuilder<T> is(Matcher<?> matcher);

    <U> ExtractingMatcherBuilder<T> is(Matcher<? super U>... matchers);

    <U> ExtractingMatcherBuilder<T> is(Iterable<? extends Matcher<? super U>> matchers);

    // TODO: are, returns
    // TODO: isPresent(), isAbsent()

}
