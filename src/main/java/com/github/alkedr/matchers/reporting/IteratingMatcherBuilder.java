package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractor;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Collection;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.subValuesMatcher;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatchers;
import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.containsInAnyOrder;
import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.containsInSpecifiedOrder;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;

public class IteratingMatcherBuilder<T, U> extends BaseReportingMatcherBuilder<T> {
    private final SubValuesExtractor<? super T> subValuesExtractor;
    private final Collection<? extends Matcher<? super U>> elementMatchers;
    private final boolean orderIsImportant;

    IteratingMatcherBuilder(SubValuesExtractor<? super T> subValuesExtractor) {
        this(subValuesExtractor, emptyList(), true);
    }

    private IteratingMatcherBuilder(SubValuesExtractor<? super T> subValuesExtractor,
                                    Collection<? extends Matcher<? super U>> elementMatchers, boolean orderIsImportant) {
        this.subValuesExtractor = subValuesExtractor;
        this.elementMatchers = elementMatchers;
        this.orderIsImportant = orderIsImportant;
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

    public IteratingMatcherBuilder<T, U> withElementsMatching(Collection<? extends Matcher<? super U>> newElementMatchers) {
        return new IteratingMatcherBuilder<>(subValuesExtractor, newElementMatchers, orderIsImportant);
    }

    public IteratingMatcherBuilder<T, U> orderIsImportant(boolean newValue) {
        return new IteratingMatcherBuilder<>(subValuesExtractor, elementMatchers, newValue);
    }

    public IteratingMatcherBuilder<T, U> orderIsImportant() {
        return orderIsImportant(true);
    }

    public IteratingMatcherBuilder<T, U> orderIsNotImportant() {
        return orderIsImportant(false);
    }

/*
    // отчёт красивее
    IteratingMatcherBuilder<T, U> shouldTryToSkipNotMatchedElements();
    // быстрее, меньше памяти, запускает матчеры для элементов один раз
    IteratingMatcherBuilder<T, U> shouldNotTryToSkipNotMatchedElements();
*/


    @Override
    public ReportingMatcher<T> build() {
        return subValuesMatcher(
                subValuesExtractor,
                orderIsImportant
                        ? () -> containsInSpecifiedOrder(toReportingMatchers(elementMatchers))
                        : () -> containsInAnyOrder(toReportingMatchers(elementMatchers))
        );
    }
}
