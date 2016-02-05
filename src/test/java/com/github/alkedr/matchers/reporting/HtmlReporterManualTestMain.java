package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.CloseableSimpleTreeReporter;
import com.github.alkedr.matchers.reporting.reporters.FlatReporter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.htmlReporter;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.fieldByNameKey;

public class HtmlReporterManualTestMain {
    public static void main(String... args) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(args[0])) {
            try (Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
                try (CloseableSimpleTreeReporter reporter = htmlReporter(writer)) {
                    callAllFlatReporterMethods(reporter);
                    reporter.beginPresentNode(fieldByNameKey("present"), "present_value");
                    callAllFlatReporterMethods(reporter);
                    reporter.endNode();
                    reporter.beginAbsentNode(fieldByNameKey("absent"));
                    callAllFlatReporterMethods(reporter);
                    reporter.endNode();
                    reporter.beginBrokenNode(fieldByNameKey("broken"), new RuntimeException("broken"));
                    callAllFlatReporterMethods(reporter);
                    reporter.endNode();
                    reporter.beginPresentNode(fieldByNameKey("unchecked_present"), "unchecked_present_value");
                    reporter.endNode();
                    reporter.beginAbsentNode(fieldByNameKey("unchecked_absent"));
                    reporter.endNode();
                    reporter.beginBrokenNode(fieldByNameKey("unchecked_broken"), new RuntimeException("unchecked_broken"));
                    reporter.endNode();
                }
            }
        }
    }

    private static void callAllFlatReporterMethods(FlatReporter reporter) {
        reporter.correctlyPresent();
        reporter.correctlyAbsent();
        reporter.incorrectlyPresent();
        reporter.incorrectlyAbsent();
        reporter.passedCheck("passed check 1");
        reporter.failedCheck("failed check 1 expected", "failed check 1 actual");
        reporter.checkForAbsentItem("check for absent item 1");
        reporter.brokenCheck("broken check 1", new RuntimeException("broken check 1"));
    }
}
