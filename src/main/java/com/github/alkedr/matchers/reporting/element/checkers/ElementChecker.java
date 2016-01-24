package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;

public interface ElementChecker {
    void begin(SafeTreeReporter safeTreeReporter);
    void element(Key key, Object value, SafeTreeReporter safeTreeReporter);
    void end(SafeTreeReporter safeTreeReporter);
}
