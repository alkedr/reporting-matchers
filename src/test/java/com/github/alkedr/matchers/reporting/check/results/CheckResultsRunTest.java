package com.github.alkedr.matchers.reporting.check.results;

import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.keys.Keys;
import com.github.alkedr.matchers.reporting.reporters.Reporter;
import org.junit.After;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.Collections;

import static com.github.alkedr.matchers.reporting.check.results.CheckResults.*;
import static com.google.common.collect.Iterators.forArray;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class CheckResultsRunTest {
    private final Reporter reporter = mock(Reporter.class);
    private final InOrder inOrder = inOrder(reporter);
    private final Key key = Keys.elementKey(0);

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void correctlyPresentTest() {
        correctlyPresent().run(reporter);
        inOrder.verify(reporter).correctlyPresent();
    }

    @Test
    public void correctlyMissingTest() {
        correctlyMissing().run(reporter);
        inOrder.verify(reporter).correctlyMissing();
    }

    @Test
    public void incorrectlyPresentTest() {
        incorrectlyPresent().run(reporter);
        inOrder.verify(reporter).incorrectlyPresent();
    }

    @Test
    public void incorrectlyMissingTest() {
        incorrectlyMissing().run(reporter);
        inOrder.verify(reporter).incorrectlyMissing();
    }

    @Test
    public void passedMatcherTest() {
        passedMatcher("1").run(reporter);
        inOrder.verify(reporter).passedCheck("1");
    }

    @Test
    public void failedMatcherTest() {
        failedMatcher("1", "2").run(reporter);
        inOrder.verify(reporter).failedCheck("1", "2");
    }

    @Test
    public void brokenMatcherTest() {
        Throwable throwable = new RuntimeException();
        brokenMatcher("1", throwable).run(reporter);
        inOrder.verify(reporter).brokenCheck("1", throwable);
    }

    @Test
    public void matcherForMissingItemTest() {
        matcherForMissingItem("1").run(reporter);
        inOrder.verify(reporter).checkForMissingItem("1");
    }

    @Test
    public void presentSubValueTest() {
        Object value = new Object();
        presentSubValue(key, value, forArray(passedMatcher("1"))).run(reporter);
        inOrder.verify(reporter).beginNode(key.asString(), value);
        inOrder.verify(reporter).passedCheck("1");
        inOrder.verify(reporter).endNode();
    }

    @Test
    public void missingSubValueTest() {
        missingSubValue(key, forArray(passedMatcher("1"))).run(reporter);
        inOrder.verify(reporter).beginMissingNode(key.asString());
        inOrder.verify(reporter).passedCheck("1");
        inOrder.verify(reporter).endNode();
    }

    @Test
    public void brokenSubValueTest() {
        Throwable throwable = new RuntimeException();
        brokenSubValue(key, throwable, forArray(passedMatcher("1"))).run(reporter);
        inOrder.verify(reporter).beginBrokenNode(key.asString(), throwable);
        inOrder.verify(reporter).passedCheck("1");
        inOrder.verify(reporter).endNode();
    }

    @Test
    public void sequenceOfMergedSubValuesTest() {
        sequenceOfMergedSubValues(forArray(
                missingSubValue(key, forArray(passedMatcher("1"))),
                missingSubValue(key, forArray(passedMatcher("2")))
        )).run(reporter);
        inOrder.verify(reporter).beginMissingNode(key.asString());
        inOrder.verify(reporter).passedCheck("1");
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginMissingNode(key.asString());
        inOrder.verify(reporter).passedCheck("2");
        inOrder.verify(reporter).endNode();
    }


    @Test
    public void run_zeroCheckResults() {
        runAll(Collections.emptyIterator(), reporter);
    }

    @Test
    public void run_oneCheckResult() {
        runAll(singletonList(passedMatcher("1")).iterator(), reporter);
        inOrder.verify(reporter).passedCheck("1");
    }

    @Test
    public void run_twoCheckResults() {
        runAll(asList(passedMatcher("1"), passedMatcher("2")).iterator(), reporter);
        inOrder.verify(reporter).passedCheck("1");
        inOrder.verify(reporter).passedCheck("2");
    }
}
