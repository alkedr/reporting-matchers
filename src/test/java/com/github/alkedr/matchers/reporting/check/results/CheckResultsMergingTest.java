package com.github.alkedr.matchers.reporting.check.results;

import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.keys.Keys;
import com.github.alkedr.matchers.reporting.reporters.Reporter;
import org.junit.After;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.NoSuchElementException;

import static com.github.alkedr.matchers.reporting.check.results.CheckResults.*;
import static com.google.common.collect.Iterators.forArray;
import static java.util.Collections.emptyIterator;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

// TODO: больше тестов
public class CheckResultsMergingTest {
    private final Reporter reporter = mock(Reporter.class);
    private final InOrder inOrder = inOrder(reporter);
    private final Key key = Keys.elementKey(0);
    private final Key key2 = Keys.elementKey(1);

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void empty() {
        runAll(merge(forArray()), reporter);
    }

    @Test(expected = NoSuchElementException.class)
    public void empty_nextShouldThrow() {
        merge(forArray()).next();
    }

    @Test
    public void twoOfEveryCheckResultThatDoesNotNeedToBeMerged() {
        Throwable throwable = new RuntimeException();
        runAll(merge(forArray(
                correctlyPresent(),
                correctlyPresent(),
                correctlyMissing(),
                correctlyMissing(),
                incorrectlyPresent(),
                incorrectlyPresent(),
                incorrectlyMissing(),
                incorrectlyMissing(),
                passedMatcher("1"),
                passedMatcher("1"),
                failedMatcher("2", "3"),
                failedMatcher("2", "3"),
                brokenMatcher("4", throwable),
                brokenMatcher("4", throwable),
                matcherForMissingItem("5"),
                matcherForMissingItem("5")
        )), reporter);
        inOrder.verify(reporter, times(2)).correctlyPresent();
        inOrder.verify(reporter, times(2)).correctlyMissing();
        inOrder.verify(reporter, times(2)).incorrectlyPresent();
        inOrder.verify(reporter, times(2)).incorrectlyMissing();
        inOrder.verify(reporter, times(2)).passedCheck("1");
        inOrder.verify(reporter, times(2)).failedCheck("2", "3");
        inOrder.verify(reporter, times(2)).brokenCheck("4", throwable);
        inOrder.verify(reporter, times(2)).checkForMissingItem("5");
    }

    @Test
    public void oneCorrectlyPresentAndOneSubValue() {
        runAll(merge(forArray(
                correctlyPresent(),
                missingSubValue(key, emptyIterator())
        )), reporter);
        inOrder.verify(reporter).correctlyPresent();
        inOrder.verify(reporter).beginMissingNode(key.asString());
        inOrder.verify(reporter).endNode();
    }

    @Test
    public void oneOneMergedSubValueSequenceAndOneCorrectlyPresent() {
        runAll(merge(forArray(
                sequenceOfMergedSubValues(singletonList(
                        missingSubValue(key, emptyIterator())
                ).iterator()),
                correctlyPresent()
        )), reporter);
        inOrder.verify(reporter).correctlyPresent();
        inOrder.verify(reporter).beginMissingNode(key.asString());
        inOrder.verify(reporter).endNode();
    }

    @Test
    public void threeMergeableSubValueWithInnerCheckResultsThatNeedToBeMerged() {
        runAll(merge(forArray(
                missingSubValue(key, forArray(
                        missingSubValue(key2, forArray(
                                passedMatcher("1")
                        ))
                )),
                missingSubValue(key, forArray(
                        missingSubValue(key2, forArray(
                                passedMatcher("2")
                        ))
                )),
                sequenceOfMergedSubValues(forArray(
                        missingSubValue(key, forArray(
                                missingSubValue(key2, forArray(
                                        passedMatcher("3")
                                ))
                        ))
                ))
        )), reporter);
        inOrder.verify(reporter).beginMissingNode(key.asString());
        inOrder.verify(reporter).beginMissingNode(key2.asString());
        inOrder.verify(reporter).passedCheck("1");
        inOrder.verify(reporter).passedCheck("2");
        inOrder.verify(reporter).passedCheck("3");
        inOrder.verify(reporter, times(2)).endNode();
    }

    @Test
    public void twoSubValuesThatCanNotBeMerged() {
        runAll(merge(forArray(
                missingSubValue(key, forArray()),
                missingSubValue(key2, forArray())
        )), reporter);
        inOrder.verify(reporter).beginMissingNode(key.asString());
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginMissingNode(key2.asString());
        inOrder.verify(reporter).endNode();
    }
}
