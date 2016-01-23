package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.ElementKey;

import java.util.Iterator;
import java.util.List;

class ElementExtractor implements Extractor {
    private final ElementKey key;

    ElementExtractor(ElementKey key) {
        this.key = key;
    }

    @Override
    public void extractFrom(Object item, ResultListener result) {
        if (item == null) {
            result.missing(key);
        } else {
            if (item instanceof Object[]) {
                extractFromArray((Object[]) item, result);
            } else if (item instanceof List) {
                extractFromList((List<?>) item, result);
            } else if (item instanceof Iterable) {
                extractFromIterable((Iterable<?>) item, result);
            } else {
                result.broken(key, new ClassCastException());  // FIXME ClassCastException? своё исключение?
            }
        }
    }

    private void extractFromArray(Object[] array, ResultListener result) {
        if (key.getIndex() < 0 || key.getIndex() >= array.length) {
            result.missing(key);
        } else {
            result.present(key, array[key.getIndex()]);
        }
    }

    private void extractFromList(List<?> list, ResultListener result) {
        if (key.getIndex() < 0 || key.getIndex() >= list.size()) {
            result.missing(key);
        } else {
            result.present(key, list.get(key.getIndex()));
        }
    }

    private void extractFromIterable(Iterable<?> iterable, ResultListener result) {
        Iterator<?> iterator = iterable.iterator();
        int currentIndex = 0;
        while (iterator.hasNext()) {
            Object currentElement = iterator.next();
            if (currentIndex == key.getIndex()) {
                result.present(key, currentElement);
                return;
            }
        }
        result.missing(key);
    }

    @Override
    public void extractFromMissingItem(ResultListener result) {
        extractFrom(null, result);
    }
}
