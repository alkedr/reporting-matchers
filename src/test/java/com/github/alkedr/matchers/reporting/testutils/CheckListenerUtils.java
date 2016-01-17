package com.github.alkedr.matchers.reporting.testutils;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.apache.commons.collections4.IteratorUtils;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.mockito.ArgumentCaptor;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;

public class CheckListenerUtils {
    @SafeVarargs
    public static void verifyKvcGroup(ReportingMatcher.CheckListener checkListener,
                                       Matcher<ReportingMatcher.KeyValueChecks>... kvcs) {
        ArgumentCaptor<Iterator<ReportingMatcher.KeyValueChecks>> captor =
                (ArgumentCaptor<Iterator<ReportingMatcher.KeyValueChecks>>) (Object)
                        ArgumentCaptor.forClass(Iterator.class);
        inOrder(checkListener).verify(checkListener).keyValueChecksGroup(captor.capture());
        assertThat(IteratorUtils.toList(captor.getValue()), contains(kvcs));
    }

    public static Matcher<ReportingMatcher.KeyValueChecks> kvc(ReportingMatcher.Key key,
                                                               ReportingMatcher.Value value,
                                                               ReportingMatcher.Checks checks) {
        return kvc(sameInstance(key), sameInstance(value), sameInstance(checks));
    }

    public static Matcher<ReportingMatcher.KeyValueChecks> kvc(Matcher<ReportingMatcher.Key> keyMatcher,
                                                               Matcher<ReportingMatcher.Value> valueMatcher,
                                                               Matcher<ReportingMatcher.Checks> checksMatcher) {
        return allOf(
                new FeatureMatcher<ReportingMatcher.KeyValueChecks, ReportingMatcher.Key>(keyMatcher, "key", "key") {
                    @Override
                    protected ReportingMatcher.Key featureValueOf(ReportingMatcher.KeyValueChecks actual) {
                        return actual.key();
                    }
                },

                new FeatureMatcher<ReportingMatcher.KeyValueChecks, ReportingMatcher.Value>(valueMatcher, "value", "value") {
                    @Override
                    protected ReportingMatcher.Value featureValueOf(ReportingMatcher.KeyValueChecks actual) {
                        return actual.value();
                    }
                },

                new FeatureMatcher<ReportingMatcher.KeyValueChecks, ReportingMatcher.Checks>(checksMatcher, "checks", "checks") {
                    @Override
                    protected ReportingMatcher.Checks featureValueOf(ReportingMatcher.KeyValueChecks actual) {
                        return actual.checks();
                    }
                }
        );
    }
}
