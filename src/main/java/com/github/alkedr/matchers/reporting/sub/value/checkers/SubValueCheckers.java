package com.github.alkedr.matchers.reporting.sub.value.checkers;

import com.github.alkedr.matchers.reporting.ReportingMatcher;

import java.util.Iterator;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatcher;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;

// TODO: убрать дублирование кода
// TODO: флаг extraElementsAllowed можно реализовать с пом. бесконечной коллекции матчеров, которая возвращает noOp() или missing()
public enum SubValueCheckers {
    ;

    public static SubValuesChecker noOpSubValuesChecker() {
        return NoOpSubValuesChecker.INSTANCE;
    }


    public static SubValuesChecker compositeSubValuesChecker(SubValuesChecker... subValuesCheckers) {
        return compositeSubValuesChecker(asList(subValuesCheckers));
    }

    public static SubValuesChecker compositeSubValuesChecker(Iterable<SubValuesChecker> subValuesCheckers) {
        return new CompositeSubValuesChecker(subValuesCheckers);
    }


    public static <T> SubValuesChecker containsInSpecifiedOrder(Iterator<? extends ReportingMatcher<? super T>> elementMatchers) {
        return new ContainsInSpecifiedOrderSubValuesChecker<>(elementMatchers);
    }

    public static <T> SubValuesChecker containsInSpecifiedOrder(Iterable<? extends ReportingMatcher<? super T>> elementMatchers) {
        return containsInSpecifiedOrder(elementMatchers.iterator());
    }

    // TODO: тут нужны перегрузки для всех примитивных типов?
    @SafeVarargs
    public static <T> SubValuesChecker containsInSpecifiedOrder(T... elements) {
        return containsInSpecifiedOrder(
                stream(elements)
                        .map(element -> toReportingMatcher(equalTo(element)))
                        .collect(toList())
        );
    }


    // TODO: Iterable?
    public static <T> SubValuesChecker containsInAnyOrder(Iterable<? extends ReportingMatcher<? super T>> elementMatchers) {
        return new ContainsInAnyOrderSubValuesChecker<>(elementMatchers);
    }

    // TODO: тут нужны перегрузки для всех примитивных типов?
    @SafeVarargs
    public static <T> SubValuesChecker containsInAnyOrder(T... elements) {
        return containsInAnyOrder(
                stream(elements)
                        .map(element -> toReportingMatcher(equalTo(element)))
                        .collect(toList())
        );
    }


    // TODO: переименовать? allSubValuesMatch() ?
    public static SubValuesChecker matcherSubValuesChecker(ReportingMatcher<?> matcherForExtractedValue) {
        return new MatcherSubValuesChecker(matcherForExtractedValue);
    }


    // TODO: containsElement(x)
    // TODO: containsElement(x).atIndex(i)
    // TODO: containsElement(x).atIndex(greaterThan(i)) ?

    // TODO: everyElement(), everyArrayElement()
    // TODO: elementsThatAre(predicate/matcher).alsoAre()
    // TODO: listWithElementsInAnyOrder, listWithElementsMatchingInAnyOrder
    // TODO: возможность указать мапу ключ -> матчер?
    // TODO: возможность указать мапу (матчер для ключа + матчер для значения) -> матчер для отчёта?



    /*public static ElementChecker onePassContains(Iterator<ReportingMatcher<?>> elementMatchers) {
    }

    public static ElementChecker contains(OrderPolicy orderPolicy, ExtraElementsPolicy extraElementsPolicy,
                                          MatcherFindingPolicy matcherFindingPolicy, MatcherRemovingPolicy matcherRemovingPolicy,
                                          Collection<ReportingMatcher<?>> elementMatchers) {
        return new ElementChecker() {
            @Override
            public void begin(SafeTreeReporter safeTreeReporter) {
            }

            @Override
            public Consumer<SafeTreeReporter> element(Key key, Object value) {
                if (orderPolicy == IN_ANY_ORDER) {
                    if (matcherFindingPolicy == MatcherFindingPolicy.FIND_FIRST) {
                        // найти первый матчер, у которого .matches(value) == true
                        if (matcherRemovingPolicy == MatcherRemovingPolicy.REMOVE_MATCHER_AFTER_FINDING_FIRST_MATCHING_ELEMENT) {
                            // удалить найденный матчер
                        }
                    }
                    if (matcherFindingPolicy == MatcherFindingPolicy.FIND_ALL) {
                        // найти все матчеры, у которых .matches(value) == true и объединить их
                        if (matcherRemovingPolicy == MatcherRemovingPolicy.REMOVE_MATCHER_AFTER_FINDING_FIRST_MATCHING_ELEMENT) {
                            // удалить найденные матчеры
                        }
                    }
                    if ()
                    if (extraElementsPolicy == )
                }
                *//*if (orderPolicy == IN_ANY_ORDER) {
                    if (matcherToElementMappingPolicy == ONE_MATCHER_FOR_EVERY_ELEMENT) {
                        // просмотреть все elementMatchers пока не найдём первый подходящий
                    } else if (matcherToElementMappingPolicy == EXACTLY_ONE_MATCHER_FOR_EVERY_ELEMENT) {
                        // просмотреть все elementMatchers, найти все подходящие, убедиться что их ровно 1
                    } else {
                        // просмотреть все elementMatchers, найти все подходящие, объединить их в один матчер
                    }
                    if (extraElementsPolicy == EXTRA_ELEMENTS_ARE_ALLOWED) {
                        // если не нашли подходящий, то возвращаем noOp()
                    }
                    if (extraElementsPolicy == EXTRA_ELEMENTS_ARE_NOT_ALLOWED) {
                        // если не нашли подходящий, то возвращаем incorrectlyPresent()
                    }
                } else {
                    // брать матчеры с начала elementMatchers
                }
                return null;*//*
            }

            @Override
            public void end(SafeTreeReporter safeTreeReporter) {
                // запустить все оставшиеся матчеры
            }
        };
    }*/

    /*public enum OrderPolicy {
        IN_ANY_ORDER,
        IN_SPECIFIED_ORDER,
    }

    public enum ExtraElementsPolicy {
        EXTRA_ELEMENTS_ARE_ALLOWED,
        EXTRA_ELEMENTS_ARE_NOT_ALLOWED,
    }*/

    /*public enum MatcherFindingPolicy {
        FIND_FIRST,   // поиск матчеров прекратится после одного найденного
        FIND_ALL,     // поиск матчеров не прекратится
    }*/

    /*public enum MatcherRemovingPolicy {
        REMOVE_MATCHER_AFTER_FINDING_FIRST_MATCHING_ELEMENT,
        KEEP_MATCHER_AFTER_FINDING_FIRST_MATCHING_ELEMENT,
    }*/

    // один матчер = один элемент vs. каждый элемент должен заматчиться одним матчером vs. ошибка если один элемент матчится двумя или более матчерами ?
    /*public enum MatcherToElementMappingPolicy {
        ONE_MATCHER_ONE_ELEMENT,  // когда нашли элемент для матчера, этот матчер удаляем и переходим к следующему элементу
        MANY_MATCHERS_ONE_ELEMENT,  // когда нашли элемент для матчера, этот матчер удаляем и ищем дальше
        ONE_MATCHER_MANY_ELEMENTS,  // когда нашли элемент для матчера, этот матчер НЕ удаляем и переходим к следующему элементу



        ONE_MATCHER_FOR_EVERY_ELEMENT,
        EXACTLY_ONE_MATCHER_FOR_EVERY_ELEMENT,   // Только для IN_ANY_ORDER?
        AT_LEAST_ONE_MATCHER_FOR_EVERY_ELEMENT,   // Только для IN_ANY_ORDER?
    }*/

    /*
    ЧТо если IN_SPECIFIED_ORDER и нашли несоответствие?
     - пропустить элемент (применить этот же матчер к следующему элементу)
     - пропустить матчер (применить следующий матчер к этому элементу)
     - пропустить и элемент, и матчер (применить следующий матчер к следующему элементу)
     */
}
