package com.github.alkedr.matchers.reporting.iteration;

import com.github.alkedr.matchers.reporting.BaseReportingMatcher;
import com.github.alkedr.matchers.reporting.extraction.ElementExtractor;
import com.google.common.collect.Iterators;
import org.hamcrest.Description;

import java.util.Iterator;

// По хорошему надо объединять missing?
// TODO: убрать abstract, сделать фабрику чекеров полем
// TODO: поддерживать массивы, итераторы, итераблы ?мапы?  (это всё обёртки для IteratorMatcher, которые преобразовывают item)
public abstract class IteratorMatcher<T> extends BaseReportingMatcher<Iterator<T>> {

    protected abstract Checker createChecker();



    /*
    Сначала вызываются element(), потом getMissingKeyValueChecksIterator(), потому end()
    То, что возвращает getMissingKeyValueChecksIterator присоединяется к основной группе (в которой все элементы)
     */
    public interface Checker {
        Checks element(Key key, Value value);
        // Вызывается после element()
        Iterator<KeyValueChecks> getMissingKeyValueChecksIterator();
        void end(CheckListener checkListener);
    }



    public abstract static class Checker2 {
        final Iterator<KeyValueChecks> missingIterator;
        final Iterator<KeyValueChecks> secondKeyValueChecksGroupIterator;

        protected Checker2(Iterator<KeyValueChecks> missingIterator,
                          Iterator<KeyValueChecks> secondKeyValueChecksGroupIterator) {
            this.missingIterator = missingIterator;
            this.secondKeyValueChecksGroupIterator = secondKeyValueChecksGroupIterator;
        }

        public abstract Checks element(Key key, Value value);
    }

    // getNextMissingElement и getNextSecondKeyValueChecksGroupElement могут быть вызваны несколько раз в конце, они
    // должны вернуть null каждый раз
    public interface Checker {
        Checks element(Key key, Value value);
        KeyValueChecks getNextMissingElement();
        KeyValueChecks getNextSecondKeyValueChecksGroupElement();
    }

    // Разделение на end() и end2() нужно чтобы иметь возможность добавлять проверки не только для текущего элемента,
    // но и для элементов, которые были раньше
//    protected interface Checker {
//        Checks element(Key key, Value value);
//
//        // присоединится к Iterator<KVC> со всеми остальными элементами и НЕ будет с ними мержиться
//        Iterator<KeyValueChecks> getMissing();
//
//        // присоединится к Iterator<Object>, который возвращает run() и будет мержиться с остальными элементами
//        // можно добавить проверки для любых элементов, а не только текущего
//        Iterator<KeyValueChecks> getSecondKeyValueChecksGroup();
//    }

    @Override
    public void run(Object item, CheckListener checkListener) {
        Checker checker = createChecker();
        checkListener.keyValueChecksGroup(Iterators.concat(
                createPresentElementsIterator((Iterator<?>) item, checker),
                createMissingElementsIterator(checker)
        ));
        checkListener.keyValueChecksGroup(createSecondKeyValueChecksGroupElementsIterator(checker));

//        Checker checker = createChecker();
//        checkListener.keyValueChecksGroup(
//                Iterators.concat(
//                        Iterators.transform(
//                                (Iterator<?>) item,
//                                element -> {
//                                    Key key = new ElementExtractor(0);  // TODO: индексы
//                                    Value value = Value.present(item);
//                                    return new KeyValueChecks(key, value, checker.element(key, value));
//                                }
//                        ),
//                        checker.missingIterator
//                )
//        );
//        checkListener.keyValueChecksGroup(checker.secondKeyValueChecksGroupIterator);
    }

    @Override
    public void runForMissingItem(CheckListener checkListener) {
        Checker checker = createChecker();
        checkListener.keyValueChecksGroup(createMissingElementsIterator(checker));
        checkListener.keyValueChecksGroup(createSecondKeyValueChecksGroupElementsIterator(checker));
//        checkListener.keyValueChecksGroup(checker.missingIterator);
//        checkListener.keyValueChecksGroup(checker.secondKeyValueChecksGroupIterator);
    }

    private static Iterator<KeyValueChecks> createPresentElementsIterator(Iterator<?> item, Checker checker) {
        return Iterators.transform(
                item,
                element -> {
                    Key key = new ElementExtractor(0);  // TODO: индексы
                    Value value = Value.present(item);
                    return new KeyValueChecks(key, value, checker.element(key, value));
                }
        );
    }

    private static Iterator<KeyValueChecks> createMissingElementsIterator(Checker checker) {
        return new Iterator<KeyValueChecks>() {
            private KeyValueChecks next;

            @Override
            public boolean hasNext() {
                if (next == null) {
                    next = checker.getNextMissingElement();
                }
                return next == null;
            }

            @Override
            public KeyValueChecks next() {
                return null;
            }
        };
    }

    private static Iterator<KeyValueChecks> createSecondKeyValueChecksGroupElementsIterator(Checker checker) {
        return null;
    }

    /*@Override
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
    }*/

    @Override
    public void describeTo(Description description) {
        // TODO
    }
}
