package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.noOp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class NoOpMatcherTest {
    private final SafeTreeReporter safeTreeReporter = mock(SafeTreeReporter.class);
    private final InOrder inOrder = inOrder(safeTreeReporter);

    @Test
    public void singleton() {
        assertSame(noOp(), noOp());
    }

    @Test
    public void matches() {
        assertTrue(noOp().matches(null));
        assertTrue(noOp().matches(1));
    }

    @Test
    public void describeTo() {
        assertEquals("anything", StringDescription.asString(noOp()));
    }

    @Test
    public void run() {
        noOp().run(null, safeTreeReporter);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void runForAbsentItem() {
        noOp().runForAbsentItem(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();
    }
}
