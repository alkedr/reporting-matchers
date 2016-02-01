package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractor;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Collection;

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
    public final <T2, U2> IteratingMatcherBuilder<T2, U2> withElements(U2... newElements) {
        return withElements(asList(newElements));
    }

    public <T2, U2> IteratingMatcherBuilder<T2, U2> withElements(Iterable<U2> newElements) {
        // TODO: конвертировать на лету?
        Collection<Matcher<U2>> matchers = new ArrayList<>();
        for (U2 element : newElements) {
            matchers.add(equalTo(element));
        }
        return withElementsMatching(matchers);
    }

    @SafeVarargs
    public final <T2, U2> IteratingMatcherBuilder<T2, U2> withElementsMatching(Matcher<U2>... newElementMatchers) {
        return withElementsMatching(asList(newElementMatchers));
    }

    public <T2, U2> IteratingMatcherBuilder<T2, U2> withElementsMatching(Collection<? extends Matcher<? super U2>> newElementMatchers) {
        return new IteratingMatcherBuilder<>((SubValuesExtractor<? super T2>) subValuesExtractor, newElementMatchers, orderIsImportant);
    }

    public <T2, U2> IteratingMatcherBuilder<T2, U2> orderIsImportant(boolean newValue) {
        return new IteratingMatcherBuilder<>((SubValuesExtractor<? super T2>) subValuesExtractor, (Collection<? extends Matcher<? super U2>>) elementMatchers, newValue);
    }

    public <T2, U2> IteratingMatcherBuilder<T2, U2> orderIsImportant() {
        return orderIsImportant(true);
    }

    public <T2, U2> IteratingMatcherBuilder<T2, U2> orderIsNotImportant() {
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
        return new SubValuesMatcher<>(
                subValuesExtractor,
                orderIsImportant
                        ? () -> containsInSpecifiedOrder(toReportingMatchers(elementMatchers))
                        : () -> containsInAnyOrder(toReportingMatchers(elementMatchers))
        );
    }
}
