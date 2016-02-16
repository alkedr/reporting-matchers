package com.github.alkedr.matchers.reporting.reporters;

import static java.util.Arrays.asList;

public enum Reporters {
    ;

    public static SafeTreeReporter noOpSafeTreeReporter() {
        return new NoOpSafeTreeReporter();
    }

    public static SimpleTreeReporter noOpSimpleTreeReporter() {
        return new NoOpSimpleTreeReporter();
    }

    public static SafeTreeReporter simpleTreeReporterToSafeTreeReporter(SimpleTreeReporter simpleTreeReporter) {
        return new SimpleTreeReporterToSafeTreeReporterAdapter(simpleTreeReporter);
    }

    public static MatchesFlagRecordingSimpleTreeReporter matchesFlagRecordingReporter() {
        return new MatchesFlagRecordingSimpleTreeReporterImpl();
    }

    public static SimpleTreeReporter compositeSimpleTreeReporter(SimpleTreeReporter... reporters) {
        return compositeSimpleTreeReporter(asList(reporters));
    }

    public static SimpleTreeReporter compositeSimpleTreeReporter(Iterable<SimpleTreeReporter> reporters) {
        return new CompositeSimpleTreeReporter(reporters);
    }

    public static CloseableSafeTreeReporter mergingReporter(SafeTreeReporter wrappedSafeTreeReporter) {
        return new MergingSafeTreeReporter(wrappedSafeTreeReporter);
    }

    public static SimpleTreeReporter brokenThrowingReporter(SimpleTreeReporter next) {
        return new BrokenThrowingReporter(next);
    }

    public static SimpleTreeReporter notFailedFilteringReporter(SimpleTreeReporter next) {
        return new NotFailedFilteringReporter(next);
    }

    public static SimpleTreeReporter uncheckedNodesFilteringReporter(SimpleTreeReporter next) {
        return new UncheckedNodesFilteringReporter(next);
    }

    public static SimpleTreeReporter checksCountLimitingReporter(SimpleTreeReporter next, int maxChecksCount) {
        return new ChecksCountLimitingReporter(next, maxChecksCount);
    }


    public static CloseableSimpleTreeReporter htmlReporter(Appendable appendable) {
        return new HtmlReporter(appendable);
    }

    public static SimpleTreeReporter plainTextReporter(Appendable appendable) {
        return new PlainTextReporter(appendable);
    }


    public static SafeTreeReporter describeMismatchReporter(Appendable stringBuilder) {
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
