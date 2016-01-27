package com.github.alkedr.matchers.reporting;

public interface ReportingMatcherBuilder<T> extends ReportingMatcher<T> {
    ReportingMatcher<T> build();
}
