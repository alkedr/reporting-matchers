package com.github.alkedr.matchers.reporting.utility;

import com.github.alkedr.matchers.reporting.Reporter;
import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

public class ReportingCheckListener implements ReportingMatcher.CheckListener {
    private final Reporter reporter;
    private final ReportingMatcher.CheckListener checkListenerForRecursiveCalls;  // для многоступенчатых обёрток

    public ReportingCheckListener(Reporter reporter) {
        this.reporter = reporter;
        this.checkListenerForRecursiveCalls = this;
    }

    public ReportingCheckListener(Reporter reporter, ReportingMatcher.CheckListener checkListenerForRecursiveCalls) {
        this.reporter = reporter;
        this.checkListenerForRecursiveCalls = checkListenerForRecursiveCalls;
    }

    // TODO: отдельная обёртка для Matcher'ов (?и ReportingMatcher'ов?), которая ловит исключения и делает BROKEN
    // TODO: (сделать ловлю исключений опциональной/настраиваемой)
    // TODO: м. б. в Iterator<Object> сделать не Matcher, а объект, который умеет добавляться в отчёт?
    @Override
    public void simpleMatcher(Object item, Matcher<?> matcher) {
        boolean matches;
        try {
            matches = matcher.matches(item);
        } catch (RuntimeException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            reporter.addCheck(Reporter.CheckStatus.BROKEN, sw.getBuffer().toString());
            return;
        }
        if (matches) {
            // TODO: подсовывать свой Description, который отлавливает equalTo и is
            reporter.addCheck(Reporter.CheckStatus.PASSED, StringDescription.toString(matcher));
        } else {
            Description stringDescription = new StringDescription()
                    .appendText("Expected: ")
                    .appendDescriptionOf(matcher)
                    .appendText("\n     but: ");
            matcher.describeMismatch(item, stringDescription);
            reporter.addCheck(Reporter.CheckStatus.FAILED, stringDescription.toString());
        }
    }

    @Override
    public void simpleMatcherForMissingItem(Matcher<?> matcher) {
        // TODO:
    }

    @Override
    public void keyValueChecksGroup(Iterator<ReportingMatcher.KeyValueChecks> keyValueChecksGroup) {
        keyValueChecksGroup.forEachRemaining(this::keyValueChecks);
    }

    private void keyValueChecks(ReportingMatcher.KeyValueChecks kvc) {
        reporter.beginKeyValuePair(kvc.key().asString(), null, kvc.value().asString());
        // TODO: missing, broken etc
        kvc.checks().matcher().run(kvc.value().get(), checkListenerForRecursiveCalls);
        reporter.endKeyValuePair();
    }
}
