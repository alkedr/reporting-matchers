package com.github.alkedr.matchers.reporting.array;

import com.github.alkedr.matchers.reporting.ExtractingMatcherBuilder;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.NoOpMatcher.noOp;
import static com.github.alkedr.matchers.reporting.array.ReportingMatchersForArrays.arrayElement;
import static com.github.alkedr.matchers.reporting.array.ReportingMatchersForArrays.createArrayElementExtractor;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ReportingMatchersForArraysTest {
    @Test
    public void valueForKeyMatcher() {
        assertReflectionEquals(
                new ExtractingMatcherBuilder<>("1", createArrayElementExtractor(1), noOp()),
                arrayElement(1)
        );
    }
}
