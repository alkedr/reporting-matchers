package com.github.alkedr.matchers.reporting.map;

import com.github.alkedr.matchers.reporting.ExtractingMatcherBuilder;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.NoOpMatcher.noOp;
import static com.github.alkedr.matchers.reporting.map.ReportingMatchersForMaps.createValueForKeyExtractor;
import static com.github.alkedr.matchers.reporting.map.ReportingMatchersForMaps.valueForKey;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ReportingMatchersForMapsTest {
    @Test
    public void valueForKeyMatcher() {
        assertReflectionEquals(
                new ExtractingMatcherBuilder<>("1", createValueForKeyExtractor(1), noOp()),
                valueForKey(1)
        );
    }
}
