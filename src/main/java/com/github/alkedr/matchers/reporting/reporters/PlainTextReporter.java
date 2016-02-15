package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.io.IOException;
import java.io.UncheckedIOException;

import static org.apache.commons.lang3.StringUtils.repeat;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

class PlainTextReporter implements SimpleTreeReporter {
    private final Appendable appendable;
    private int indentationLevel = 0;

    PlainTextReporter(Appendable appendable) {
        this.appendable = appendable;
    }

    @Override
    public void beginPresentNode(Key key, Object value) {
        append(indentation());
        append(key.asString());
        append(": ");
        append(String.valueOf(value));    // TODO: делать правильные отступы если в значении содержатся переходы на новую строку
        append("\n");
        indentationLevel++;
    }

    @Override
    public void beginAbsentNode(Key key) {
        append(indentation());
        append(key.asString());
        append(": (absent)\n");
        indentationLevel++;
    }

    @Override
    public void beginBrokenNode(Key key, Throwable throwable) {
        append(indentation());
        append(key.asString());
        append(": (broken)\n");
        append(indent(getStackTrace(throwable), indentation()));
        indentationLevel++;
    }

    @Override
    public void endNode() {
        indentationLevel--;
    }

    @Override
    public void correctlyPresent() {
        passedCheck("is present");
    }

    @Override
    public void correctlyAbsent() {
        passedCheck("is absent");
    }

    @Override
    public void incorrectlyPresent() {
        failedCheck("is absent", "was present");
    }

    @Override
    public void incorrectlyAbsent() {
        failedCheck("is present", "was absent");
    }

    @Override
    public void passedCheck(String description) {
        append(indentation());
        append("[ ok ] - ");
        append(description);
        append("\n");
    }

    @Override
    public void failedCheck(String expected, String actual) {
        append(indentation());
        append("[fail] - Expected: ");
        append(expected);
        append("\n");
        append(indentation());
        append("              but: ");
        append(actual);
        append("\n");
    }

    @Override
    public void checkForAbsentItem(String description) {
        append(indentation());
        append("[fail] - Expected: ");
        append(description);
        append("\n");
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
        append(indentation());
        append("[fail] - Expected: ");
        append(description);
        append("\n");
        append(indentation());
        append(indent(getStackTrace(throwable), indentation()));
    }


    static CharSequence indent(String s, String indentation) {
        return s.replaceAll("(?m)^", indentation);
    }

    private String indentation() {
        return repeat('\t', indentationLevel);
    }

    private void append(CharSequence charSequence) {
        try {
            appendable.append(charSequence);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
