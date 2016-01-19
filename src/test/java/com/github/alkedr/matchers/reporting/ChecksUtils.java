package com.github.alkedr.matchers.reporting;

import com.google.common.collect.Iterators;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.util.Iterator;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ChecksUtils {
    public static void verifyChecks(ReportingMatcher.Checks actual, ReportingMatcher.Checks expected) {
        assertSame(expected, actual);
    }

    @SafeVarargs
    public static void verifyChecks(ReportingMatcher.Checks actual, Consumer<Object>... expected) {
        verifyChecks(actual.checksIterator, Iterators.forArray(expected));
    }

    private static void verifyChecks(Iterator<?> actual, Iterator<Consumer<Object>> expected) {
        while (actual.hasNext() && expected.hasNext()) {
            expected.next().accept(actual.next());
        }
        assertFalse("В actual лишние элементы", actual.hasNext());
        assertFalse("В actual слишком мало элементов", expected.hasNext());
    }


    @SafeVarargs
    public static Consumer<Iterator<?>> checks(Consumer<Object>... expected) {
        return actual -> verifyChecks(actual, Iterators.forArray(expected));
    }


    public static Consumer<Object> presenceStatus(ReportingMatcher.PresenceStatus expected) {
        return actual -> assertSame(actual, expected);
    }


    public static Consumer<Object> matcher(Matcher<?> expected) {
        return actual -> assertSame(actual, expected);
    }


    public static Consumer<Object> kvc(ReportingMatcher.Key key, ReportingMatcher.Value value,
                                       ReportingMatcher.Checks checks) {
        return kvc(sameInstance(key), sameInstance(value), sameInstance(checks));
    }

    public static Consumer<Object> kvc(Matcher<ReportingMatcher.Key> keyMatcher,
                                       Matcher<ReportingMatcher.Value> valueMatcher,
                                       Matcher<ReportingMatcher.Checks> checksMatcher) {
        return actual -> {
            assertTrue(actual instanceof ReportingMatcher.KeyValueChecks);
            ReportingMatcher.KeyValueChecks actualKvc = (ReportingMatcher.KeyValueChecks) actual;
            assertThat("key", actualKvc.key(), keyMatcher);
            assertThat("value", actualKvc.value(), valueMatcher);
            assertThat("checks", actualKvc.checks(), checksMatcher);
        };
    }

    public static Consumer<Object> kvc(ReportingMatcher.Key key, ReportingMatcher.Value value,
                                       Consumer<Iterator<?>> checksVerifier) {
        return kvc(sameInstance(key), sameInstance(value), checksVerifier);
    }

    public static Consumer<Object> kvc(Matcher<ReportingMatcher.Key> keyMatcher,
                                       Matcher<ReportingMatcher.Value> valueMatcher,
                                       Consumer<Iterator<?>> checksVerifier) {
        return actual -> {
            assertTrue(actual instanceof ReportingMatcher.KeyValueChecks);
            ReportingMatcher.KeyValueChecks actualKvc = (ReportingMatcher.KeyValueChecks) actual;
            assertThat("key", actualKvc.key(), keyMatcher);
            assertThat("value", actualKvc.value(), valueMatcher);
            checksVerifier.accept(actualKvc.checks().checksIterator);
        };
    }


    public static Matcher<ReportingMatcher.Value> value(ReportingMatcher.PresenceStatus presenceStatusMatcher,
                                                        Object objectMatcher,
                                                        Throwable extractionThrowableMatcher) {
        return value(equalTo(presenceStatusMatcher), sameInstance(objectMatcher), sameInstance(extractionThrowableMatcher));
    }

    public static Matcher<ReportingMatcher.Value> value(Matcher<ReportingMatcher.PresenceStatus> presenceStatusMatcher,
                                                        Matcher<Object> objectMatcher,
                                                        Matcher<Throwable> extractionThrowableMatcher) {
        return allOf(
                new FeatureMatcher<ReportingMatcher.Value, ReportingMatcher.PresenceStatus>(presenceStatusMatcher, "presenceStatus", "presenceStatus") {
                    @Override
                    protected ReportingMatcher.PresenceStatus featureValueOf(ReportingMatcher.Value actual) {
                        return actual.presenceStatus();
                    }
                },
                new FeatureMatcher<ReportingMatcher.Value, Object>(objectMatcher, "object", "object") {
                    @Override
                    protected Object featureValueOf(ReportingMatcher.Value actual) {
                        return actual.get();
                    }
                },
                new FeatureMatcher<ReportingMatcher.Value, Throwable>(extractionThrowableMatcher, "extractionThrowable", "extractionThrowable") {
                    @Override
                    protected Throwable featureValueOf(ReportingMatcher.Value actual) {
                        return actual.extractionThrowable();
                    }
                }
        );
    }
}
