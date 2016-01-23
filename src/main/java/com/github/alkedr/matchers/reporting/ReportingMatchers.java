package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.keys.*;
import com.google.common.collect.Iterators;
import org.hamcrest.Matcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.github.alkedr.matchers.reporting.element.checkers.IteratorMatcherElementCheckers.containsInSpecifiedOrderChecker;
import static com.github.alkedr.matchers.reporting.extractors.Extractors.*;
import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;
import static java.util.Arrays.asList;

// используй static import
// TODO: использовать здесь только интерфейсы из корневого пакета?
// TODO: сделать реализации матчеров package-private?
public class ReportingMatchers {

    @SuppressWarnings("unchecked")
    public static <T> ReportingMatcher<T> noOp() {
        return (ReportingMatcher<T>) NoOpMatcher.INSTANCE;
    }



    // оборачивает переданный ему матчер если он не reporting.
    public static <T> ReportingMatcher<T> toReportingMatcher(Matcher<T> matcher) {
        return matcher instanceof ReportingMatcher ? (ReportingMatcher<T>) matcher : new ReportingMatchersAdapter<>(matcher);
    }

    public static <U> Iterable<ReportingMatcher<? super U>> toReportingMatchers(Iterable<? extends Matcher<? super U>> matchers) {
        // TODO: конвертировать на лету?
        Collection<ReportingMatcher<? super U>> result = new ArrayList<>();
        for (Matcher<? super U> matcher : matchers) {
            result.add(toReportingMatcher(matcher));
        }
        return result;
    }



    @SafeVarargs
    public static <T> ReportingMatcher<T> sequence(ReportingMatcher<? super T>... matchers) {
        return sequence(asList(matchers));
    }

    public static <T> ReportingMatcher<T> sequence(Iterable<? extends ReportingMatcher<? super T>> matchers) {
        return new SequenceMatcher<>(matchers);
    }


    @SafeVarargs
    public static <T> ReportingMatcher<T> merge(ReportingMatcher<? super T>... matchers) {
        return merge(asList(matchers));
    }

    public static <T> ReportingMatcher<T> merge(Iterable<? extends ReportingMatcher<? super T>> matchers) {
        return new MergingMatcher<>(sequence(matchers));
    }


    // TODO: noOp() ?
    // TODO: merge() ?
    // TODO: что-то очень универсальное, принимающее лямбду? value("name", <lambda>)  mergeableValue("name", <lambda>)
    // TODO: everyElement(), everyArrayElement()
    // TODO: elementsThatAre(predicate/matcher).alsoAre()
    // TODO: method(lambda), getter(lambda)

    // TODO: present(), missing()


    // пробивает доступ к private, protected и package-private полям
    // если проверяемый объект имеет неправильный класс, то бросает исключение в matches() ?
    public static <T> ExtractingMatcherBuilder<T> field(Field field) {
        return new ExtractingMatcher<>(fieldExtractor(new FieldKey(field)));
    }

    // пробивает доступ к private, protected и package-private полям
    // если поле не найдено, то бросает исключение в matches() ?
    public static <T> ExtractingMatcherBuilder<T> field(String fieldName) {
        return new ExtractingMatcher<>(fieldByNameExtractor(new FieldByNameKey(fieldName)));
    }


    // НЕ пробивает доступ к private, protected и package-private полям TODO: пофиксить это?
    // если проверяемый объект имеет неправильный класс, то бросает исключение в matches()
    public static <T> ExtractingMatcherBuilder<T> method(Method method, Object... arguments) {
        return new ExtractingMatcher<>(methodExtractor(new MethodKey(method, arguments)));
    }

    // НЕ пробивает доступ к private, protected и package-private полям TODO: пофиксить это?
    // если метод не найден, то бросает исключение в matches()
    public static <T> ExtractingMatcherBuilder<T> method(String methodName, Object... arguments) {
        return new ExtractingMatcher<>(methodByNameExtractor(new MethodByNameKey(methodName, arguments)));
    }

//    public static <T> ExtractingMatcher<T> method(Function<T, ?> function) {
        // Получить класс из параметра function'а?
        // Mockito вроде как не требует конструктора без параметров, значит это возможно
//        return new SimpleMethodByLambdaExtractingMatcher<>(function);
//    }


    // как method(), только убирает 'get' и 'is'
    public static <T> ExtractingMatcherBuilder<T> getter(Method method) {
        return new ExtractingMatcher<T>(methodExtractor(new MethodKey(method)), createNameForGetterMethodInvocation(method.getName()));
    }

    // как method(), только убирает 'get' и 'is'
    public static <T> ExtractingMatcherBuilder<T> getter(String methodName) {
        return new ExtractingMatcher<T>(methodByNameExtractor(new MethodByNameKey(methodName)), createNameForGetterMethodInvocation(methodName));
    }

    // как method(), только убирает 'get' и 'is'
//    public static <T> ExtractingMatcher<T> getter(Function<T, ?> function) {
//        return new GetterMethodByLambdaExtractingMatcher<>(function);
//    }


    public static <T> ExtractingMatcherBuilder<T[]> arrayElement(int index) {
        return new ExtractingMatcher<>(elementExtractor(new ElementKey(index)));
    }

    // вызывает .get(), O(N) для не-RandomAccess
    public static <T> ExtractingMatcherBuilder<List<T>> element(int index) {
        return new ExtractingMatcher<>(elementExtractor(new ElementKey(index)));
    }

    // O(N)
    public static <T> ExtractingMatcherBuilder<Iterable<T>> iterableElement(int index) {
        return new ExtractingMatcher<>(elementExtractor(new ElementKey(index)));
    }


    public static <K, V> ExtractingMatcherBuilder<Map<K, V>> valueForKey(K key) {
        return new ExtractingMatcher<>(hashMapKeyExtractor(new HashMapKey(key)));
    }



    // TODO: поддерживать массивы, итераторы, итераблы ?мапы?  (это всё обёртки для IteratorMatcher, которые преобразовывают item)
    // TODO: тут нужны перегрузки для всех примитивных типов
    @SafeVarargs
    public static <T> ReportingMatcher<T[]> arrayWithElements(T... elements) {
        return new ConvertingReportingMatcher<>(
                item -> Iterators.forArray((Object[]) item),
                new IteratorMatcher<>(() -> containsInSpecifiedOrderChecker(elements))
        );
    }





    // TODO: рекурсивный матчер, который работает как equalTo
    // TODO: compare().fields().getters().with(expected)
    public static <T> ComparingReportingMatcherBuilder<T> compare() {
        return new ComparingReportingMatcherBuilder<>();
    }




    static String createNameForGetterMethodInvocation(String name) {
        if (name == null) {
            return "";
        }
        if (name.length() > 3 && name.startsWith("get") && isUpperCase(name.charAt(3))) {
            return toLowerCase(name.charAt(3)) + name.substring(4);
        }
        if (name.length() > 2 && name.startsWith("is") && isUpperCase(name.charAt(2))) {
            return toLowerCase(name.charAt(2)) + name.substring(3);
        }
        return name;
    }


//    public static <T> ComparingReportingMatcherBuilder.FieldsMatcherBuilder<T> fields() {
//        return fields(notStaticNotTransientNotSyntheticFieldsPredicate());
//    }
//
//    public static <T> ComparingReportingMatcherBuilder.FieldsMatcherBuilder<T> fields(Predicate<Field> predicate) {
//        return new ComparingReportingMatcherBuilder.FieldsMatcherBuilder<>(predicate);
//    }
//
//    public static <T> ComparingReportingMatcherBuilder.MethodsWithoutParametersMatcherBuilder<T> methodsWithoutParameters() {
//        return methodsWithoutParameters(method -> true);
//    }
//
//    public static <T> ComparingReportingMatcherBuilder.MethodsWithoutParametersMatcherBuilder<T> methodsWithoutParameters(Predicate<Method> predicate) {
//        return new ComparingReportingMatcherBuilder.MethodsWithoutParametersMatcherBuilder<>(predicate);
//    }
//
//    public static <T> ComparingReportingMatcherBuilder.MethodsWithoutParametersMatcherBuilder<T> getters() {
//        return getters(method -> true);
//    }
//
//    public static <T> ComparingReportingMatcherBuilder.MethodsWithoutParametersMatcherBuilder<T> getters(Predicate<Method> predicate) {
//        return methodsWithoutParameters(gettersPredicate().and(predicate));
//    }



    // TODO: метод, который принимает мапу и лямбду, которая преобразовывает значение в матчер, то же для массивов и списков?

    /*public static <E> ReportingMatcher<List<E>> listWithElements(Collection<E> elements) {
        return listWithElementsMatching(
                elements.stream()
                        .map(CoreMatchers::equalTo)
                        .collect(Collectors.toList())
        );
    }
*/
    // TODO: пробовать пропускать элементы чтобы лишний элемент в начале списка не сделал весь список красным?
    // если не использовать uncheckedIsFail() и извлекатель непроверенных элементов, то непроверенные элементы в конце
    // будут проигнорированы, даже в отчёт не попадут, в будущем поведение может измениться
    /*public static <E> ReportingMatcher<List<E>> listWithElementsMatching(Iterable<Matcher<E>> matchers) {
        Collection<ReportingMatcher<List<E>>> elementMatchers = new ArrayList<>();
        int i = 0;
        for (Matcher<E> matcher : matchers) {
            elementMatchers.add(ReportingMatchers.<E>element(i++).is(matcher));
        }
        return sequence(elementMatchers);
    }*/

    // TODO: listWithElementsInAnyOrder, listWithElementsMatchingInAnyOrder

}
