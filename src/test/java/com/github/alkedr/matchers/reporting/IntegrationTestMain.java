package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.CloseableSimpleTreeReporter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.*;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.htmlReporter;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;
import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.containsInSpecifiedOrder;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;

public class IntegrationTestMain {
    private static final User USER = new User();

    public static void main(String... args) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(args[0])) {
            try (Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
                CloseableSimpleTreeReporter reporter = htmlReporter(writer, "Заголовок страницы");
                isCorrectUser().run(USER, simpleTreeReporterToSafeTreeReporter(reporter));
                reporter.close();
            }
        }

        // TODO: missing, broken, другие extractor'ы
    }

    static ReportingMatcher<? super User> isCorrectUser() {
        return sequence(
                field("id").is(123),
                field("login").is("login"),
                field("password").is("drowssap"),
                getter("getNames").is(
                        field("first").is(startsWith("qwe"), endsWith("rty")),
                        field("middle").is(equalTo("123456")),
                        field("last").is(is("ytrewq"))
                ),
                method("getArray").is(
                        arrayElement(0).is(1),
                        array(() -> containsInSpecifiedOrder(1, 2, 3, 4))
                )
        );
    }


    public static class User {
        private final int id = 123;
        private final String login = "login";
        private final String password = "drowssap";
        private final Names names = new Names();

        public Names getNames() {
            return names;
        }

        public Integer[] getArray() {
            return new Integer[]{1, 2, 3};
        }

        public static class Names {
            private final String first = "qwerty";
            private final String middle = "123456";
            private final String last = "ytrewq";
        }
    }
}
