package com.github.alkedr.matchers.reporting;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Обёртка для матчеров, которая позволяет сделать любой матчер ReportingMatcher'ом
 */
public class ReportingMatcherAdapter<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    private final Matcher<T> regularMatcher;

    // Если заранее неизвестно является ли матчер ReportingMatcher'ом, то лучше использовать toReportingMatcher()
    public ReportingMatcherAdapter(Matcher<T> regularMatcher) {
        this.regularMatcher = regularMatcher;
    }

    @Override
    public void run(Object item, Reporter reporter) {
        // TODO: сделать ловлю исключений настраиваемой
        boolean matches;
        try {
            matches = regularMatcher.matches(item);
        } catch (RuntimeException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            reporter.addCheck(Reporter.CheckStatus.BROKEN, sw.getBuffer().toString());
            return;
        }
        if (matches) {
            // TODO: подсовывать свой Description, который отлавливает equalTo и is
            reporter.addCheck(Reporter.CheckStatus.PASSED, StringDescription.toString(regularMatcher));
        } else {
            Description stringDescription = new StringDescription()
                    .appendText("Expected: ")
                    .appendDescriptionOf(regularMatcher)
                    .appendText("\n     but: ");
            regularMatcher.describeMismatch(item, stringDescription);
            reporter.addCheck(Reporter.CheckStatus.FAILED, stringDescription.toString());
        }
    }

    @Override
    public void runForMissingItem(Reporter reporter) {
        // TODO: специальный ITEM_IS_MISSING в Reporter.CheckStatus для таких случаев?
        reporter.addCheck(Reporter.CheckStatus.FAILED, StringDescription.asString(regularMatcher));
    }

    @Override
    public boolean matches(Object item) {
        return regularMatcher.matches(item);
    }

    @Override
    public void describeTo(Description description) {
        regularMatcher.describeTo(description);
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        regularMatcher.describeMismatch(item, description);
    }


    // оборачивает переданный ему матчер если он не reporting.
    public static <T> ReportingMatcher<T> toReportingMatcher(Matcher<T> matcher) {
        return matcher instanceof ReportingMatcher ? (ReportingMatcher<T>) matcher : new ReportingMatcherAdapter<>(matcher);
    }

    public static <U> Iterable<ReportingMatcher<? super U>> toReportingMatchers(Iterable<Matcher<? super U>> matchers) {
        // TODO: конвертировать на лету?
        Collection<ReportingMatcher<? super U>> result = new ArrayList<>();
        for (Matcher<? super U> matcher : matchers) {
            result.add(toReportingMatcher(matcher));
        }
        return result;
    }

    // TODO: отдельная обёртка для Matcher'ов (?и ReportingMatcher'ов?), которая ловит исключения и делает BROKEN
    // TODO: (сделать ловлю исключений опциональной/настраиваемой)
}
