package com.github.alkedr.matchers.reporting;

import org.hamcrest.Matcher;

// TODO: описать зачем нужно, пример использования
// field("qwerty").displayedAs("12345").is(equalTo(1))
// is заменяет, не добавляет
// все методы возвращают новый инстанс
// TODO: написать в доках интерфейсов как их реализовывать
public interface ExtractingMatcherBuilder<T> extends ReportingMatcher<T> {
    ExtractingMatcherBuilder<T> displayedAs(String name);
    // Заменяет, а не добавляет матчеры?
    ExtractingMatcherBuilder<T> is(Object value);
    ExtractingMatcherBuilder<T> is(Matcher<?> matcher);
    <U> ExtractingMatcherBuilder<T> is(Matcher<? super U>... matchers);
    <U> ExtractingMatcherBuilder<T> is(Iterable<? extends Matcher<? super U>> matchers);

    // TODO: are, returns
}
