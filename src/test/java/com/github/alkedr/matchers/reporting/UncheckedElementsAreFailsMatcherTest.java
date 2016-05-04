package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.reporters.SimpleTreeReporter;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.arrayElement;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.uncheckedElementsAreFails;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class UncheckedElementsAreFailsMatcherTest {
    private final SimpleTreeReporter simpleTreeReporter = mock(SimpleTreeReporter.class);
    private final SafeTreeReporter safeTreeReporter = simpleTreeReporterToSafeTreeReporter(simpleTreeReporter);
    private final InOrder inOrder = inOrder(simpleTreeReporter);

    @Test
    public void test() {
        uncheckedElementsAreFails(arrayElement(0)).run(new Object[]{0}, safeTreeReporter);
        inOrder.verify(simpleTreeReporter).beginPresentNode(elementKey(0), 0);
        inOrder.verify(simpleTreeReporter).incorrectlyPresent();
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }
}
