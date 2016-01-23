package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.Reporter;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class ReporterNodeContentsMatchers {
    public static Consumer<Reporter> emptyContents() {
        return contentsThat((inOrder, reporter) -> {});
    }

    public static Consumer<Reporter> contentsThat(BiConsumer<InOrder, Reporter> verifier) {
        return argThat(
                (ArgumentMatcher<Consumer<Reporter>>) item -> {
                    Reporter reporter = mock(Reporter.class);
                    InOrder inOrder = inOrder(reporter);
                    ((Consumer<Reporter>) item).accept(reporter);
                    verifier.accept(inOrder, reporter);
                    inOrder.verifyNoMoreInteractions();
                    return true;
                }
        );
    }
}
