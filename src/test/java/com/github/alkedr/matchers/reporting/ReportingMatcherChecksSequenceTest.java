package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.keys.ElementKey;
import org.hamcrest.Matcher;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.ChecksUtils.verifyChecks;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Checks.*;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.PresenceStatus.MISSING;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.PresenceStatus.PRESENT;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;

public class ReportingMatcherChecksSequenceTest {
    private final Matcher<?> matcher = mock(Matcher.class);

    @Test
    public void empty() {
        verifyChecks(sequence());
    }

    @Test
    public void matcher() {
        verifyChecks(sequence(matchers(matcher)), ChecksUtils.matcher(matcher));
    }

    @Test
    public void presenceStatus_matcher_kvc() {
        ReportingMatcher.Key key = new ElementKey(0);
        ReportingMatcher.Value value = ReportingMatcher.Value.missing();
        ReportingMatcher.Checks checks = noOp();
        verifyChecks(
                sequence(
                        presenceStatus(PRESENT),
                        matchers(matcher),
                        keyValueChecks(new ReportingMatcher.KeyValueChecks(key, value, checks))
                ),
                ChecksUtils.presenceStatus(PRESENT),
                ChecksUtils.matcher(matcher),
                ChecksUtils.kvc(key, value, checks)
        );
    }

    @Test
    public void iterable() {
        verifyChecks(
                sequence(asList(presenceStatus(PRESENT), presenceStatus(MISSING))),
                ChecksUtils.presenceStatus(PRESENT),
                ChecksUtils.presenceStatus(MISSING)
        );
    }

    @Test
    public void iterator() {
        verifyChecks(
                sequence(asList(presenceStatus(PRESENT), presenceStatus(MISSING)).iterator()),
                ChecksUtils.presenceStatus(PRESENT),
                ChecksUtils.presenceStatus(MISSING)
        );
    }
}
