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

@Deprecated
class IteratingMatcherBuilderImpl<T, U> extends BaseReportingMatcherBuilder<T> implements IteratingMatcherBuilder<T, U> {
    private final ForeachAdapter<? super T> foreachAdapter;
    private final Collection<Matcher<U>> elementMatchers;
    private final boolean orderIsImportant;
    private final boolean extraElementsAreAllowed;

    IteratingMatcherBuilderImpl(ForeachAdapter<? super T> foreachAdapter) {
        this(foreachAdapter, emptyList(), true, false);
    }

    private IteratingMatcherBuilderImpl(ForeachAdapter<? super T> foreachAdapter,
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
    @Override
    public final IteratingMatcherBuilder<T, U> withElements(U... newElements) {
        return withElements(asList(newElements));
    }

    @Override
    public IteratingMatcherBuilder<T, U> withElements(Iterable<U> newElements) {
        // TODO: конвертировать на лету?
        Collection<Matcher<U>> matchers = new ArrayList<>();
        for (U element : newElements) {
            matchers.add(equalTo(element));
        }
        return withElementsMatching(matchers);
    }

    @SafeVarargs
    @Override
    public final IteratingMatcherBuilder<T, U> withElementsMatching(Matcher<U>... newElementMatchers) {
        return withElementsMatching(asList(newElementMatchers));
    }

    @Override
    public IteratingMatcherBuilder<T, U> withElementsMatching(Collection<Matcher<U>> newElementMatchers) {
        return new IteratingMatcherBuilderImpl<>(foreachAdapter, newElementMatchers, orderIsImportant, extraElementsAreAllowed);
    }

    @Override
    public IteratingMatcherBuilder<T, U> inAnyOrder() {
        return new IteratingMatcherBuilderImpl<>(foreachAdapter, elementMatchers, false, extraElementsAreAllowed);
    }

    @Override
    public IteratingMatcherBuilder<T, U> inSpecifiedOrder() {
        return new IteratingMatcherBuilderImpl<>(foreachAdapter, elementMatchers, true, extraElementsAreAllowed);
    }

    @Override
    public IteratingMatcherBuilder<T, U> extraElementsAreAllowed() {
        return new IteratingMatcherBuilderImpl<>(foreachAdapter, elementMatchers, orderIsImportant, true);
    }

    @Override
    public IteratingMatcherBuilder<T, U> extraElementsAreNotAllowed() {
        return new IteratingMatcherBuilderImpl<>(foreachAdapter, elementMatchers, orderIsImportant, false);
    }
}
