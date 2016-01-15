package com.github.alkedr.matchers.reporting;

import com.google.common.collect.Iterators;
import org.hamcrest.Description;

import java.util.Iterator;

public class IteratorMatcher<T> extends BaseReportingMatcher<Iterator<T>> {
    private final Checker checker;

    public IteratorMatcher(Checker checker) {
        this.checker = checker;
    }


    // Хочется иметь возможность добавлять проверки не только для текущего элемента, но и для элементов, которые были раньше
    // Это можно сделать если разделить end() на два метода:
    //   - один метод присоединится к Iterator<KVC> со всеми остальными элементами и не будет с ними мержиться
    //   - другой метод присоединится к Iterator<Object>, который возвращает run() и будет мержиться с остальными элементами

    interface Checker {
        Iterator<KeyValueChecks> begin();
        Checks element(Key key, Value value);
        Iterator<KeyValueChecks> end();
        Iterator<Object> end2();  // TODO: переименовать
    }


    @Override
    public Iterator<Object> run(Object item) {
        return Iterators.forArray(
                Iterators.concat(
                        checker.begin(),
                        Iterators.<KeyValueChecks>concat(
                                Iterators.transform(
                                        (Iterator<?>) item,
                                        element -> {
                                            Key key = new Extractors.ListElementExtractor(0);  // TODO: индексы
                                            Value value = Value.present(item);
                                            return new KeyValueChecks(key, value, checker.element(key, value));
                                        }
                                )
                        ),
                        checker.end()
                ),
                checker.end2()
        );
    }

    @Override
    public Iterator<Object> runForMissingItem() {
        return Iterators.forArray(
                Iterators.concat(checker.begin(), checker.end()),
                checker.end2()
        );
    }

    @Override
    public void describeTo(Description description) {
        // TODO
    }
}
