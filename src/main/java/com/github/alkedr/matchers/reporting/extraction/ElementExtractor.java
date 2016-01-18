package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.keys.ElementKey;
import org.apache.commons.collections4.IteratorUtils;

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
            item = ((Iterable<?>) item).iterator();
        }
        if (item instanceof Iterator) {  // iterator не нужен потмоу что по нему можно пройти только 1 раз?
            // TODO: missing если выход за границы
            return new ExtractingMatcher.KeyValue(this, present(IteratorUtils.get((Iterator<?>) item, getIndex())));
        }
        return new ExtractingMatcher.KeyValue(this, broken(new RuntimeException()));   // FIXME ClassCastException? своё исключение?
    }

    @Override
    public ExtractingMatcher.KeyValue extractFromMissingItem() {
        return new ExtractingMatcher.KeyValue(this, missing());
    }
}
