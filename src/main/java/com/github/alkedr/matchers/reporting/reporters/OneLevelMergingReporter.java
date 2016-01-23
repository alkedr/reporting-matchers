package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.io.Closeable;
import java.util.LinkedHashMap;
import java.util.Map;

/*
Нельзя мёржить только один уровень иерархии, потому что даже если нижние уровни иерархии были помёржены,
они перестанут быть помёрженными после мёржа верхнего уровня иерархии,


Пример:

qwe:
    rty:
        matcher1
qwe:
    rty:
        matcher2


Мёрдж одного уровня:

qwe:
    rty:
        matcher1
    rty:
        matcher2


 */
public class OneLevelMergingReporter implements Reporter, Closeable {
    private final Reporter reporter;
    private final Map<Node, RecordingReporter> map = new LinkedHashMap<>();
    private int depth = 0;
    private RecordingReporter current;

    public OneLevelMergingReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    @Override
    public void beginNode(Key key, Object value) {
        if (depth == 0) {
            current = map.getOrDefault(new PresentNode(key, value), new RecordingReporter());
        } else {
            current.beginNode(key, value);
        }
        depth++;
    }

    @Override
    public void beginMissingNode(Key key) {
        if (depth == 0) {
            current = map.getOrDefault(new MissingNode(key), new RecordingReporter());
        } else {
            current.beginMissingNode(key);
        }
        depth++;
    }

    @Override
    public void beginBrokenNode(Key key, Throwable throwable) {
        if (depth == 0) {
            current = map.getOrDefault(new BrokenNode(key, throwable), new RecordingReporter());
        } else {
            current.beginBrokenNode(key, throwable);
        }
        depth++;
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

    @Override
    public void endNode() {
        depth--;
        if (depth != 0) {
            current.endNode();
        }
    }

    @Override
    public void close() {
        for (Map.Entry<Node, RecordingReporter> entry : map.entrySet()) {
            entry.getKey().begin(reporter);
            entry.getValue().playback(reporter);
            reporter.endNode();
        }
    }


    interface Node {
        void begin(Reporter reporter);
    }


    static class PresentNode implements Node {
        final Key key;
        final Object value;   // TODO: сравнивать по == ?

        PresentNode(Key key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public void begin(Reporter reporter) {
            reporter.beginNode(key, value);
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
        public void begin(Reporter reporter) {
            reporter.beginMissingNode(key);
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
        public void begin(Reporter reporter) {
            reporter.beginBrokenNode(key, throwable);
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
