package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

class MergingReporter implements CloseableSafeTreeReporter {
    protected final SafeTreeReporter reporter;
    private final Map<Node, Collection<Consumer<SafeTreeReporter>>> nodes = new LinkedHashMap<>();

    MergingReporter(SafeTreeReporter reporter) {
        this.reporter = reporter;
    }

    @Override
    public void close() {
        for (Map.Entry<Node, Collection<Consumer<SafeTreeReporter>>> entry : nodes.entrySet()) {
            entry.getKey().addToReporter(
                    reporter,
                    r -> {  // TODO: сделать лямбду классом
                        try (CloseableSafeTreeReporter mergingReporter = Reporters.mergingReporter(r)) {
                            for (Consumer<SafeTreeReporter> contents : entry.getValue()) {
                                contents.accept(mergingReporter);
                            }
                        }
                    }
            );
        }
        nodes.clear();
    }

    @Override
    public void presentNode(Key key, Object value, Consumer<SafeTreeReporter> contents) {
        nodes.computeIfAbsent(new PresentNode(key, value), k -> new ArrayList<>()).add(contents);
    }

    @Override
    public void missingNode(Key key, Consumer<SafeTreeReporter> contents) {
        nodes.computeIfAbsent(new MissingNode(key), k -> new ArrayList<>()).add(contents);
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
    public void correctlyMissing() {
        reporter.correctlyMissing();
    }

    @Override
    public void incorrectlyPresent() {
        reporter.incorrectlyPresent();
    }

    @Override
    public void incorrectlyMissing() {
        reporter.incorrectlyMissing();
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
    public void checkForMissingItem(String description) {
        reporter.checkForMissingItem(description);
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
            return key.equals(that.key) && value.equals(that.value);

        }

        @Override
        public int hashCode() {
            int result = key.hashCode();
            result = 31 * result + value.hashCode();
            return result;
        }
    }


    static class MissingNode implements Node {
        final Key key;

        MissingNode(Key key) {
            this.key = key;
        }

        @Override
        public void addToReporter(SafeTreeReporter safeTreeReporter, Consumer<SafeTreeReporter> contents) {
            safeTreeReporter.missingNode(key, contents);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MissingNode that = (MissingNode) o;
            return key.equals(that.key);
        }

        @Override
        public int hashCode() {
            return key.hashCode();
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
            return key.equals(that.key) && throwable.equals(that.throwable);
        }

        @Override
        public int hashCode() {
            int result = key.hashCode();
            result = 31 * result + throwable.hashCode();
            return result;
        }
    }
}
