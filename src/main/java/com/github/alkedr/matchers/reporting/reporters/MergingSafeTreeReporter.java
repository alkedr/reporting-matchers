package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.util.*;
import java.util.function.Consumer;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.mergingReporter;

class MergingSafeTreeReporter implements CloseableSafeTreeReporter {
    protected final SafeTreeReporter reporter;
    private final Map<Node, Collection<Consumer<SafeTreeReporter>>> nodes = new LinkedHashMap<>();

    MergingSafeTreeReporter(SafeTreeReporter reporter) {
        this.reporter = reporter;
    }

    @Override
    public void close() {
        for (Map.Entry<Node, Collection<Consumer<SafeTreeReporter>>> entry : nodes.entrySet()) {
            entry.getKey().addToReporter(reporter, new MergedNodeContents(entry.getValue()));
        }
        nodes.clear();
    }

    @Override
    public void presentNode(Key key, Object value, Consumer<SafeTreeReporter> contents) {
        nodes.computeIfAbsent(new PresentNode(key, value), k -> new ArrayList<>()).add(contents);
    }

    @Override
    public void absentNode(Key key, Consumer<SafeTreeReporter> contents) {
        nodes.computeIfAbsent(new AbsentNode(key), k -> new ArrayList<>()).add(contents);
    }

    @Override
    public void brokenNode(Key key, Throwable throwable, Consumer<SafeTreeReporter> contents) {
        nodes.computeIfAbsent(new BrokenNode(key, throwable), k -> new ArrayList<>()).add(contents);
    }

    @Override
    public void correctlyPresent() {
        reporter.correctlyPresent();
    }

    @Override
    public void correctlyAbsent() {
        reporter.correctlyAbsent();
    }

    @Override
    public void incorrectlyPresent() {
        reporter.incorrectlyPresent();
    }

    @Override
    public void incorrectlyAbsent() {
        reporter.incorrectlyAbsent();
    }

    @Override
    public void passedCheck(String description) {
        reporter.passedCheck(description);
    }

    @Override
    public void failedCheck(String expected, String actual) {
        reporter.failedCheck(expected, actual);
    }

    @Override
    public void checkForAbsentItem(String description) {
        reporter.checkForAbsentItem(description);
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
        reporter.brokenCheck(description, throwable);
    }


    interface Node {
        void addToReporter(SafeTreeReporter safeTreeReporter, Consumer<SafeTreeReporter> contents);
    }


    static class PresentNode implements Node {
        final Key key;
        final Object value;   // TODO: сравнивать по == ?

        PresentNode(Key key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public void addToReporter(SafeTreeReporter safeTreeReporter, Consumer<SafeTreeReporter> contents) {
            safeTreeReporter.presentNode(key, value, contents);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PresentNode that = (PresentNode) o;
            return Objects.equals(key, that.key) && Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }


    static class AbsentNode implements Node {
        final Key key;

        AbsentNode(Key key) {
            this.key = key;
        }

        @Override
        public void addToReporter(SafeTreeReporter safeTreeReporter, Consumer<SafeTreeReporter> contents) {
            safeTreeReporter.absentNode(key, contents);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AbsentNode that = (AbsentNode) o;
            return Objects.equals(key, that.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }


    static class BrokenNode implements Node {
        final Key key;
        final Throwable throwable;

        BrokenNode(Key key, Throwable throwable) {
            this.key = key;
            this.throwable = throwable;
        }

        @Override
        public void addToReporter(SafeTreeReporter safeTreeReporter, Consumer<SafeTreeReporter> contents) {
            safeTreeReporter.brokenNode(key, throwable, contents);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BrokenNode that = (BrokenNode) o;
            return Objects.equals(key, that.key) && Objects.equals(throwable, that.throwable);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, throwable);
        }
    }


    private static class MergedNodeContents implements Consumer<SafeTreeReporter> {
        private final Collection<Consumer<SafeTreeReporter>> contentsCollection;

        MergedNodeContents(Collection<Consumer<SafeTreeReporter>> contentsCollection) {
            this.contentsCollection = contentsCollection;
        }

        @Override
        public void accept(SafeTreeReporter reporter) {
            try (CloseableSafeTreeReporter mergingReporter = mergingReporter(reporter)) {
                for (Consumer<SafeTreeReporter> contents : contentsCollection) {
                    contents.accept(mergingReporter);
                }
            }
        }
    }
}
