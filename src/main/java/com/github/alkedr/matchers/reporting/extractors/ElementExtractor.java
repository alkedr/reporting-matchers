package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.Iterator;
import java.util.List;

import static com.github.alkedr.matchers.reporting.keys.Keys.elementKey;

// умеет работать с массивами, итераблами и списками
class ElementExtractor implements Extractor {
    private final int index;
    private final Key key;

    ElementExtractor(int index) {
        this.index = index;
        this.key = elementKey(index);
    }

    @Override
    public Result extractFrom(Object item) {
        if (item instanceof Object[]) {
            Object[] array = (Object[]) item;
            if (index < 0 || index >= array.length) {
                return new Result.Missing(key);
            }
            return new Result.Present(key, array[index]);
        }
        if (item instanceof List) {
            List<?> list = (List<?>) item;
            if (index < 0 || index >= list.size()) {
                return new Result.Missing(key);
            }
            return new Result.Present(key, list.get(index));
        }
        if (item instanceof Iterable) {
            Iterator<?> iterator = ((Iterable<?>) item).iterator();
            int currentIndex = 0;
            while (iterator.hasNext()) {
                Object currentElement = iterator.next();
                if (currentIndex == index) {
                    return new Result.Present(key, currentElement);
                }
            }
            return new Result.Missing(key);
        }
        return new Result.Broken(key, new ClassCastException());   // FIXME ClassCastException? своё исключение?
    }

    @Override
    public Result extractFromMissingItem() {
        return new Result.Missing(key);
    }
}
