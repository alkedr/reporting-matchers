package com.github.alkedr.matchers.reporting.extraction;

public class ExtractingMatcherTest {
    /*private final Object item = new Object();
    private final ExtractingMatcher.Extractor extractor = mock(ExtractingMatcher.Extractor.class);
    private final ReportingMatcher.Key key = mock(ReportingMatcher.Key.class);
    private final ReportingMatcher.Value value1 = present(new Object(), "1");
    private final ReportingMatcher.Value value2 = present(new Object(), "2");
    private final ReportingMatcher.CheckListener checkListener = mock(ReportingMatcher.CheckListener.class);

    @Before
    public void setUp() {
        when(extractor.extractFrom(item)).thenReturn(new ExtractingMatcher.KeyValue(key, value1));
        when(extractor.extractFromMissingItem()).thenReturn(new ExtractingMatcher.KeyValue(key, value2));
    }


    @Test
    public void run() {
        new ExtractingMatcher<>(null, extractor, null).run(item, checkListener);
        CheckListenerUtils.verifyKvcGroup(checkListener, CheckListenerUtils.kvc(key, value1, DEFAULT_CHECKS));
        verifyNoMoreInteractions(checkListener);
    }

    @Test
    public void runForMissingItem() {
        new ExtractingMatcher<>(null, extractor, null).runForMissingItem(checkListener);
        CheckListenerUtils.verifyKvcGroup(checkListener, CheckListenerUtils.kvc(key, value2, DEFAULT_CHECKS));
        verifyNoMoreInteractions(checkListener);
    }


    @Test
    public void nameInConstructor() {
        String customName = "12345";
        new ExtractingMatcher<>(customName, extractor, null).runForMissingItem(checkListener);
        CheckListenerUtils.verifyKvcGroup(checkListener, CheckListenerUtils.kvc(renamedKey(customName, key), sameInstance(value2), sameInstance(DEFAULT_CHECKS)));
        verifyNoMoreInteractions(checkListener);
    }

    @Test
    public void checksInConstructor() {
        ReportingMatcher.Checks customChecks = new ReportingMatcher.Checks(MISSING, noOp());
        new ExtractingMatcher<>(null, extractor, customChecks).runForMissingItem(checkListener);
        CheckListenerUtils.verifyKvcGroup(checkListener, CheckListenerUtils.kvc(key, value2, customChecks));
        verifyNoMoreInteractions(checkListener);
    }


    // TODO: тесты на fluent API
    // TODO: тесты на RenamedKey


    public static Matcher<ReportingMatcher.Key> renamedKey(String customName, ReportingMatcher.Key key) {
        return new CustomMatcher<ReportingMatcher.Key>("RenamedKey{" + customName + ", " + key + "}") {
            @Override
            public boolean matches(Object item) {
                return item instanceof ExtractingMatcher.RenamedKey
                        && ((ExtractingMatcher.RenamedKey) item).name == customName
                        && ((ExtractingMatcher.RenamedKey) item).key == key
                        ;
            }
        };
    }*/
}
