package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.CheckStatus;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.CheckStatus.FAILED;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.CheckStatus.PASSED;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.BROKEN;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.MISSING;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.NORMAL;

public class ExampleReportGeneratorMain {
    public static void main(String... args) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(args[0])) {
            try (Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
                ReportingMatcher.Reporter r = new HtmlReporter(writer, "Заголовок страницы");
                r.beginReport();
                r.addCheck(PASSED, "Пройденная проверка");
                r.addCheck(FAILED, "Непройденная проверка");
                r.addCheck(CheckStatus.BROKEN, "Сломанная проверка");
                r.beginKeyValuePair("ключ1", NORMAL, "43");
                r.addCheck(PASSED, "Пройденная проверка");
                r.addCheck(FAILED, "Непройденная проверка");
                r.addCheck(CheckStatus.BROKEN, "Сломанная проверка");
                r.beginKeyValuePair("ключ11", MISSING, "(missing)");
                r.addCheck(FAILED, "Проверка 1");
                r.addCheck(FAILED, "Проверка 2");
                r.addCheck(FAILED, "Проверка 3");
                r.endKeyValuePair();
                r.endKeyValuePair();
                r.beginKeyValuePair("ключ2", BROKEN, "(broken)");
                r.addCheck(CheckStatus.BROKEN, "Сломанное извлечение");
                r.addCheck(FAILED, "Проверка 1");
                r.addCheck(FAILED, "Проверка 2");
                r.addCheck(FAILED, "Проверка 3");
                r.endKeyValuePair();
                r.endReport();

                // TODO: тестить эскейпинг во всех местах
            }
        }
    }
}
