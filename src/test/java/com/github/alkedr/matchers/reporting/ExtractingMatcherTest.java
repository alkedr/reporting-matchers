package com.github.alkedr.matchers.reporting;

// TODO: протестировать merge!
@Deprecated
public class ExtractingMatcherTest {
    /*private final SimpleTreeReporter simpleTreeReporter = mock(SimpleTreeReporter.class);
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
        value(presentKey).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key), same(value));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void run_missing_noName_noMatcher() {
        value(missingKey).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void run_broken_noName_noMatcher() {
        value(brokenKey).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key), same(throwable));
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void runForMissingItem_present_noName_noMatcher() {
        value(presentKey).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key), same(value));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_missing_noName_noMatcher() {
        value(missingKey).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_broken_noName_noMatcher() {
        value(brokenKey).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key), same(throwable));
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void run_present_nameInDisplayedAs_noMatcher() {
        value(presentKey).displayedAs("123").run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(eq(renamedKey(key, "123")), same(value));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void run_missing_nameInDisplayedAs_noMatcher() {
        value(missingKey).displayedAs("123").run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginMissingNode(eq(renamedKey(key, "123")));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void run_broken_nameInDisplayedAs_noMatcher() {
        value(brokenKey).displayedAs("123").run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(eq(renamedKey(key, "123")), same(throwable));
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void runForMissingItem_present_nameInDisplayedAs_noMatcher() {
        value(presentKey).displayedAs("123").runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(eq(renamedKey(key, "123")), same(value));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_missing_nameInDisplayedAs_noMatcher() {
        value(missingKey).displayedAs("123").runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginMissingNode(eq(renamedKey(key, "123")));
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_broken_nameInDisplayedAs_noMatcher() {
        value(brokenKey).displayedAs("123").runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(eq(renamedKey(key, "123")), same(throwable));
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void run_present_noName_isValue() {
        value(presentKey).is(1).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key), same(value));
        inOrder.verify(simpleTreeReporter).passedCheck("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void run_missing_noName_isValue() {
        value(missingKey).is(1).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key));
        inOrder.verify(simpleTreeReporter).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void run_broken_noName_isValue() {
        value(brokenKey).is(1).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key), same(throwable));
        inOrder.verify(simpleTreeReporter).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void runForMissingItem_present_noName_isValue() {
        value(presentKey).is(1).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key), same(value));
        inOrder.verify(simpleTreeReporter).passedCheck("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_missing_noName_isValue() {
        value(missingKey).is(1).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key));
        inOrder.verify(simpleTreeReporter).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_broken_noName_isValue() {
        value(brokenKey).is(1).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key), same(throwable));
        inOrder.verify(simpleTreeReporter).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void run_present_noName_arrayOfMatchers() {
        value(presentKey).is(equalTo(1), equalTo(1)).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key), same(value));
        inOrder.verify(simpleTreeReporter, times(2)).passedCheck("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void run_missing_noName_arrayOfMatchers() {
        value(missingKey).is(equalTo(1), equalTo(1)).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key));
        inOrder.verify(simpleTreeReporter, times(2)).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void run_broken_noName_arrayOfMatchers() {
        value(brokenKey).is(equalTo(1), equalTo(1)).run(item, simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key), same(throwable));
        inOrder.verify(simpleTreeReporter, times(2)).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }


    @Test
    public void runForMissingItem_present_noName_arrayOfMatchers() {
        value(presentKey).is(equalTo(1), equalTo(1)).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key), same(value));
        inOrder.verify(simpleTreeReporter, times(2)).passedCheck("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_missing_noName_arrayOfMatchers() {
        value(missingKey).is(equalTo(1), equalTo(1)).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key));
        inOrder.verify(simpleTreeReporter, times(2)).checkForMissingItem("<1>");
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void runForMissingItem_broken_noName_arrayOfMatchers() {
        value(brokenKey).is(equalTo(1), equalTo(1)).runForMissingItem(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
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
    }*/
}
