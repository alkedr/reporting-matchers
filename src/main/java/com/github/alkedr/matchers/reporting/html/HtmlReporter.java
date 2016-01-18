package com.github.alkedr.matchers.reporting.html;

import com.github.alkedr.matchers.reporting.Reporter;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

// TODO: сломанное извлечение отображать как матчер (с каким-нибудь поясняющим текстом)
// TODO: (missing) и (broken) в репортере
// TODO: эскейпить
// TODO: три вкладки: actual, diff, expected
// TODO: серые линии слева, как в тасках
public class HtmlReporter implements Reporter, Closeable {
    private final Appendable appendable;
    private final String title;

    public HtmlReporter(Appendable appendable, String title) {
        this.appendable = appendable;
        this.title = title;

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
    public void beginNode(String name, Object value) {
        appendDiv("key", escapeHtml4(name));
        if (value != null) {
            // TODO: разные стили в зависимости от Object.getClass()
            // TODO: защита от очень больших значений
            appendDiv("value", escapeHtml4(value.toString()));
        }
        appendDivStart("checks");
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
    public void brokenCheck(String description, Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        appendDiv("BROKEN", escapeHtml4(description) + "\n" + sw.getBuffer());
    }

    @Override
    public void endNode() {
        appendDivEnd();
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
