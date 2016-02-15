package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Scanner;

import static org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper;
import static org.apache.commons.lang3.StringEscapeUtils.escapeJson;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

// TODO: эскейпить
// TODO: три вкладки: actual, diff, expected
// TODO: серые линии слева, как в тасках
// TODO: защита от очень больших значений
class HtmlReporter implements CloseableSimpleTreeReporter {
    private static final String HTML_BEGIN;
    private static final String HTML_END;

    private final Appendable appendable;

    static {
        String htmlTemplate = resourceFileToString("/report.html");
        String css = resourceFileToString("/report.css");
        String js = resourceFileToString("/report.js");
        String[] checksSplit = htmlTemplate.split("@@CHECKS@@");
        HTML_BEGIN = checksSplit[0].replaceFirst("@@JS@@", js).replaceFirst("@@CSS@@", css);
        HTML_END = checksSplit[1];
    }

    @Deprecated
    HtmlReporter(Appendable appendable, String title) {
        this(appendable);
    }

    HtmlReporter(Appendable appendable) {
        this.appendable = appendable;
        append(HTML_BEGIN);
    }


    @Override
    public void close() {
        append(HTML_END);
    }


    @Override
    public void beginPresentNode(Key key, Object value) {
        append("presentNode(\"");
        append(escapeJson(key.asString()));
        append("\",");
        if (value == null) {
            append("null");
        } else if (isPrimitiveOrWrapper(value.getClass()) || value instanceof String) {
            append("\"");
            append(value.toString());
            append("\"");
        } else {
            append("\"\"");
        }
        append(",[");
    }

    @Override
    public void beginAbsentNode(Key key) {
        append("absentNode(\"");
        append(escapeJson(key.asString()));
        append("\",[");
    }

    @Override
    public void beginBrokenNode(Key key, Throwable throwable) {
        append("brokenNode(\"");
        append(escapeJson(key.asString()));
        append("\",\"");
        append(escapeJson(getStackTrace(throwable)));
        append("\",[");
    }

    @Override
    public void endNode() {
        append("]),");
    }


    @Override
    public void correctlyPresent() {
        append("correctlyPresent(),");
    }

    @Override
    public void correctlyAbsent() {
        append("correctlyAbsent(),");
    }

    @Override
    public void incorrectlyPresent() {
        append("incorrectlyPresent(),");
    }

    @Override
    public void incorrectlyAbsent() {
        append("incorrectlyAbsent(),");
    }

    @Override
    public void passedCheck(String description) {
        append("passedCheck(\"");
        append(escapeJson(description));
        append("\"),");
    }

    @Override
    public void failedCheck(String expected, String actual) {
        append("failedCheck(\"");
        append(escapeJson(expected));
        append("\",\"");
        append(escapeJson(actual));
        append("\"),");
    }

    @Override
    public void checkForAbsentItem(String description) {
        append("checkForAbsentItem(\"");
        append(escapeJson(description));
        append("\"),");
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
        append("brokenCheck(\"");
        append(escapeJson(description));
        append("\",\"");
        append(escapeJson(getStackTrace(throwable)));
        append("\"),");
    }


    private void append(CharSequence charSequence) {
        try {
            appendable.append(charSequence);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String resourceFileToString(String name) {
        return new Scanner(HtmlReporter.class.getResourceAsStream(name), "UTF-8").useDelimiter("\\A").next();
    }
}
