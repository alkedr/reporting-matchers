package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.keys.ElementKey;
import org.hamcrest.CustomMatcher;
import org.hamcrest.Matcher;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Checks.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ReportingMatcherChecksRunTest {
    private final Object item = new Object();
    private final Reporter reporter = mock(Reporter.class);
    private final RuntimeException exception = new RuntimeException();
    private final Matcher<?> brokenMatcher = new CustomMatcher<Object>("12345") {
        @Override
        public boolean matches(Object item) {
            throw exception;
        }
    };
    private final ElementKey key = new ElementKey(0);


    @Test
    public void presentItem_noOp() {
        noOp().run(item, reporter);
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void missingItem_noOp() {
        noOp().runForMissingItem(reporter);
        verifyNoMoreInteractions(reporter);
    }


    @Test
    public void presentItem_present() {
        present().run(item, reporter);
        verify(reporter).passedCheck("is present");
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void presentItem_missing() {
        missing().run(item, reporter);
        verify(reporter).failedCheck("missing", "present");
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void missingItem_present() {
        present().runForMissingItem(reporter);
        verify(reporter).failedCheck("present", "missing");
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void missingItem_missing() {
        missing().runForMissingItem(reporter);
        verify(reporter).passedCheck("is missing");
        verifyNoMoreInteractions(reporter);
    }


    @Test
    public void presentItem_passingMatcher() {
        matchers(anything()).run(item, reporter);
        verify(reporter).passedCheck("ANYTHING");
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void presentItem_failingMatcher() {
        matchers(not(anything())).run(item, reporter);
        verify(reporter).failedCheck("not ANYTHING", "was <" + item + ">");
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void presentItem_throwingMatcher() {
        matchers(brokenMatcher).run(item, reporter);
        verify(reporter).brokenCheck("матчер '12345' бросил исключение:", exception);
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void missingItem_passingMatcher() {
        matchers(anything()).runForMissingItem(reporter);
        verify(reporter).failedCheck("ANYTHING", "missing");
        verifyNoMoreInteractions(reporter);
    }


    @Test
    public void presentItem_presentKeyValueChecks() {
        ReportingMatcher.Value value = ReportingMatcher.Value.present(1);
        keyValueChecks(new ReportingMatcher.KeyValueChecks(key, value, present())).run(item, reporter);
        verify(reporter).beginNode(key.asString(), value.asString());
        verify(reporter).passedCheck("is present");
        verify(reporter).endNode();
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void presentItem_missingKeyValueChecks() {
        ReportingMatcher.Value value = ReportingMatcher.Value.missing();
        keyValueChecks(new ReportingMatcher.KeyValueChecks(key, value, missing())).run(item, reporter);
        verify(reporter).beginNode(key.asString(), value.asString());
        verify(reporter).passedCheck("is missing");
        verify(reporter).endNode();
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void presentItem_brokenKeyValueChecks() {
        ReportingMatcher.Value value = ReportingMatcher.Value.broken(new RuntimeException("123"));
        keyValueChecks(new ReportingMatcher.KeyValueChecks(key, value, missing())).run(item, reporter);
        verify(reporter).beginNode(key.asString(), value.asString());
        verify(reporter).brokenCheck("ошибка при извлечении:", value.extractionThrowable());
        verify(reporter).passedCheck("is missing");
        verify(reporter).endNode();
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void missingItem_presentKeyValueChecks() {
        ReportingMatcher.Value value = ReportingMatcher.Value.present(1);
        keyValueChecks(new ReportingMatcher.KeyValueChecks(key, value, present())).runForMissingItem(reporter);
        verify(reporter).beginNode(key.asString(), value.asString());
        verify(reporter).passedCheck("is present");
        verify(reporter).endNode();
        verifyNoMoreInteractions(reporter);
    }

    // TODO: тест с полноценным деревом проверок

    /*@Test
    public void presentItem_missingKeyValueChecks_withPresentKeyValueChecksInside() {
        ReportingMatcher.Value presentValue = ReportingMatcher.Value.present(1);
        ReportingMatcher.Value missingValue = ReportingMatcher.Value.missing();
        keyValueChecks(new ReportingMatcher.KeyValueChecks(
                key,
                presentValue,
                keyValueChecks(new ReportingMatcher.KeyValueChecks(
                        key,
                        missingValue,
                        missing()
                ))
        )).run(item, reporter);
        verify(reporter).beginNode(key.asString(), presentValue.asString());
        verify(reporter).beginNode(key.asString(), missingValue.asString());
        verify(reporter).passedCheck("is missing");
        verify(reporter, times(2)).endNode();
        verifyNoMoreInteractions(reporter);
    }*/
}
