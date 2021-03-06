package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.CloseableSimpleTreeReporter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.*;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.htmlReporter;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;
import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.containsInAnyOrder;
import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractors.getters;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;

public class IntegrationTestMain {
    private static final User USER = new User();

    public static void main(String... args) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(args[0])) {
            try (Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
                CloseableSimpleTreeReporter reporter = htmlReporter(writer);
                isCorrectUser().run(USER, simpleTreeReporterToSafeTreeReporter(reporter));
                reporter.close();
            }
        }

        // TODO: missing, broken, другие extractor'ы
    }

    static ReportingMatcher<? super User> isCorrectUser() {
        return merge(
                field("id", 123),
                field("login", "login"),
                field("password", "drowssap"),
                getter("getNames"
                        , field("first", startsWith("qwe"), endsWith("rty"))
                        , field("middle", equalTo("123456"))
                        , field("last", is("ytrewq"))
                ),
                method(invocation("getArray")
                        , arrayElement(0, 1)
                        , array(containsInAnyOrder(1, 2, 3))
                ),
                displayAll(getters())
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

        public URL getUnchecked() {
            try {
                return new URL("http://example.com");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        public static class Names {
            private final String first = "qwerty";
            private final String middle = "123456";
            private final String last = "ytrewq";
        }
    }
}
