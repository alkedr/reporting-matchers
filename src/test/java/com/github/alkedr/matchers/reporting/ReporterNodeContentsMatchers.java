package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.Reporter;
import org.hamcrest.CustomTypeSafeMatcher;
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
        return argThat(new CustomTypeSafeMatcher<Consumer<Reporter>>("has correct contents") {
            @Override
            public boolean matchesSafely(Consumer<Reporter> item) {
                Reporter reporter = mock(Reporter.class);
                InOrder inOrder = inOrder(reporter);
                item.accept(reporter);
                verifier.accept(inOrder, reporter);
                inOrder.verifyNoMoreInteractions();
                return true;
            }
        });
    }
}
