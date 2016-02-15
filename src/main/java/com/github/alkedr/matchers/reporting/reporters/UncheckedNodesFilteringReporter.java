package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.util.ArrayList;
import java.util.List;

// непроверенные значения - present node и absent node без содержимого
class UncheckedNodesFilteringReporter implements SimpleTreeReporter {
    private final SimpleTreeReporter next;
    private final List<NodeStarter> nodesStack = new ArrayList<>();

    UncheckedNodesFilteringReporter(SimpleTreeReporter next) {
        this.next = next;
    }

    @Override
    public void beginPresentNode(Key key, Object value) {
        rememberNode(reporter -> reporter.beginPresentNode(key, value));
    }

    @Override
    public void beginAbsentNode(Key key) {
        rememberNode(reporter -> reporter.beginAbsentNode(key));
    }

    @Override
    public void beginBrokenNode(Key key, Throwable throwable) {
        rememberNode(reporter -> reporter.beginBrokenNode(key, throwable));
        rememberThatCurrentNodeHasContent();
    }

    @Override
    public void endNode() {
        if (nodesStack.isEmpty()) {
            next.endNode();
        } else {
            nodesStack.remove(nodesStack.size() - 1);
        }
    }

    @Override
    public void correctlyPresent() {
        rememberThatCurrentNodeHasContent();
        next.correctlyPresent();
    }

    @Override
    public void correctlyAbsent() {
        rememberThatCurrentNodeHasContent();
        next.correctlyAbsent();
    }

    @Override
    public void incorrectlyPresent() {
        rememberThatCurrentNodeHasContent();
        next.incorrectlyPresent();
    }

    @Override
    public void incorrectlyAbsent() {
        rememberThatCurrentNodeHasContent();
        next.incorrectlyAbsent();
    }

    @Override
    public void passedCheck(String description) {
        rememberThatCurrentNodeHasContent();
        next.passedCheck(description);
    }

    @Override
    public void failedCheck(String expected, String actual) {
        rememberThatCurrentNodeHasContent();
        next.failedCheck(expected, actual);
    }

    @Override
    public void checkForAbsentItem(String description) {
        rememberThatCurrentNodeHasContent();
        next.checkForAbsentItem(description);
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
        rememberThatCurrentNodeHasContent();
        next.brokenCheck(description, throwable);
    }


    private void rememberNode(NodeStarter nodeStarter) {
        nodesStack.add(nodeStarter);
    }

    private void rememberThatCurrentNodeHasContent() {
        for (NodeStarter nodeStarter : nodesStack) {
            nodeStarter.start(next);
        }
        nodesStack.clear();
    }

    private interface NodeStarter {
        void start(SimpleTreeReporter simpleTreeReporter);
    }
}
