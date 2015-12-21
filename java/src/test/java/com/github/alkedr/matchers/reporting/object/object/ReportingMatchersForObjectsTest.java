package com.github.alkedr.matchers.reporting.object.object;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.junit.Test;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.CheckStatus.PASSED;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.NORMAL;
import static com.github.alkedr.matchers.reporting.object.ReportingMatchersForObjects.field;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ReportingMatchersForObjectsTest {
    private static final Field FIELD;
    private static final Field INACCESSSIBLE_FIELD;

    static {
        try {
            FIELD = MyClass.class.getField("myField");
            INACCESSSIBLE_FIELD = MyClass.class.getDeclaredField("myInaccessibleField");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private final ReportingMatcher.Reporter reporter = mock(ReportingMatcher.Reporter.class);


    @Test
    public void field1() {
        String nameForReport = "123";
        field(FIELD).displayedAs(nameForReport).is(equalTo(1)).run(new MyClass(1), reporter);
        inOrder(reporter).verify(reporter).beginKeyValuePair(nameForReport, NORMAL, "1");
        inOrder(reporter).verify(reporter).addCheck(PASSED, "<1>");
        inOrder(reporter).verify(reporter).endKeyValuePair();
        verifyNoMoreInteractions(reporter);
    }



    public static class MyClass {
        public final int myField;
        private final int myInaccessibleField;

        private MyClass(int myField) {
            this.myField = myField;
            this.myInaccessibleField = myField;
        }
    }
}
