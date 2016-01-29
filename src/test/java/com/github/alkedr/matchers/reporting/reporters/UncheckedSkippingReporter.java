package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.util.ArrayDeque;
import java.util.Deque;

@Deprecated
public class UncheckedSkippingReporter implements SimpleTreeReporter {
    private final SimpleTreeReporter simpleTreeReporter;
    private final Deque<Node> deque = new ArrayDeque<>();

    public UncheckedSkippingReporter(SimpleTreeReporter simpleTreeReporter) {
        this.simpleTreeReporter = simpleTreeReporter;
    }

    @Override
    public void beginPresentNode(Key key, Object value) {
        deque.add(new PresentNode(key, value));
    }

    @Override
    public void beginAbsentNode(Key key) {
        deque.add(new AbsentNode(key));
    }

    @Override
    public void beginBrokenNode(Key key, Throwable throwable) {
        deque.add(new BrokenNode(key, throwable));
    }

    @Override
    public void endNode() {
        deque.pop().endIfNecessary(simpleTreeReporter);
    }

    @Override
    public void correctlyPresent() {
        callBeginIfNecessary();
        simpleTreeReporter.correctlyPresent();
    }

    @Override
    public void correctlyAbsent() {
        callBeginIfNecessary();
        simpleTreeReporter.correctlyAbsent();
    }

    @Override
    public void incorrectlyPresent() {
        callBeginIfNecessary();
        simpleTreeReporter.incorrectlyPresent();
    }

    @Override
    public void incorrectlyAbsent() {
        callBeginIfNecessary();
        simpleTreeReporter.incorrectlyAbsent();
    }

    @Override
    public void passedCheck(String description) {
        callBeginIfNecessary();
        simpleTreeReporter.passedCheck(description);
    }

    @Override
    public void failedCheck(String expected, String actual) {
        callBeginIfNecessary();
        simpleTreeReporter.failedCheck(expected, actual);
    }

    @Override
    public void checkForAbsentItem(String description) {
        callBeginIfNecessary();
        simpleTreeReporter.checkForAbsentItem(description);
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
        callBeginIfNecessary();
        simpleTreeReporter.brokenCheck(description, throwable);
    }


    private void callBeginIfNecessary() {
        if (!deque.isEmpty()) {
            deque.peek().beginIfNecessary(simpleTreeReporter);
        }
    }


    private interface Node {
        void beginIfNecessary(SimpleTreeReporter simpleTreeReporter);
        void endIfNecessary(SimpleTreeReporter simpleTreeReporter);
    }

    private abstract static class AbstractNode implements Node {
        private boolean calledBegin = false;

        protected abstract void begin(SimpleTreeReporter simpleTreeReporter);

        @Override
        public void beginIfNecessary(SimpleTreeReporter simpleTreeReporter) {
            if (!calledBegin) {
                begin(simpleTreeReporter);
                calledBegin = true;
            }
        }

        @Override
        public void endIfNecessary(SimpleTreeReporter simpleTreeReporter) {
            if (calledBegin) {
                simpleTreeReporter.endNode();
            }
        }
    }

    private static class PresentNode extends AbstractNode {
        private final Key key;
        private final Object value;

        private PresentNode(Key key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        protected void begin(SimpleTreeReporter simpleTreeReporter) {
            simpleTreeReporter.beginPresentNode(key, value);
        }
    }

    private static class AbsentNode extends AbstractNode {
        private final Key key;

        private AbsentNode(Key key) {
            this.key = key;
        }

        @Override
        protected void begin(SimpleTreeReporter simpleTreeReporter) {
            simpleTreeReporter.beginAbsentNode(key);
        }
    }

    private static class BrokenNode extends AbstractNode {
        private final Key key;
        private final Throwable throwable;

        private BrokenNode(Key key, Throwable throwable) {
            this.key = key;
            this.throwable = throwable;
        }

        @Override
        protected void begin(SimpleTreeReporter simpleTreeReporter) {
            simpleTreeReporter.beginBrokenNode(key, throwable);
        }
    }
}
