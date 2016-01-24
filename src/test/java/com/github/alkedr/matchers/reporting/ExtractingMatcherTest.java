package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.keys.ExtractableKey;
import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.SimpleTreeReporter;
import org.junit.After;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatcher;
import static com.github.alkedr.matchers.reporting.keys.Keys.renamedKey;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.anything;
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
    private final Key key1 = mock(Key.class);
    private final Key key2 = mock(Key.class);
    private final Key key3 = mock(Key.class);
    private final Throwable throwable = new RuntimeException();
    private final ExtractableKey extractor = new ExtractableKey() {
        @Override
        public String asString() {
            return null;
        }

        @Override
        public void extractFrom(Object item, ResultListener result) {
            result.present(key1, 1);
            result.missing(key2);
            result.broken(key3, throwable);
        }

        @Override
        public void extractFromMissingItem(ResultListener result) {
            result.broken(key3, throwable);
            result.missing(key2);
            result.present(key1, 1);
        }
    };

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void run_noName_noMatcher() {
        new ExtractingMatcher<>(extractor).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key1), eq(1));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key2));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key3), same(throwable));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_noName_noMatcher() {
        new ExtractingMatcher<>(extractor).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key3), same(throwable));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key2));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key1), eq(1));
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void run_nameInConstructor_noMatcher() {
        new ExtractingMatcher<>(extractor, "123").run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(eq(renamedKey(key1, "123")), eq(1));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginMissingNode(eq(renamedKey(key2, "123")));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginBrokenNode(eq(renamedKey(key3, "123")), same(throwable));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_nameInConstructor_noMatcher() {
        new ExtractingMatcher<>(extractor, "123").runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(eq(renamedKey(key3, "123")), same(throwable));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginMissingNode(eq(renamedKey(key2, "123")));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginPresentNode(eq(renamedKey(key1, "123")), eq(1));
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void run_nameInDisplayedAs_noMatcher() {
        new ExtractingMatcher<>(extractor).displayedAs("123").run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(eq(renamedKey(key1, "123")), eq(1));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginMissingNode(eq(renamedKey(key2, "123")));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginBrokenNode(eq(renamedKey(key3, "123")), same(throwable));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_nameInDisplayedAs_noMatcher() {
        new ExtractingMatcher<>(extractor).displayedAs("123").runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(eq(renamedKey(key3, "123")), same(throwable));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginMissingNode(eq(renamedKey(key2, "123")));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginPresentNode(eq(renamedKey(key1, "123")), eq(1));
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void run_noName_matcherInConstructor() {
        new ExtractingMatcher<>(extractor, null, toReportingMatcher(anything("1"))).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key1), eq(1));
        inOrder.verify(simpleTreeReporter).passedCheck("1");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key2));
        inOrder.verify(simpleTreeReporter).checkForMissingItem("1");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key3), same(throwable));
        inOrder.verify(simpleTreeReporter).checkForMissingItem("1");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_noName_matcherInConstructor() {
        new ExtractingMatcher<>(extractor, null, toReportingMatcher(anything("1"))).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key3), same(throwable));
        inOrder.verify(simpleTreeReporter).checkForMissingItem("1");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key2));
        inOrder.verify(simpleTreeReporter).checkForMissingItem("1");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key1), eq(1));
        inOrder.verify(simpleTreeReporter).passedCheck("1");
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void run_noName_matcherInIsValue() {
        new ExtractingMatcher<>(extractor).is(1).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key1), eq(1));
        inOrder.verify(simpleTreeReporter).passedCheck("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key2));
        inOrder.verify(simpleTreeReporter).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key3), same(throwable));
        inOrder.verify(simpleTreeReporter).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_noName_matcherInIsValue() {
        new ExtractingMatcher<>(extractor).is(1).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key3), same(throwable));
        inOrder.verify(simpleTreeReporter).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key2));
        inOrder.verify(simpleTreeReporter).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key1), eq(1));
        inOrder.verify(simpleTreeReporter).passedCheck("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void run_noName_matcherInIsArrayOfMatchers() {
        new ExtractingMatcher<>(extractor).is(equalTo(1), equalTo(1)).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key1), eq(1));
        inOrder.verify(simpleTreeReporter, times(2)).passedCheck("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key2));
        inOrder.verify(simpleTreeReporter, times(2)).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key3), same(throwable));
        inOrder.verify(simpleTreeReporter, times(2)).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_noName_matcherInIsArrayOfMatchers() {
        new ExtractingMatcher<>(extractor).is(equalTo(1), equalTo(1)).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key3), same(throwable));
        inOrder.verify(simpleTreeReporter, times(2)).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key2));
        inOrder.verify(simpleTreeReporter, times(2)).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key1), eq(1));
        inOrder.verify(simpleTreeReporter, times(2)).passedCheck("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }
}
