package com.github.alkedr.matchers.reporting.sub.value.keys;

import com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractor;
import org.hamcrest.Matcher;

import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

public class ExtractorVerificationUtils {
    public static void verifyPresent(Consumer<SubValuesExtractor.SubValuesListener> consumer, Matcher<Key> keyMatcher, Matcher<Object> valueMatcher) {
        SubValuesExtractor.SubValuesListener subValuesListener = mock(SubValuesExtractor.SubValuesListener.class);
        consumer.accept(subValuesListener);
        verify(subValuesListener).present(argThat(keyMatcher), argThat(valueMatcher));
        verifyNoMoreInteractions(subValuesListener);
    }

    public static void verifyMissing(Consumer<SubValuesExtractor.SubValuesListener> consumer, Matcher<Key> keyMatcher) {
        SubValuesExtractor.SubValuesListener subValuesListener = mock(SubValuesExtractor.SubValuesListener.class);
        consumer.accept(subValuesListener);
        verify(subValuesListener).missing(argThat(keyMatcher));
        verifyNoMoreInteractions(subValuesListener);
    }

    public static void verifyBroken(Consumer<SubValuesExtractor.SubValuesListener> consumer, Matcher<Key> keyMatcher, Class<? extends Throwable> throwableClass) {
        SubValuesExtractor.SubValuesListener subValuesListener = mock(SubValuesExtractor.SubValuesListener.class);
        consumer.accept(subValuesListener);
        verify(subValuesListener).broken(argThat(keyMatcher), argThat(isA(throwableClass)));
        verifyNoMoreInteractions(subValuesListener);
    }
}
