package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.reporters.SimpleTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.checkers.SubValuesChecker;
import com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractor;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.subValuesMatcher;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SubValuesMatcherTest {
    private final SimpleTreeReporter simpleTreeReporter = mock(SimpleTreeReporter.class);
    private final SafeTreeReporter safeTreeReporter = simpleTreeReporterToSafeTreeReporter(simpleTreeReporter);
    private final InOrder inOrder = inOrder(simpleTreeReporter);

    @Test(expected = NullPointerException.class)
    public void null_subValuesExtractor() {
        new SubValuesMatcher<>(null, () -> null);
    }

    @Test(expected = NullPointerException.class)
    public void null_subValuesCheckerSupplier() {
        new SubValuesMatcher<>(mock(SubValuesExtractor.class), null);
    }

    @Test
    public void run_and_runForAbsentItem() {
        Object item = new Object();
        Object extractedItem = new Object();
        Throwable throwable = new RuntimeException();
        Key key = mock(Key.class);

        SubValuesChecker subValuesChecker = mock(SubValuesChecker.class);
        doAnswer(invocation -> {
            safeTreeReporter.passedCheck("1");
            return null;
        }).when(subValuesChecker).begin(same(safeTreeReporter));
        when(subValuesChecker.present(same(key), same(extractedItem))).thenReturn(str -> str.passedCheck("2"));
        when(subValuesChecker.absent(same(key))).thenReturn(str -> str.passedCheck("3"));
        when(subValuesChecker.broken(same(key), same(throwable))).thenReturn(str -> str.passedCheck("4"));
        doAnswer(invocation -> {
            safeTreeReporter.passedCheck("5");
            return null;
        }).when(subValuesChecker).end(same(safeTreeReporter));

        SubValuesExtractor<Object, Object> subValuesExtractor = mock(SubValuesExtractor.class);
        doAnswer(invocation -> {
            SubValuesExtractor.SubValuesListener<Object> svl = (SubValuesExtractor.SubValuesListener<Object>) invocation.getArguments()[1];
            svl.present(key, extractedItem);
            svl.absent(key);
            svl.broken(key, throwable);
            return null;
        }).when(subValuesExtractor).run(same(item), any());

        subValuesMatcher(subValuesExtractor, () -> subValuesChecker).run(item, safeTreeReporter);
        inOrder.verify(simpleTreeReporter).passedCheck("1");
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key), same(extractedItem));
        inOrder.verify(simpleTreeReporter).passedCheck("2");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginAbsentNode(same(key));
        inOrder.verify(simpleTreeReporter).passedCheck("3");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key), same(throwable));
        inOrder.verify(simpleTreeReporter).passedCheck("4");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).passedCheck("5");
        inOrder.verifyNoMoreInteractions();

        subValuesMatcher(subValuesExtractor, () -> subValuesChecker).runForAbsentItem(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).passedCheck("1");
        inOrder.verify(simpleTreeReporter).passedCheck("5");
        inOrder.verifyNoMoreInteractions();
    }

    // TODO: subValuesCheckerSupplier, который возвращает null
    // TODO: SubValuesChecker возвращает null вместо consumer'ов
    // TODO: subValuesCheckerSupplier, SubValuesChecker или SubValuesExtractor бросают исключение
}
