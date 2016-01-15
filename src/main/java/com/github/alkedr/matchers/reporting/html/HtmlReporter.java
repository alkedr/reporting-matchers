package com.github.alkedr.matchers.reporting.html;

import com.github.alkedr.matchers.reporting.Reporter;

import java.io.IOException;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

// TODO: сломанное извлечение отображать как матчер (с каким-нибудь поясняющим текстом)
// TODO: (missing) и (broken) в репортере
// TODO: эскейпить
// TODO: три вкладки: actual, diff, expected
public class HtmlReporter implements Reporter {
    private final Appendable appendable;
    private final String title;

    public HtmlReporter(Appendable appendable, String title) {
        this.appendable = appendable;
        this.title = title;
    }

    @Override
    public void beginReport() {
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
    public void beginKeyValuePair(String keyAsString, ValueStatus valueStatus, String valueAsString) {
        appendDiv("key", escapeHtml4(keyAsString));
        if (valueAsString != null && !valueAsString.isEmpty()) {
            appendDiv(valueStatus + " value", escapeHtml4(valueAsString));
        }
        appendDivStart("checks");
    }

    @Override
    public void addCheck(CheckStatus status, String description) {
        appendDiv(status.toString(), escapeHtml4(description));
    }

    @Override
    public void endKeyValuePair() {
        appendDivEnd();
    }

    @Override
    public void endReport() {
        append("</body></html>");
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
