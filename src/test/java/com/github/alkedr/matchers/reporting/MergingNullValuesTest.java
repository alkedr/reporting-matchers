package com.github.alkedr.matchers.reporting;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.field;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.merge;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.noOpSafeTreeReporter;

/**
 * Была бага, MergingSafeTreeReporter.PresentNode.hashCode бросал NullPointerException
 */
public class MergingNullValuesTest {
    @Test
    public void shouldNotThrowNullPointerException() {
        merge(
                field("field"),
                field("field")
        ).run(new MyClass(), noOpSafeTreeReporter());
    }

    private static class MyClass {
        private final Object field = null;
    }
}
