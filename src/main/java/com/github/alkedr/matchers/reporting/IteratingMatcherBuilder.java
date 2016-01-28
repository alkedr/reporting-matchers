package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.element.checkers.ElementCheckerFactory;
import com.github.alkedr.matchers.reporting.foreach.adapters.ForeachAdapter;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Collection;

import static com.github.alkedr.matchers.reporting.element.checkers.ElementCheckers.containsInAnyOrder;
import static com.github.alkedr.matchers.reporting.element.checkers.ElementCheckers.containsInAnyOrderWithExtraElementsAllowed;
import static com.github.alkedr.matchers.reporting.element.checkers.ElementCheckers.containsInSpecifiedOrder;
import static com.github.alkedr.matchers.reporting.element.checkers.ElementCheckers.containsInSpecifiedOrderWithExtraElementsAllowed;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;

public class IteratingMatcherBuilder<T, U> extends BaseReportingMatcherBuilder<T> {
    private final ForeachAdapter<? super T> foreachAdapter;
    private final Collection<Matcher<U>> elementMatchers;
    private final boolean orderIsImportant;
    private final boolean extraElementsAreAllowed;

    IteratingMatcherBuilder(ForeachAdapter<? super T> foreachAdapter) {
        this(foreachAdapter, emptyList(), true, false);
    }

    private IteratingMatcherBuilder(ForeachAdapter<? super T> foreachAdapter,
                                        Collection<Matcher<U>> elementMatchers, boolean orderIsImportant,
                                        boolean extraElementsAreAllowed) {
        this.foreachAdapter = foreachAdapter;
        this.elementMatchers = elementMatchers;
        this.orderIsImportant = orderIsImportant;
        this.extraElementsAreAllowed = extraElementsAreAllowed;
    }

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

    private ReportingMatcher<T> build(ElementCheckerFactory elementCheckerSupplier) {
        return new IteratingMatcher<>(foreachAdapter, elementCheckerSupplier);
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
        return new IteratingMatcherBuilder<>(foreachAdapter, newElementMatchers, orderIsImportant, extraElementsAreAllowed);
    }

    public IteratingMatcherBuilder<T, U> inAnyOrder() {
        return new IteratingMatcherBuilder<>(foreachAdapter, elementMatchers, false, extraElementsAreAllowed);
    }

    public IteratingMatcherBuilder<T, U> inSpecifiedOrder() {
        return new IteratingMatcherBuilder<>(foreachAdapter, elementMatchers, true, extraElementsAreAllowed);
    }

    public IteratingMatcherBuilder<T, U> extraElementsAreAllowed() {
        return new IteratingMatcherBuilder<>(foreachAdapter, elementMatchers, orderIsImportant, true);
    }

    public IteratingMatcherBuilder<T, U> extraElementsAreNotAllowed() {
        return new IteratingMatcherBuilder<>(foreachAdapter, elementMatchers, orderIsImportant, false);
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
}
