package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.present;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class PresentMatcherTest {
    private final SafeTreeReporter safeTreeReporter = mock(SafeTreeReporter.class);
    private final InOrder inOrder = inOrder(safeTreeReporter);

    @Test
    public void singleton() {
        assertSame(present(), present());
    }

    @Test
    public void matches() {
        assertTrue(present().matches(null));
        assertTrue(present().matches(1));
    }

    @Test
    public void describeTo() {
        assertEquals("present", StringDescription.asString(present()));
    }

    @Test
    public void run() {
        present().run(null, safeTreeReporter);
        inOrder.verify(safeTreeReporter).correctlyPresent();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void runForAbsentItem() {
        present().runForAbsentItem(safeTreeReporter);
        inOrder.verify(safeTreeReporter).incorrectlyAbsent();
        inOrder.verifyNoMoreInteractions();
    }
}
