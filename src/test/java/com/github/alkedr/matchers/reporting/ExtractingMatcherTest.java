package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.keys.ExtractableKey;
import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.SimpleTreeReporter;
import org.junit.After;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.keys.Keys.renamedKey;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

// TODO: протестировать merge!
public class ExtractingMatcherTest {
    private final SimpleTreeReporter simpleTreeReporter = mock(SimpleTreeReporter.class);
    private final InOrder inOrder = inOrder(simpleTreeReporter);
    private final Object item = new Object();
    private final Key key = mock(Key.class);
    private final Object value = 1;
    private final Throwable throwable = new RuntimeException();
    private final ExtractableKey presentKey = new PresentExtractableKey();
    private final ExtractableKey missingKey = new MissingExtractableKey();
    private final ExtractableKey brokenKey = new BrokenExtractableKey();

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    public void run_present_noName_noMatcher() {
        new ExtractingMatcher<>(presentKey).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key), same(value));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void run_missing_noName_noMatcher() {
        new ExtractingMatcher<>(missingKey).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void run_broken_noName_noMatcher() {
        new ExtractingMatcher<>(brokenKey).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key), same(throwable));
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void runForMissingItem_present_noName_noMatcher() {
        new ExtractingMatcher<>(presentKey).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key), same(value));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_missing_noName_noMatcher() {
        new ExtractingMatcher<>(missingKey).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_broken_noName_noMatcher() {
        new ExtractingMatcher<>(brokenKey).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key), same(throwable));
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void run_present_nameInDisplayedAs_noMatcher() {
        new ExtractingMatcher<>(presentKey).displayedAs("123").run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(eq(renamedKey(key, "123")), same(value));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void run_missing_nameInDisplayedAs_noMatcher() {
        new ExtractingMatcher<>(missingKey).displayedAs("123").run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginMissingNode(eq(renamedKey(key, "123")));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void run_broken_nameInDisplayedAs_noMatcher() {
        new ExtractingMatcher<>(brokenKey).displayedAs("123").run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(eq(renamedKey(key, "123")), same(throwable));
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void runForMissingItem_present_nameInDisplayedAs_noMatcher() {
        new ExtractingMatcher<>(presentKey).displayedAs("123").runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(eq(renamedKey(key, "123")), same(value));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_missing_nameInDisplayedAs_noMatcher() {
        new ExtractingMatcher<>(missingKey).displayedAs("123").runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginMissingNode(eq(renamedKey(key, "123")));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_broken_nameInDisplayedAs_noMatcher() {
        new ExtractingMatcher<>(brokenKey).displayedAs("123").runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(eq(renamedKey(key, "123")), same(throwable));
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void run_present_noName_isValue() {
        new ExtractingMatcher<>(presentKey).is(1).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key), same(value));
        inOrder.verify(simpleTreeReporter).passedCheck("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void run_missing_noName_isValue() {
        new ExtractingMatcher<>(missingKey).is(1).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key));
        inOrder.verify(simpleTreeReporter).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void run_broken_noName_isValue() {
        new ExtractingMatcher<>(brokenKey).is(1).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key), same(throwable));
        inOrder.verify(simpleTreeReporter).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void runForMissingItem_present_noName_isValue() {
        new ExtractingMatcher<>(presentKey).is(1).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key), same(value));
        inOrder.verify(simpleTreeReporter).passedCheck("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_missing_noName_isValue() {
        new ExtractingMatcher<>(missingKey).is(1).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key));
        inOrder.verify(simpleTreeReporter).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_broken_noName_isValue() {
        new ExtractingMatcher<>(brokenKey).is(1).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key), same(throwable));
        inOrder.verify(simpleTreeReporter).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void run_present_noName_arrayOfMatchers() {
        new ExtractingMatcher<>(presentKey).is(equalTo(1), equalTo(1)).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key), same(value));
        inOrder.verify(simpleTreeReporter, times(2)).passedCheck("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void run_missing_noName_arrayOfMatchers() {
        new ExtractingMatcher<>(missingKey).is(equalTo(1), equalTo(1)).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key));
        inOrder.verify(simpleTreeReporter, times(2)).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void run_broken_noName_arrayOfMatchers() {
        new ExtractingMatcher<>(brokenKey).is(equalTo(1), equalTo(1)).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key), same(throwable));
        inOrder.verify(simpleTreeReporter, times(2)).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void runForMissingItem_present_noName_arrayOfMatchers() {
        new ExtractingMatcher<>(presentKey).is(equalTo(1), equalTo(1)).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key), same(value));
        inOrder.verify(simpleTreeReporter, times(2)).passedCheck("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_missing_noName_arrayOfMatchers() {
        new ExtractingMatcher<>(missingKey).is(equalTo(1), equalTo(1)).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key));
        inOrder.verify(simpleTreeReporter, times(2)).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_broken_noName_arrayOfMatchers() {
        new ExtractingMatcher<>(brokenKey).is(equalTo(1), equalTo(1)).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key), same(throwable));
        inOrder.verify(simpleTreeReporter, times(2)).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }


    private class PresentExtractableKey implements ExtractableKey {
        @Override
        public ExtractionResult extractFrom(Object item) throws MissingException, BrokenException {
            assertSame(ExtractingMatcherTest.this.item, item);
            return extractFromMissingItem();
        }

        @Override
        public ExtractionResult extractFromMissingItem() throws MissingException, BrokenException {
            return new ExtractionResult(key, value);
        }

        @Override
        public String asString() {
            return null;
        }
    }


    private class MissingExtractableKey implements ExtractableKey {
        @Override
        public ExtractionResult extractFrom(Object item) throws MissingException, BrokenException {
            assertSame(ExtractingMatcherTest.this.item, item);
            return extractFromMissingItem();
        }

        @Override
        public ExtractionResult extractFromMissingItem() throws MissingException, BrokenException {
            throw new MissingException(key);
        }

        @Override
        public String asString() {
            return null;
        }
    }


    private class BrokenExtractableKey implements ExtractableKey {
        @Override
        public ExtractionResult extractFrom(Object item) throws MissingException, BrokenException {
            assertSame(ExtractingMatcherTest.this.item, item);
            return extractFromMissingItem();
        }

        @Override
        public ExtractionResult extractFromMissingItem() throws MissingException, BrokenException {
            throw new BrokenException(key, throwable);
        }

        @Override
        public String asString() {
            return null;
        }
    }
}
