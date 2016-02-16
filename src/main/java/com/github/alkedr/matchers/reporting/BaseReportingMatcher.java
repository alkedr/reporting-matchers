package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.MatchesFlagRecordingSimpleTreeReporter;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.describeMismatchReporter;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.matchesFlagRecordingReporter;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;

public abstract class BaseReportingMatcher<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    @Override
    public boolean matches(Object item) {
        MatchesFlagRecordingSimpleTreeReporter reporter = matchesFlagRecordingReporter();
        run(item, simpleTreeReporterToSafeTreeReporter(reporter));
        return reporter.getMatchesFlag();
    }

    @Override
    public void describeTo(Description description) {
        // Не добавляем в description все проверки, потому что нужно чтобы ReportingMatcher'ы можно было передавать в
        // assertThat. assertThat отображает и describeTo, и describeMismatch, полноценный отчёт должен быть только в
        // одном из них
        description.appendText("is correct");
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        StringBuilder stringBuilder = new StringBuilder();
        run(item, describeMismatchReporter(stringBuilder));
        description.appendText(stringBuilder.toString());
    }
}
