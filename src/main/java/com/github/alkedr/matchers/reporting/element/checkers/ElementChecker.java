package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.Reporter;

public interface ElementChecker {
    void begin(Reporter reporter);
    void element(Key key, Object value, Reporter reporter);
    void end(Reporter reporter);
}
