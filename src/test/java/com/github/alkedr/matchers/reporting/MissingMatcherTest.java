package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.missing;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class MissingMatcherTest {
    private final SafeTreeReporter safeTreeReporter = mock(SafeTreeReporter.class);
    private final InOrder inOrder = inOrder(safeTreeReporter);

    @Test
    public void singleton() {
        assertSame(missing(), missing());
    }

    @Test
    public void matches() {
        assertFalse(missing().matches(null));
        assertFalse(missing().matches(1));
    }

    @Test
    public void describeTo() {
        assertEquals("missing", StringDescription.asString(missing()));
    }

    @Test
    public void run() {
        missing().run(null, safeTreeReporter);
        inOrder.verify(safeTreeReporter).incorrectlyPresent();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void runForMissingItem() {
        missing().runForMissingItem(safeTreeReporter);
        inOrder.verify(safeTreeReporter).correctlyMissing();
        inOrder.verifyNoMoreInteractions();
    }
}
