package com.github.alkedr.matchers.reporting.integration;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.SequenceMatcher;
import com.github.alkedr.matchers.reporting.html.HtmlReporter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import static com.github.alkedr.matchers.reporting.object.ReportingMatchersForObjects.field;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

public class ExampleReportGeneratorMain {
    private static final User USER = new User();

    public static void main(String... args) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(args[0])) {
            try (Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
                ReportingMatcher.Reporter reporter = new HtmlReporter(writer, "Заголовок страницы");
                reporter.beginReport();
                new SequenceMatcher<>(asList(
                        field("id").is(123),
                        field("login").is("login"),
                        field("password").is("drowssap"),
                        field("birthDate").is(
                                field("year").is(greaterThan(1900), lessThan(2016)),
                                field("month").is(greaterThan(0), lessThan(13)),
                                field("day").is(greaterThan(0), lessThan(32))
                        )
                )).run(USER, reporter);
                reporter.endReport();
            }
        }
    }


    private static class User {
        private final int id = 123;
        private final String login = "login";
        private final String password = "drowssap";
        private final Date birthDate = new Date();

        private static class Date {
            private final int year = 2050;
            private final int month = 14;
            private final int day = -1;
        }
    }
}
