package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.sub.value.keys.MethodNameUtils.createNameForRegularMethodInvocation;
import static org.junit.Assert.assertEquals;

public class CreateNameForRegularMethodInvocationTest {
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
