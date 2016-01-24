package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;

// TODO: написать в доках интерфейсов как их реализовывать
public interface SimpleTreeReporter extends FlatReporter {
    void beginPresentNode(Key key, Object value);
    void beginMissingNode(Key key);
    void beginBrokenNode(Key key, Throwable throwable);
    void endNode();
}