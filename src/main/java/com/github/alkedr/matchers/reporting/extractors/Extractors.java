package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.*;

// TODO: убрать static factory?
public enum Extractors {
    ;

    public static Extractor fieldExtractor(FieldKey key) {
        return new FieldExtractor(key);
    }

    public static Extractor fieldByNameExtractor(FieldByNameKey key) {
        return new FieldByNameExtractor(key);
    }

    public static Extractor methodExtractor(MethodKey key) {
        return new MethodExtractor(key);
    }

    public static Extractor methodByNameExtractor(MethodByNameKey key) {
        return new MethodByNameExtractor(key);
    }

    public static Extractor elementExtractor(ElementKey key) {
        return new ElementExtractor(key);
    }

    public static Extractor hashMapKeyExtractor(HashMapKey key) {
        return new HashMapKeyExtractor(key);
    }
}
