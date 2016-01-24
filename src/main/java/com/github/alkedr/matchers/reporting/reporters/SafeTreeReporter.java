package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.function.Consumer;

// TODO: написать в доках интерфейсов как их реализовывать
public interface SafeTreeReporter extends FlatReporter {
    void presentNode(Key key, Object value, Consumer<SafeTreeReporter> contents);
    void missingNode(Key key, Consumer<SafeTreeReporter> contents);
    void brokenNode(Key key, Throwable throwable, Consumer<SafeTreeReporter> contents);
}
