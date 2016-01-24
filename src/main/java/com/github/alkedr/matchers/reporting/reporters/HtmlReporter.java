package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

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
                + ".PASSED{background-color:#DFF0D8}"
                + ".FAILED{background-color:#F2DEDE}"
                + ".BROKEN{background-color:#FAEBCC}"
                + ".MISSING{background-color:#F2DEDE;}"
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
        if (value != null) {
            appendDiv("value", escapeHtml4(value.toString()));
        }
        appendDivStart("checks");
    }

    @Override
    public void beginMissingNode(Key key) {
        appendDiv("key", escapeHtml4(key.asString()));
        appendDivStart("checks");
    }

    @Override
    public void beginBrokenNode(Key key, Throwable throwable) {
        appendDiv("key", escapeHtml4(key.asString()));
        appendDivStart("checks");
        appendDiv("BROKEN", "при извлечении значения было брошено исключение:\n" + throwableToString(throwable));
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
    public void correctlyMissing() {
        passedCheck("missing");
    }

    @Override
    public void incorrectlyPresent() {
        failedCheck("missing", "present");
    }

    @Override
    public void incorrectlyMissing() {
        failedCheck("present", "missing");
    }

    @Override
    public void passedCheck(String description) {
        appendDiv("PASSED", escapeHtml4(description));
    }

    @Override
    public void failedCheck(String expected, String actual) {
        appendDiv("FAILED", "Expected: " + escapeHtml4(expected) + "\n     but: was " + actual);
    }

    @Override
    public void checkForMissingItem(String description) {
        appendDiv("FAILED", description);
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
        appendDiv("BROKEN", escapeHtml4(description) + "\n" + throwableToString(throwable));
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

    private static String throwableToString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
