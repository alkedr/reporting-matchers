package com.github.alkedr.matchers.reporting.list;

import com.github.alkedr.matchers.reporting.ExtractingMatcherBuilder;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.NoOpMatcher.noOp;
import static com.github.alkedr.matchers.reporting.list.ReportingMatchersForLists.createListElementExtractor;
import static com.github.alkedr.matchers.reporting.list.ReportingMatchersForLists.element;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ReportingMatchersForListsTest {
    @Test
    public void valueForKeyMatcher() {
        assertReflectionEquals(
                new ExtractingMatcherBuilder<>("1", createListElementExtractor(1), noOp()),
                element(1)
        );
    }
}
