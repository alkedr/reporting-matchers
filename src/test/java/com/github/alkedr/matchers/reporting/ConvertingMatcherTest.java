package com.github.alkedr.matchers.reporting;

public class ConvertingMatcherTest {
    /*private final SafeTreeReporter reporter = mock(SafeTreeReporter.class);
    private final InOrder inOrder = inOrder(reporter);
    private final ReportingMatcher<String> convertingMatcher =
            new ConvertingMatcher<>(Integer::parseInt, toReportingMatcher(CoreMatchers.equalTo(1)));

    @Test
    public void run_passed() {
        convertingMatcher.run("1", reporter);
        inOrder.verify(reporter).passedCheck("<1>");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void run_failed() {
        convertingMatcher.run("2", reporter);
        inOrder.verify(reporter).failedCheck("<1>", "was <2>");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void run_throwingConverter() {
        convertingMatcher.run("qwe", reporter);
        inOrder.verify(reporter).brokenCheck(eq("Ошибка при преобразовании"), isA(NumberFormatException.class));
        inOrder.verify(reporter).checkForMissingItem("<1>");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void run_wrongItemType() {
        convertingMatcher.run(1, reporter);
        inOrder.verify(reporter).brokenCheck(eq("Ошибка при преобразовании"), isA(ClassCastException.class));
        inOrder.verify(reporter).checkForMissingItem("<1>");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void runForMissingItem() {
        missing().runForMissingItem(reporter);
        inOrder.verify(reporter).correctlyMissing();
        inOrder.verifyNoMoreInteractions();
    }*/
}
