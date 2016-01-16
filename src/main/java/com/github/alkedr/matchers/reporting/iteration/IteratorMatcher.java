package com.github.alkedr.matchers.reporting.iteration;

import com.github.alkedr.matchers.reporting.BaseReportingMatcher;
import com.github.alkedr.matchers.reporting.extraction.ElementExtractor;
import com.google.common.collect.Iterators;
import org.hamcrest.Description;

import java.util.Iterator;

// TODO: убрать abstract, сделать фабрику чекеров полем
// TODO: поддерживать массивы, итераторы, итераблы ?мапы?  (это всё обёртки для IteratorMatcher, которые преобразовывают item)
public abstract class IteratorMatcher<T> extends BaseReportingMatcher<Iterator<T>> {

    protected abstract Checker createChecker();

    // Разделение на end() и end2() нужно чтобы иметь возможность добавлять проверки не только для текущего элемента,
    // но и для элементов, которые были раньше
    protected interface Checker {
        Checks element(Key key, Value value);

        // присоединится к Iterator<KVC> со всеми остальными элементами и НЕ будет с ними мержиться
        Iterator<Object> end();

        // присоединится к Iterator<Object>, который возвращает run() и будет мержиться с остальными элементами
        Iterator<Object> end2();  // TODO: переименовать
    }


    @Override
    public Iterator<Object> run(Object item) {
        Checker checker = createChecker();
        return Iterators.forArray(
                Iterators.concat(
                        Iterators.transform(
                                (Iterator<?>) item,
                                element -> {
                                    Key key = new ElementExtractor(0);  // TODO: индексы
                                    Value value = Value.present(item);
                                    return new KeyValueChecks(key, value, checker.element(key, value));
                                }
                        ),
                        checker.end()
                ),
                checker.end2()
        );
    }

    @Override
    public Iterator<Object> runForMissingItem() {
        Checker checker = createChecker();
        return Iterators.forArray(checker.end(), checker.end2());
    }

    @Override
    public void describeTo(Description description) {
        // TODO
    }
}
