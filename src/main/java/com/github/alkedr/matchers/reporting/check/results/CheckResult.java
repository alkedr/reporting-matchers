package com.github.alkedr.matchers.reporting.check.results;

import com.github.alkedr.matchers.reporting.reporters.Reporter;

// можно имплементить, но нельзя будет мержить?
public interface CheckResult {
    void run(Reporter reporter);
}
