package com.github.alkedr.matchers.reporting.sub.value.extractors;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

public interface SubValuesExtractor<T, S> {
    void run(T item, SubValuesListener<S> subValuesListener);
    void runForAbsentItem(SubValuesListener<S> subValuesListener);

    interface SubValuesListener<S> {
        void present(Key key, S value);
        void absent(Key key);
        void broken(Key key, Throwable throwable);
    }
}
