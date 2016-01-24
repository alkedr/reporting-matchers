package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MergingReporter extends ReporterWrapper implements Closeable {
    private final Map<Node, Collection<Consumer<Reporter>>> nodes = new LinkedHashMap<>();

    public MergingReporter(Reporter wrappedReporter) {
        super(wrappedReporter);
    }

    @Override
    public void close() {
        for (Map.Entry<Node, Collection<Consumer<Reporter>>> entry : nodes.entrySet()) {
            entry.getKey().addToReporter(
                    wrappedReporter,
                    r -> {  // TODO: сделать лямбду классом
                        try (MergingReporter mergingReporter = new MergingReporter(r)) {
                            for (Consumer<Reporter> contents : entry.getValue()) {
                                contents.accept(mergingReporter);
                            }
                        }
                    }
            );
        }
        nodes.clear();
    }

    @Override
    public void presentNode(Key key, Object value, Consumer<Reporter> contents) {
        nodes.computeIfAbsent(new PresentNode(key, value), k -> new ArrayList<>()).add(contents);
    }

    @Override
    public void missingNode(Key key, Consumer<Reporter> contents) {
        nodes.computeIfAbsent(new MissingNode(key), k -> new ArrayList<>()).add(contents);
    }

    @Override
    public void brokenNode(Key key, Throwable throwable, Consumer<Reporter> contents) {
        nodes.computeIfAbsent(new BrokenNode(key, throwable), k -> new ArrayList<>()).add(contents);
    }


    interface Node {
        void addToReporter(Reporter reporter, Consumer<Reporter> contents);
    }


    static class PresentNode implements Node {
        final Key key;
        final Object value;   // TODO: сравнивать по == ?

        PresentNode(Key key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public void addToReporter(Reporter reporter, Consumer<Reporter> contents) {
            reporter.presentNode(key, value, contents);
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
        public void addToReporter(Reporter reporter, Consumer<Reporter> contents) {
            reporter.missingNode(key, contents);
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
        public void addToReporter(Reporter reporter, Consumer<Reporter> contents) {
            reporter.brokenNode(key, throwable, contents);
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
