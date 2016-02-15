package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.MatchesFlagRecordingSimpleTreeReporter;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.*;

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
        run(item, createReporterForDescribeMismatch(stringBuilder));
        description.appendText(stringBuilder.toString());
    }


    static SafeTreeReporter createReporterForDescribeMismatch(Appendable stringBuilder) {
        return simpleTreeReporterToSafeTreeReporter(
                brokenThrowingReporter(
                        notFailedFilteringReporter(
                                checksCountLimitingReporter(
                                        uncheckedNodesFilteringReporter(
                                                plainTextReporter(stringBuilder)
                                        ),
                                        10
                                )
                        )
                )
        );
    }
}
