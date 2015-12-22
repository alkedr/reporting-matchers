package com.github.alkedr.matchers.reporting.object.object;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.CheckStatus.PASSED;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.NORMAL;
import static com.github.alkedr.matchers.reporting.object.ReportingMatchersForObjects.field;
import static com.github.alkedr.matchers.reporting.object.object.MyClass.FIELD;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ReportingMatchersForObjectsTest {
    private final ReportingMatcher.Reporter reporter = mock(ReportingMatcher.Reporter.class);

    @Test
    public void fieldByField() {
        // TODO: вызывать field(FIELD) и проверять что возвращают геттеры?
        String nameForReport = "123";
        field(FIELD).displayedAs(nameForReport).is(equalTo(1)).run(new MyClass(1), reporter);
        inOrder(reporter).verify(reporter).beginKeyValuePair(nameForReport, NORMAL, "1");
        inOrder(reporter).verify(reporter).addCheck(PASSED, "<1>");
        inOrder(reporter).verify(reporter).endKeyValuePair();
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void fieldByName() {
    }
}
