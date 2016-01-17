package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.html.HtmlReporter;
import com.github.alkedr.matchers.reporting.utility.ReportingCheckListener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.field;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.getter;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.sequence;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

public class IntegrationTestMain {
    private static final User USER = new User();

    public static void main(String... args) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(args[0])) {
            try (Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
                Reporter reporter = new HtmlReporter(writer, "Заголовок страницы");
                reporter.beginReport();
                isCorrectUser().getChecks(USER, new ReportingCheckListener(reporter));
                reporter.endReport();
            }
        }

        // TODO: missing, broken, другие extractor'ы
    }

    static ReportingMatcher<? super User> isCorrectUser() {
        return sequence(
                field("id").is(123),
                field("login").is("login"),
                field("password").is("drowssap"),
                getter("getBirthDate").is(
                        field("year").is(greaterThan(1900), lessThan(2016)),
                        field("month").is(greaterThan(0), lessThan(13)),
                        field("day").is(greaterThan(0), lessThan(32))
                )
        );
    }


    public static class User {
        private final int id = 123;
        private final String login = "login";
        private final String password = "drowssap";
        private final Date birthDate = new Date();

        public Date getBirthDate() {
            return birthDate;
        }

        public static class Date {
            private final int year = 2050;
            private final int month = 14;
            private final int day = -1;
        }
    }
}
