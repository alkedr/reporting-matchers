package com.github.alkedr.matchers.reporting.reporters;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.reporters.PlainTextReporter.indent;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.plainTextReporter;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.junit.Assert.assertEquals;

// TODO: проверить отступы ВЕЗДЕ!
public class PlainTextReporterTest {
    private final StringBuilder stringBuilder = new StringBuilder();
    private final SimpleTreeReporter reporter = plainTextReporter(stringBuilder);

    @Test
    public void beginPresentNode() {
        reporter.beginPresentNode(elementKey(0), 1);
        reporter.beginPresentNode(elementKey(1), 2);
        assertEquals("[1]: 1\n\t[2]: 2\n", stringBuilder.toString());
    }

    @Test
    public void beginAbsentNode() {
        reporter.beginAbsentNode(elementKey(0));
        reporter.beginAbsentNode(elementKey(1));
        assertEquals("[1]: (absent)\n\t[2]: (absent)\n", stringBuilder.toString());
    }

    @Test
    public void beginBrokenNode() {
        Throwable throwable1 = new RuntimeException("123");
        Throwable throwable2 = new IndexOutOfBoundsException("234");
        reporter.beginBrokenNode(elementKey(0), throwable1);
        reporter.beginBrokenNode(elementKey(1), throwable2);
        assertEquals("[1]: (broken)\n" + getStackTrace(throwable1) + "\t[2]: (broken)\n" + indent(getStackTrace(throwable2), "\t"), stringBuilder.toString());
    }

    @Test
    public void endNode() {
        reporter.beginPresentNode(elementKey(0), 1);
        reporter.endNode();
        reporter.beginPresentNode(elementKey(1), 2);
        assertEquals("[1]: 1\n[2]: 2\n", stringBuilder.toString());
    }

    @Test
    public void correctlyPresent() {
        reporter.beginPresentNode(elementKey(0), 1);
        reporter.correctlyPresent();
        reporter.endNode();
        assertEquals("[1]: 1\n\t[ ok ] - is present\n", stringBuilder.toString());
    }

    @Test
    public void correctlyAbsent() {
        reporter.beginPresentNode(elementKey(0), 1);
        reporter.correctlyAbsent();
        reporter.endNode();
        assertEquals("[1]: 1\n\t[ ok ] - is absent\n", stringBuilder.toString());
    }

    @Test
    public void incorrectlyPresent() {
        reporter.beginPresentNode(elementKey(0), 1);
        reporter.incorrectlyPresent();
        reporter.endNode();
        assertEquals("[1]: 1\n\t[fail] - Expected: is absent\n\t              but: was present\n", stringBuilder.toString());
    }

    @Test
    public void incorrectlyAbsent() {
        reporter.beginPresentNode(elementKey(0), 1);
        reporter.incorrectlyAbsent();
        reporter.endNode();
        assertEquals("[1]: 1\n\t[fail] - Expected: is present\n\t              but: was absent\n", stringBuilder.toString());
    }

    @Test
    public void passedCheck() {
        reporter.beginPresentNode(elementKey(0), 1);
        reporter.passedCheck("2");
        reporter.endNode();
        assertEquals("[1]: 1\n\t[ ok ] - 2\n", stringBuilder.toString());
    }

    @Test
    public void failedCheck() {
        reporter.beginPresentNode(elementKey(0), 1);
        reporter.failedCheck("2", "3");
        reporter.endNode();
        assertEquals("[1]: 1\n\t[fail] - Expected: 2\n\t              but: 3\n", stringBuilder.toString());
    }

    @Test
    public void checkForAbsentItem() {
        reporter.beginPresentNode(elementKey(0), 1);
        reporter.checkForAbsentItem("2");
        reporter.endNode();
        assertEquals("[1]: 1\n\t[fail] - Expected: 2\n", stringBuilder.toString());
    }

    @Test
    public void brokenCheck() {
        Throwable throwable = new RuntimeException("123");
        reporter.beginPresentNode(elementKey(0), 1);
        reporter.brokenCheck("2", throwable);
        reporter.endNode();
        assertEquals("[1]: 1\n\t[fail] - Expected: 2\n\t" + indent(getStackTrace(throwable), "\t"), stringBuilder.toString());
    }
}
