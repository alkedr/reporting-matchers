package com.github.alkedr.matchers.reporting.sub.value.checkers;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.reporters.SimpleTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.function.Consumer;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;
import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.compositeSubValuesCheckerSupplier;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class CompositeSubValuesCheckerSupplierTest {
    private final SimpleTreeReporter simpleTreeReporter = mock(SimpleTreeReporter.class);
    private final SafeTreeReporter safeTreeReporter = simpleTreeReporterToSafeTreeReporter(simpleTreeReporter);
    private final InOrder inOrder = inOrder(simpleTreeReporter);


    @Test
    public void test() {
        SubValuesChecker checker = compositeSubValuesCheckerSupplier(() -> new MyChecker("a"), () -> new MyChecker("b")).get();

        checker.begin(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).passedCheck("a1");
        inOrder.verify(simpleTreeReporter).passedCheck("b1");
        inOrder.verifyNoMoreInteractions();

        checker.present(null, null).accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).passedCheck("a2");
        inOrder.verify(simpleTreeReporter).passedCheck("b2");
        inOrder.verifyNoMoreInteractions();

        checker.absent(null).accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).passedCheck("a3");
        inOrder.verify(simpleTreeReporter).passedCheck("b3");
        inOrder.verifyNoMoreInteractions();

        checker.broken(null, null).accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).passedCheck("a4");
        inOrder.verify(simpleTreeReporter).passedCheck("b4");
        inOrder.verifyNoMoreInteractions();

        checker.end(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).passedCheck("a5");
        inOrder.verify(simpleTreeReporter).passedCheck("b5");
        inOrder.verifyNoMoreInteractions();
    }


    private static class MyChecker implements SubValuesChecker {
        private final String s;

        private MyChecker(String s) {
            this.s = s;
        }

        @Override
        public void begin(SafeTreeReporter safeTreeReporter) {
            safeTreeReporter.passedCheck(s + "1");
        }

        @Override
        public Consumer<SafeTreeReporter> present(Key key, Object value) {
            return safeTreeReporter -> safeTreeReporter.passedCheck(s + "2");
        }

        @Override
        public Consumer<SafeTreeReporter> absent(Key key) {
            return safeTreeReporter -> safeTreeReporter.passedCheck(s + "3");
        }

        @Override
        public Consumer<SafeTreeReporter> broken(Key key, Throwable throwable) {
            return safeTreeReporter -> safeTreeReporter.passedCheck(s + "4");
        }

        @Override
        public void end(SafeTreeReporter safeTreeReporter) {
            safeTreeReporter.passedCheck(s + "5");
        }
    }
}
