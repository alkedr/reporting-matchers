package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.sub.value.checkers.SubValuesCheckerFactory;
import com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractor;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Collection;

import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.containsInAnyOrder;
import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.containsInAnyOrderWithExtraElementsAllowed;
import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.containsInSpecifiedOrder;
import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.containsInSpecifiedOrderWithExtraElementsAllowed;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;

public class IteratingMatcherBuilder<T, U> extends BaseReportingMatcherBuilder<T> {
    private final SubValuesExtractor<? super T> subValuesExtractor;
    private final Collection<Matcher<U>> elementMatchers;
    private final boolean orderIsImportant;
    private final boolean extraElementsAreAllowed;

    IteratingMatcherBuilder(SubValuesExtractor<? super T> subValuesExtractor) {
        this(subValuesExtractor, emptyList(), true, false);
    }

    private IteratingMatcherBuilder(SubValuesExtractor<? super T> subValuesExtractor,
                                        Collection<Matcher<U>> elementMatchers, boolean orderIsImportant,
                                        boolean extraElementsAreAllowed) {
        this.subValuesExtractor = subValuesExtractor;
        this.elementMatchers = elementMatchers;
        this.orderIsImportant = orderIsImportant;
        this.extraElementsAreAllowed = extraElementsAreAllowed;
    }


    @SafeVarargs
    public final IteratingMatcherBuilder<T, U> withElements(U... newElements) {
        return withElements(asList(newElements));
    }

    public IteratingMatcherBuilder<T, U> withElements(Iterable<U> newElements) {
        // TODO: конвертировать на лету?
        Collection<Matcher<U>> matchers = new ArrayList<>();
        for (U element : newElements) {
            matchers.add(equalTo(element));
        }
        return withElementsMatching(matchers);
    }

    @SafeVarargs
    public final IteratingMatcherBuilder<T, U> withElementsMatching(Matcher<U>... newElementMatchers) {
        return withElementsMatching(asList(newElementMatchers));
    }

    public IteratingMatcherBuilder<T, U> withElementsMatching(Collection<Matcher<U>> newElementMatchers) {
        return new IteratingMatcherBuilder<>(subValuesExtractor, newElementMatchers, orderIsImportant, extraElementsAreAllowed);
    }

    public IteratingMatcherBuilder<T, U> inAnyOrder() {
        return new IteratingMatcherBuilder<>(subValuesExtractor, elementMatchers, false, extraElementsAreAllowed);
    }

    public IteratingMatcherBuilder<T, U> inSpecifiedOrder() {
        return new IteratingMatcherBuilder<>(subValuesExtractor, elementMatchers, true, extraElementsAreAllowed);
    }

    public IteratingMatcherBuilder<T, U> extraElementsAreAllowed() {
        return new IteratingMatcherBuilder<>(subValuesExtractor, elementMatchers, orderIsImportant, true);
    }

    public IteratingMatcherBuilder<T, U> extraElementsAreNotAllowed() {
        return new IteratingMatcherBuilder<>(subValuesExtractor, elementMatchers, orderIsImportant, false);
    }

/*
    // отчёт красивее
    IteratingMatcherBuilder<T, U> shouldTryToSkipNotMatchedElements();
    // быстрее, меньше памяти, запускает матчеры для элементов один раз
    IteratingMatcherBuilder<T, U> shouldNotTryToSkipNotMatchedElements();

    // наверное проще это сделать репортером
    IteratingMatcherBuilder<T, U> shouldDisplayUnchecked();
    IteratingMatcherBuilder<T, U> shouldNotDisplayUnchecked();
*/


    @Override
    public ReportingMatcher<T> build() {
        if (orderIsImportant) {
            if (extraElementsAreAllowed) {
                return build(() -> containsInSpecifiedOrderWithExtraElementsAllowed(elementMatchers));
            } else {
                return build(() -> containsInSpecifiedOrder(elementMatchers));
            }
        } else {
            if (extraElementsAreAllowed) {
                return build(() -> containsInAnyOrderWithExtraElementsAllowed(elementMatchers));
            } else {
                return build(() -> containsInAnyOrder(elementMatchers));
            }
        }
    }

    private ReportingMatcher<T> build(SubValuesCheckerFactory subValuesCheckerFactory) {
        return new SubValuesMatcher<>(subValuesExtractor, subValuesCheckerFactory);
    }
}
