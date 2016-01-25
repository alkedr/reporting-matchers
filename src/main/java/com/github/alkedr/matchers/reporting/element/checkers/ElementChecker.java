package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

import java.util.function.Consumer;

public interface ElementChecker {
    void begin(SafeTreeReporter safeTreeReporter);
    Consumer<SafeTreeReporter> element(Key key, Object value);
    void end(SafeTreeReporter safeTreeReporter);  // TODO: передавать здесь кол-во элементов чтобы реализациям не нужно было считать элементы?
}
