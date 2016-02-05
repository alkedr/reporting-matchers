package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.io.IOException;

import static org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

// TODO: эскейпить
// TODO: три вкладки: actual, diff, expected
// TODO: серые линии слева, как в тасках
// TODO: защита от очень больших значений
class HtmlReporter implements CloseableSimpleTreeReporter {
    private final Appendable appendable;

    HtmlReporter(Appendable appendable, String title) {
        this.appendable = appendable;

        append("<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>");
        append(escapeHtml4(title));
        append("</title>"
                + "<style>"
                + "body{"
                + "white-space:pre;"
                + "font-family:'Bitstream Vera Sans Mono','DejaVu Sans Mono',Monaco,Courier,monospace"
                + "}"
                + ".key,.value{vertical-align:top;display:inline-block;}"
                + ".key{font-weight:bold;}"
                + ".key::after{content:\": \"}"
                + ".passed{background-color:#DFF0D8}"
                + ".failed{background-color:#F2DEDE}"
                + ".broken{background-color:#FAEBCC}"
                + ".absent{background-color:#F2DEDE;}"
                + ".checks{margin:0px 0px 10px 20px;}"
                + "</style>"
                + "</head>"
                + "<body>"
        );
    }


    @Override
    public void close() {
        append("</body></html>");
    }


    @Override
    public void beginPresentNode(Key key, Object value) {
        appendDiv("key", escapeHtml4(key.asString()));
        if (value != null && isPrimitiveOrWrapper(value.getClass()) || value instanceof String) {
            appendDiv("value", escapeHtml4(value.toString()));
        }
        appendDivStart("checks");
    }

    @Override
    public void beginAbsentNode(Key key) {
        appendDiv("key", escapeHtml4(key.asString()));
        appendDiv("value", "(отсутствует)");
        appendDivStart("checks");
    }

    @Override
    public void beginBrokenNode(Key key, Throwable throwable) {
        appendDiv("key", escapeHtml4(key.asString()));
        appendDiv("value", "(сломано)");
        appendDivStart("checks");
        appendDiv("broken", "при извлечении значения было брошено исключение:\n" + getStackTrace(throwable));
    }

    @Override
    public void endNode() {
        appendDivEnd();
    }


    @Override
    public void correctlyPresent() {
        passedCheck("present");
    }

    @Override
    public void correctlyAbsent() {
        passedCheck("absent");
    }

    @Override
    public void incorrectlyPresent() {
        failedCheck("absent", "present");
    }

    @Override
    public void incorrectlyAbsent() {
        failedCheck("present", "absent");
    }

    @Override
    public void passedCheck(String description) {
        appendDiv("passed", escapeHtml4(description));
    }

    @Override
    public void failedCheck(String expected, String actual) {
        appendDiv("failed", "Expected: " + escapeHtml4(expected) + "\n     but: " + actual);
    }

    @Override
    public void checkForAbsentItem(String description) {
        appendDiv("failed", description);
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
        appendDiv("broken", escapeHtml4(description) + "\n" + getStackTrace(throwable));
    }


    private void appendDiv(CharSequence cssClass, CharSequence contents) {
        appendDivStart(cssClass);
        append(contents);
        appendDivEnd();
    }

    private void appendDivStart(CharSequence cssClass) {
        append("<div");
        if (cssClass != null && cssClass.length() > 0) {
            append(" class=\"");
            append(cssClass);
            append("\"");
        }
        append(">");
    }

    private void appendDivEnd() {
        append("</div>");
    }


    private void append(CharSequence charSequence) {
        try {
            appendable.append(charSequence);
        } catch (IOException e) {
            throw new RuntimeException(e);  // FIXME: своё исключение?
        }
    }
}
