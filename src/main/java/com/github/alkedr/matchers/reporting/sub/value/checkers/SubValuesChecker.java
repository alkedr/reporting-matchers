package com.github.alkedr.matchers.reporting.sub.value.checkers;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.util.function.Consumer;

// методы present, missing и broken добавляют содержимое нод
public interface SubValuesChecker {
    // TODO: убрать begin()
    void begin(SafeTreeReporter safeTreeReporter);
    Consumer<SafeTreeReporter> present(Key key, Object value);
    Consumer<SafeTreeReporter> absent(Key key);
    Consumer<SafeTreeReporter> broken(Key key, Throwable throwable);
    void end(SafeTreeReporter safeTreeReporter);
}
