package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.keys.ElementKey;

import java.util.Iterator;
import java.util.List;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.broken;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.missing;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.present;

// умеет работать с массивами, итераблами и списками
public class ElementExtractor extends ElementKey implements ExtractingMatcher.Extractor {
    public ElementExtractor(int index) {
        super(index);
    }

    @Override
    public ExtractingMatcher.KeyValue extractFrom(Object item) {
        if (item instanceof Object[]) {
            Object[] array = (Object[]) item;
            if (getIndex() < 0 || getIndex() >= array.length) {
                return new ExtractingMatcher.KeyValue(this, missing());
            }
            return new ExtractingMatcher.KeyValue(this, present(array[getIndex()]));
        }
        if (item instanceof List) {
            List<?> list = (List<?>) item;
            if (getIndex() < 0 || getIndex() >= list.size()) {
                return new ExtractingMatcher.KeyValue(this, missing());
            }
            return new ExtractingMatcher.KeyValue(this, present(list.get(getIndex())));
        }
        if (item instanceof Iterable) {
            Iterator<?> iterator = ((Iterable<?>) item).iterator();
            int currentIndex = 0;
            while (iterator.hasNext()) {
                Object currentElement = iterator.next();
                if (currentIndex == getIndex()) {
                    return new ExtractingMatcher.KeyValue(this, present(currentElement));
                }
            }
            return new ExtractingMatcher.KeyValue(this, missing());
        }
        return new ExtractingMatcher.KeyValue(this, broken(new ClassCastException()));   // FIXME ClassCastException? своё исключение?
    }

    @Override
    public ExtractingMatcher.KeyValue extractFromMissingItem() {
        return new ExtractingMatcher.KeyValue(this, missing());
    }
}
