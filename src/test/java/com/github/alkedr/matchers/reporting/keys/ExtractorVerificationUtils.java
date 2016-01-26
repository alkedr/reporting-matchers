package com.github.alkedr.matchers.reporting.keys;

import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertThat;

public class ExtractorVerificationUtils {

    public interface ThrowingExtractionResultSupplier {
        ExtractableKey.ExtractionResult get() throws ExtractableKey.MissingException, ExtractableKey.BrokenException;
    }


    public static void verifyPresent(ThrowingExtractionResultSupplier supplier, Matcher<Key> keyMatcher, Matcher<Object> valueMatcher) {
        try {
            ExtractableKey.ExtractionResult result = supplier.get();
            assertThat(result.getKey(), keyMatcher);
            assertThat(result.getValue(), valueMatcher);
        } catch (ExtractableKey.MissingException | ExtractableKey.BrokenException e) {
            throw new RuntimeException(e);
        }
    }

    public static void verifyMissing(ThrowingExtractionResultSupplier supplier, Matcher<Key> keyMatcher) {
        try {
            supplier.get();
            throw new AssertionError("Ожидали MissingException");
        } catch (ExtractableKey.MissingException e) {
            assertThat(e.getKey(), keyMatcher);
        } catch (ExtractableKey.BrokenException e) {
            throw new RuntimeException(e);
        }
    }

    public static void verifyBroken(ThrowingExtractionResultSupplier supplier, Matcher<Key> keyMatcher, Class<? extends Throwable> throwableClass) {
        try {
            supplier.get();
            throw new AssertionError("Ожидали BrokenException");
        } catch (ExtractableKey.MissingException e) {
            throw new RuntimeException(e);
        } catch (ExtractableKey.BrokenException e) {
            assertThat(e.getKey(), keyMatcher);
            assertThat(e.getCause(), isA((Class<Throwable>) throwableClass));
        }
    }
}
