package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import org.hamcrest.Matcher;

/**
 * TODO: пример кода
 */
// TODO: написать в доках интерфейсов как их реализовывать
public interface ReportingMatcher<T> extends Matcher<T> {
    void run(Object item, SafeTreeReporter safeTreeReporter);
    void runForMissingItem(SafeTreeReporter safeTreeReporter);
}
