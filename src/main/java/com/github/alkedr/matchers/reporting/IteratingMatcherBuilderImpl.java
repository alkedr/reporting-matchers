package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.element.checkers.ElementChecker;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.alkedr.matchers.reporting.element.checkers.ElementCheckers.containsInAnyOrder;
import static com.github.alkedr.matchers.reporting.element.checkers.ElementCheckers.containsInAnyOrderWithExtraElementsAllowed;
import static com.github.alkedr.matchers.reporting.element.checkers.ElementCheckers.containsInSpecifiedOrder;
import static com.github.alkedr.matchers.reporting.element.checkers.ElementCheckers.containsInSpecifiedOrderWithExtraElementsAllowed;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;

class IteratingMatcherBuilderImpl<T, U> extends BaseReportingMatcherBuilder<T> implements IteratingMatcherBuilder<T, U> {
    private final Function<T, Iterator<U>> itemToIteratorConverter;
    private final Collection<Matcher<U>> elementMatchers;
    private final boolean orderIsImportant;
    private final boolean extraElementsAreAllowed;

    IteratingMatcherBuilderImpl(Function<T, Iterator<U>> itemToIteratorConverter) {
        this(itemToIteratorConverter, emptyList(), true, false);
    }

    private IteratingMatcherBuilderImpl(Function<T, Iterator<U>> itemToIteratorConverter,
                                        Collection<Matcher<U>> elementMatchers, boolean orderIsImportant,
                                        boolean extraElementsAreAllowed) {
        this.itemToIteratorConverter = itemToIteratorConverter;
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

    private ReportingMatcher<T> build(Supplier<ElementChecker> elementCheckerSupplier) {
        return new ConvertingMatcher<>(itemToIteratorConverter, new IteratorMatcher<>(elementCheckerSupplier));
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
        return new IteratingMatcherBuilderImpl<>(itemToIteratorConverter, newElementMatchers, orderIsImportant, extraElementsAreAllowed);
    }

    @Override
    public IteratingMatcherBuilder<T, U> inAnyOrder() {
        return new IteratingMatcherBuilderImpl<>(itemToIteratorConverter, elementMatchers, false, extraElementsAreAllowed);
    }

    @Override
    public IteratingMatcherBuilder<T, U> inSpecifiedOrder() {
        return new IteratingMatcherBuilderImpl<>(itemToIteratorConverter, elementMatchers, true, extraElementsAreAllowed);
    }

    @Override
    public IteratingMatcherBuilder<T, U> extraElementsAreAllowed() {
        return new IteratingMatcherBuilderImpl<>(itemToIteratorConverter, elementMatchers, orderIsImportant, true);
    }

    @Override
    public IteratingMatcherBuilder<T, U> extraElementsAreNotAllowed() {
        return new IteratingMatcherBuilderImpl<>(itemToIteratorConverter, elementMatchers, orderIsImportant, false);
    }
}
