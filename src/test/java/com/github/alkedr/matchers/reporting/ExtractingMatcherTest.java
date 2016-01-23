package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.extractors.Extractor;
import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.keys.RenamedKey;
import com.github.alkedr.matchers.reporting.reporters.Reporter;
import org.junit.After;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatcher;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class ExtractingMatcherTest {
    private final Reporter reporter = mock(Reporter.class);
    private final InOrder inOrder = inOrder(reporter);
    private final Object item = new Object();
    private final Key key1 = mock(Key.class);
    private final Key key2 = mock(Key.class);
    private final Key key3 = mock(Key.class);
    private final Throwable throwable = new RuntimeException();
    private final Extractor extractor = new Extractor() {
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
    private final ReportingMatcher<?> matcher = toReportingMatcher(anything("1"));

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    public void run_noName_noMatcher() {
        new ExtractingMatcher<>(extractor).run(item, reporter);
        inOrder.verify(reporter).beginNode(key1, 1);
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginMissingNode(key2);
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginBrokenNode(key3, throwable);
        inOrder.verify(reporter).endNode();
    }

    @Test
    public void runForMissingItem_noName_noMatcher() {
        new ExtractingMatcher<>(extractor).runForMissingItem(reporter);
        inOrder.verify(reporter).beginBrokenNode(key3, throwable);
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginMissingNode(key2);
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginNode(key1, 1);
        inOrder.verify(reporter).endNode();
    }


    @Test
    public void run_nameInConstructor_noMatcher() {
        new ExtractingMatcher<>(extractor, "123").run(item, reporter);
        inOrder.verify(reporter).beginNode(new RenamedKey<>(key1, "123"), 1);
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginMissingNode(new RenamedKey<>(key2, "123"));
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginBrokenNode(new RenamedKey<>(key3, "123"), throwable);
        inOrder.verify(reporter).endNode();
    }

    @Test
    public void runForMissingItem_nameInConstructor_noMatcher() {
        new ExtractingMatcher<>(extractor, "123").runForMissingItem(reporter);
        inOrder.verify(reporter).beginBrokenNode(new RenamedKey<>(key3, "123"), throwable);
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginMissingNode(new RenamedKey<>(key2, "123"));
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginNode(new RenamedKey<>(key1, "123"), 1);
        inOrder.verify(reporter).endNode();
    }


    @Test
    public void run_nameInDisplayedAs_noMatcher() {
        new ExtractingMatcher<>(extractor).displayedAs("123").run(item, reporter);
        inOrder.verify(reporter).beginNode(new RenamedKey<>(key1, "123"), 1);
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginMissingNode(new RenamedKey<>(key2, "123"));
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginBrokenNode(new RenamedKey<>(key3, "123"), throwable);
        inOrder.verify(reporter).endNode();
    }

    @Test
    public void runForMissingItem_nameInDisplayedAs_noMatcher() {
        new ExtractingMatcher<>(extractor).displayedAs("123").runForMissingItem(reporter);
        inOrder.verify(reporter).beginBrokenNode(new RenamedKey<>(key3, "123"), throwable);
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginMissingNode(new RenamedKey<>(key2, "123"));
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginNode(new RenamedKey<>(key1, "123"), 1);
        inOrder.verify(reporter).endNode();
    }


    @Test
    public void run_noName_matcherInConstructor() {
        new ExtractingMatcher<>(extractor, null, toReportingMatcher(anything("1"))).run(item, reporter);
        inOrder.verify(reporter).beginNode(key1, 1);
        inOrder.verify(reporter).passedCheck("1");
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginMissingNode(key2);
        inOrder.verify(reporter).checkForMissingItem("1");
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginBrokenNode(key3, throwable);
        inOrder.verify(reporter).checkForMissingItem("1");
        inOrder.verify(reporter).endNode();
    }

    @Test
    public void runForMissingItem_noName_matcherInConstructor() {
        new ExtractingMatcher<>(extractor, null, toReportingMatcher(anything("1"))).runForMissingItem(reporter);
        inOrder.verify(reporter).beginBrokenNode(key3, throwable);
        inOrder.verify(reporter).checkForMissingItem("1");
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginMissingNode(key2);
        inOrder.verify(reporter).checkForMissingItem("1");
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginNode(key1, 1);
        inOrder.verify(reporter).passedCheck("1");
        inOrder.verify(reporter).endNode();
    }


    @Test
    public void run_noName_matcherInIsValue() {
        new ExtractingMatcher<>(extractor).is(1).run(item, reporter);
        inOrder.verify(reporter).beginNode(key1, 1);
        inOrder.verify(reporter).passedCheck("<1>");
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginMissingNode(key2);
        inOrder.verify(reporter).checkForMissingItem("<1>");
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginBrokenNode(key3, throwable);
        inOrder.verify(reporter).checkForMissingItem("<1>");
        inOrder.verify(reporter).endNode();
    }

    @Test
    public void runForMissingItem_noName_matcherInIsValue() {
        new ExtractingMatcher<>(extractor).is(1).runForMissingItem(reporter);
        inOrder.verify(reporter).beginBrokenNode(key3, throwable);
        inOrder.verify(reporter).checkForMissingItem("<1>");
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginMissingNode(key2);
        inOrder.verify(reporter).checkForMissingItem("<1>");
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginNode(key1, 1);
        inOrder.verify(reporter).passedCheck("<1>");
        inOrder.verify(reporter).endNode();
    }


    @Test
    public void run_noName_matcherInIsArrayOfMatchers() {
        new ExtractingMatcher<>(extractor).is(equalTo(1), equalTo(1)).run(item, reporter);
        inOrder.verify(reporter).beginNode(key1, 1);
        inOrder.verify(reporter, times(2)).passedCheck("<1>");
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginMissingNode(key2);
        inOrder.verify(reporter, times(2)).checkForMissingItem("<1>");
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginBrokenNode(key3, throwable);
        inOrder.verify(reporter, times(2)).checkForMissingItem("<1>");
        inOrder.verify(reporter).endNode();
    }

    @Test
    public void runForMissingItem_noName_matcherInIsArrayOfMatchers() {
        new ExtractingMatcher<>(extractor).is(equalTo(1), equalTo(1)).runForMissingItem(reporter);
        inOrder.verify(reporter).beginBrokenNode(key3, throwable);
        inOrder.verify(reporter, times(2)).checkForMissingItem("<1>");
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginMissingNode(key2);
        inOrder.verify(reporter, times(2)).checkForMissingItem("<1>");
        inOrder.verify(reporter).endNode();
        inOrder.verify(reporter).beginNode(key1, 1);
        inOrder.verify(reporter, times(2)).passedCheck("<1>");
        inOrder.verify(reporter).endNode();
    }


    // TODO: протестировать merge!
}
