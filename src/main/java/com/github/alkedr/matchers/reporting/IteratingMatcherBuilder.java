package com.github.alkedr.matchers.reporting;

import org.hamcrest.Matcher;

import java.util.Collection;

public interface IteratingMatcherBuilder<T, U> extends ReportingMatcherBuilder<T> {
    IteratingMatcherBuilder<T, U> withElements(U... newElements);
    IteratingMatcherBuilder<T, U> withElements(Iterable<U> newElements);
    IteratingMatcherBuilder<T, U> withElementsMatching(Matcher<U>... newElementMatchers);
    IteratingMatcherBuilder<T, U> withElementsMatching(Collection<Matcher<U>> newElementMatchers);

    IteratingMatcherBuilder<T, U> inAnyOrder();
    IteratingMatcherBuilder<T, U> inSpecifiedOrder();

    IteratingMatcherBuilder<T, U> extraElementsAreAllowed();
    IteratingMatcherBuilder<T, U> extraElementsAreNotAllowed();

/*
    // отчёт красивее
    IteratingMatcherBuilder<T, U> shouldTryToSkipNotMatchedElements();
    // быстрее, меньше памяти, запускает матчеры для элементов один раз
    IteratingMatcherBuilder<T, U> shouldNotTryToSkipNotMatchedElements();

    // наверное проще это сделать репортером
    IteratingMatcherBuilder<T, U> shouldDisplayUnchecked();
    IteratingMatcherBuilder<T, U> shouldNotDisplayUnchecked();
*/
}
