package com.github.alkedr.matchers.reporting.sub.value.extractors;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

public interface SubValuesExtractor<T> {
    void run(T item, SubValuesListener subValuesListener);
    void runForAbsentItem(SubValuesListener subValuesListener);

    interface SubValuesListener {
        void present(Key key, Object value);
        void absent(Key key);
        void broken(Key key, Throwable throwable);
    }
}
