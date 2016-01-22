package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.Reporter;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatcher;
import static com.github.alkedr.matchers.reporting.keys.Keys.elementKey;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class ExtractorResultsTest {
    private final Reporter reporter = mock(Reporter.class);
    private final InOrder inOrder = inOrder(reporter);
    private final Key key = elementKey(1);
    private final ReportingMatcher<?> matcher = toReportingMatcher(anything("1"));

    @Test
    public void present_createCheckResult() {
        Object value = new Object();
        new Extractor.Result.Present(key, value).createCheckResult(matcher).run(reporter);
        inOrder.verify(reporter).beginNode(key.asString(), value);
        inOrder.verify(reporter).passedCheck("1");
        inOrder.verify(reporter).endNode();
    }

    @Test
    public void missing_createCheckResult() {
        new Extractor.Result.Missing(key).createCheckResult(matcher).run(reporter);
        inOrder.verify(reporter).beginMissingNode(key.asString());
        inOrder.verify(reporter).checkForMissingItem("1");
        inOrder.verify(reporter).endNode();
    }

    @Test
    public void broken_createCheckResult() {
        Throwable throwable = new RuntimeException();
        new Extractor.Result.Broken(key, throwable).createCheckResult(matcher).run(reporter);
        inOrder.verify(reporter).beginBrokenNode(key.asString(), throwable);
        inOrder.verify(reporter).checkForMissingItem("1");
        inOrder.verify(reporter).endNode();
    }


    @Test
    public void present_rename_createCheckResult() {
        Object value = new Object();
        new Extractor.Result.Present(key, value).rename("123").createCheckResult(matcher).run(reporter);
        inOrder.verify(reporter).beginNode("123", value);
        inOrder.verify(reporter).passedCheck("1");
        inOrder.verify(reporter).endNode();
    }

    @Test
    public void missing_rename_createCheckResult() {
        new Extractor.Result.Missing(key).rename("123").createCheckResult(matcher).run(reporter);
        inOrder.verify(reporter).beginMissingNode("123");
        inOrder.verify(reporter).checkForMissingItem("1");
        inOrder.verify(reporter).endNode();
    }

    @Test
    public void broken_rename_createCheckResult() {
        Throwable throwable = new RuntimeException();
        new Extractor.Result.Broken(key, throwable).rename("123").createCheckResult(matcher).run(reporter);
        inOrder.verify(reporter).beginBrokenNode("123", throwable);
        inOrder.verify(reporter).checkForMissingItem("1");
        inOrder.verify(reporter).endNode();
    }


    @Test
    public void present_rename_equals() {
        Object value = new Object();
        Extractor.Result original = new Extractor.Result.Present(key, value);
        Extractor.Result renamed1 = original.rename("123");
        Extractor.Result renamed2 = original.rename("123");
        assertNotSame(original, renamed1);
        assertNotEquals(original.createCheckResult(matcher), renamed1.createCheckResult(matcher));
        assertEquals(renamed1.createCheckResult(matcher), renamed2.createCheckResult(matcher));
    }

    @Test
    public void missing_rename_equals() {
        Extractor.Result original = new Extractor.Result.Missing(key);
        Extractor.Result renamed1 = original.rename("123");
        Extractor.Result renamed2 = original.rename("123");
        assertNotSame(original, renamed1);
        assertNotEquals(original.createCheckResult(matcher), renamed1.createCheckResult(matcher));
        assertEquals(renamed1.createCheckResult(matcher), renamed2.createCheckResult(matcher));
    }

    @Test
    public void broken_rename_equals() {
        Throwable throwable = new RuntimeException();
        Extractor.Result original = new Extractor.Result.Broken(key, throwable);
        Extractor.Result renamed1 = original.rename("123");
        Extractor.Result renamed2 = original.rename("123");
        assertNotSame(original, renamed1);
        assertNotEquals(original.createCheckResult(matcher), renamed1.createCheckResult(matcher));
        assertEquals(renamed1.createCheckResult(matcher), renamed2.createCheckResult(matcher));
    }
}
