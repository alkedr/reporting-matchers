package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.absent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class AbsentMatcherTest {
    private final SafeTreeReporter safeTreeReporter = mock(SafeTreeReporter.class);
    private final InOrder inOrder = inOrder(safeTreeReporter);

    @Test
    public void singleton() {
        assertSame(absent(), absent());
    }

    @Test
    public void matches() {
        assertFalse(absent().matches(null));
        assertFalse(absent().matches(1));
    }

    @Test
    public void describeTo() {
        assertEquals("absent", StringDescription.asString(absent()));
    }

    @Test
    public void run() {
        absent().run(null, safeTreeReporter);
        inOrder.verify(safeTreeReporter).incorrectlyPresent();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void runForAbsentItem() {
        absent().runForAbsentItem(safeTreeReporter);
        inOrder.verify(safeTreeReporter).correctlyAbsent();
        inOrder.verifyNoMoreInteractions();
    }
}
