package com.github.alkedr.matchers.reporting.extraction;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.extraction.MethodNameUtils.createNameForRegularMethodInvocation;
import static org.junit.Assert.assertEquals;

public class MethodNameUtils_CreateNameForRegularMethodInvocationTest {
    @Test
    public void noArguments() {
        assertEquals("method()", createNameForRegularMethodInvocation("method"));
    }

    @Test
    public void oneArgument() {
        assertEquals("method(1)", createNameForRegularMethodInvocation("method", 1));
    }

    @Test
    public void twoArguments() {
        assertEquals("method(1, qwe)", createNameForRegularMethodInvocation("method", 1, "qwe"));
    }
}
