package com.github.alkedr.matchers.reporting.iteration;

import com.github.alkedr.matchers.reporting.BaseReportingMatcher;
import com.github.alkedr.matchers.reporting.keys.ElementKey;
import org.hamcrest.Description;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import static java.util.Collections.emptyIterator;

// По хорошему надо объединять missing?
// TODO: убрать abstract, сделать фабрику чекеров полем
// TODO: поддерживать массивы, итераторы, итераблы ?мапы?  (это всё обёртки для IteratorMatcher, которые преобразовывают item)
public class IteratorMatcher<T> extends BaseReportingMatcher<Iterator<T>> {
    private final Supplier<ElementChecker> elementCheckerSupplier;

    public IteratorMatcher(Supplier<ElementChecker> elementCheckerSupplier) {
        this.elementCheckerSupplier = elementCheckerSupplier;
    }

    @Override
    public Checks getChecks(Object item) {
        return Checks.sequence(new ChecksIterator(elementCheckerSupplier.get(), (Iterator<Object>) item));
    }

    @Override
    public Checks getChecksForMissingItem() {
        return Checks.sequence(new ChecksIterator(elementCheckerSupplier.get(), emptyIterator()));
    }

    // объединяется не всё со всем, а:
    //   - begin()'ы между собой
    //   - element()'ы для каждого элемента между собой
    //   - end()'ы между собой
    // Плохо что begin()'ы не объединяются с end()'ами. Точно ли нужны begin()'ы?
    public interface ElementChecker {
        Checks begin();
        Checks element(Key key, Value value);   // TODO: Object item?
        Checks end();
    }


    @Override
    public void describeTo(Description description) {
        // TODO
    }

    private static class ChecksIterator implements Iterator<Checks> {
        private final ElementChecker elementChecker;
        private final Iterator<Object> iterator;
        private boolean beginCalled = false;
        private int index = 0;
        private boolean endCalled = false;

        ChecksIterator(ElementChecker elementChecker, Iterator<Object> iterator) {
            this.elementChecker = elementChecker;
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return !endCalled;
        }

        @Override
        public Checks next() {
            if (!beginCalled) {
                beginCalled = true;
                return elementChecker.begin();
            }
            if (iterator.hasNext()) {
                Key key = new ElementKey(index++);
                Value value = Value.present(iterator.next());
                Checks checks = elementChecker.element(key, value);
                return Checks.keyValueChecks(new KeyValueChecks(key, value, checks));
            }
            if (!endCalled) {
                endCalled = true;
                return elementChecker.end();
            }
            throw new NoSuchElementException();  // FIXME
        }
    }
}
