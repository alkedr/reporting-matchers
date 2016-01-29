package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.util.function.Consumer;

// TODO: написать в доках интерфейсов как их реализовывать
public interface SafeTreeReporter extends FlatReporter {
    void presentNode(Key key, Object value, Consumer<SafeTreeReporter> contents);
    void absentNode(Key key, Consumer<SafeTreeReporter> contents);
    void brokenNode(Key key, Throwable throwable, Consumer<SafeTreeReporter> contents);
}
