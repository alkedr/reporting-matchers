package com.github.alkedr.matchers.reporting.reporters;

import static java.util.Arrays.asList;

public enum Reporters {
    ;

    public static SafeTreeReporter simpleTreeReporterToSafeTreeReporter(SimpleTreeReporter simpleTreeReporter) {
        return new SimpleToSafeTreeReporterAdapter(simpleTreeReporter);
    }

    public static CloseableSimpleTreeReporter htmlReporter(Appendable appendable, String title) {
        return new HtmlReporter(appendable, title);
    }

    public static MatchesFlagRecordingReporter matchesFlagRecordingReporter() {
        return new MatchesFlagRecordingReporterImpl();
    }

    public static SafeTreeReporter noOpSafeTreeReporter() {
        return new NoOpSafeTreeReporter();
    }

    public static SimpleTreeReporter noOpSimpleTreeReporter() {
        return new NoOpSimpleTreeReporter();
    }

    public static SimpleTreeReporter compositeSimpleTreeReporter(SimpleTreeReporter... reporters) {
        return new CompositeSimpleTreeReporter(asList(reporters));
    }

    public static SimpleTreeReporter compositeSimpleTreeReporter(Iterable<SimpleTreeReporter> reporters) {
        return new CompositeSimpleTreeReporter(reporters);
    }

    public static CloseableSafeTreeReporter mergingReporter(SafeTreeReporter wrappedSafeTreeReporter) {
        return new MergingReporter(wrappedSafeTreeReporter);
    }
}
