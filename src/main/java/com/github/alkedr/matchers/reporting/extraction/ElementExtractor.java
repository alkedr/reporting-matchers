package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.Validate;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.broken;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.missing;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.present;

// TODO: сделать универсальным (array, iterable, random access list)
public class ElementExtractor implements ExtractingMatcher.Extractor, ReportingMatcher.Key {
    private final int index;

    public ElementExtractor(int index) {
        Validate.isTrue(index >= 0, "index must be positive");
        this.index = index;
    }

    @Override
    public ExtractingMatcher.KeyValue extractFrom(Object item) {
        if (item instanceof Object[]) {
            Object[] array = (Object[]) item;
            if (index < 0 || index >= array.length) {
                return new ExtractingMatcher.KeyValue(this, missing());
            }
            return new ExtractingMatcher.KeyValue(this, present(array[index]));
        }
        if (item instanceof List) {
            List<?> list = (List<?>) item;
            if (index < 0 || index >= list.size()) {
                return new ExtractingMatcher.KeyValue(this, missing());
            }
            return new ExtractingMatcher.KeyValue(this, present(list.get(index)));
        }
        if (item instanceof Iterable) {
            item = ((Iterable<?>) item).iterator();
        }
        if (item instanceof Iterator) {  // iterator не нужен потмоу что по нему можно пройти только 1 раз?
            // TODO: missing если выход за границы
            return new ExtractingMatcher.KeyValue(this, present(IteratorUtils.get((Iterator<?>) item, index)));
        }
        return new ExtractingMatcher.KeyValue(this, broken(new RuntimeException()));   // FIXME ClassCastException? своё исключение?
    }

    @Override
    public ExtractingMatcher.KeyValue extractFromMissingItem() {
        return new ExtractingMatcher.KeyValue(this, missing());
    }

    @Override
    public String asString() {
        return "[" + index + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementExtractor that = (ElementExtractor) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
