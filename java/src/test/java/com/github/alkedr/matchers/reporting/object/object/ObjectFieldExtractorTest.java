package com.github.alkedr.matchers.reporting.object.object;

import com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue;
import com.github.alkedr.matchers.reporting.object.ObjectFieldExtractor;
import org.junit.Test;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.BROKEN;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.NORMAL;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ObjectFieldExtractorTest {
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

    // TODO: тестировать что будет если поле имеет неправильный тип

    // TODO: что правильнее делать?
    @Test(expected = NullPointerException.class)
    public void extractFrom_null() {
        new ObjectFieldExtractor(FIELD).extractFrom(null);
    }

    @Test
    public void extractFrom_normal() {
        ExtractedValue actual = new ObjectFieldExtractor(FIELD).extractFrom(new MyClass(1));
        assertEquals(NORMAL, actual.getStatus());
        assertEquals("1", actual.getValueAsString());
        assertEquals(1, actual.getValue());
    }

    @Test
    public void extractFrom_noSuchField() {
        ExtractedValue actual = new ObjectFieldExtractor(FIELD).extractFrom(new Object());
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("IllegalArgumentException"));
        assertNull(actual.getValue());
    }

    @Test
    public void inaccessibleField() {
        ExtractedValue actual = new ObjectFieldExtractor(INACCESSSIBLE_FIELD).extractFrom(new Object());
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("IllegalAccessException"));
        assertNull(actual.getValue());
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
