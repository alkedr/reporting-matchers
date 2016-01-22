package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.noOp;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.missingSubValue;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.presentSubValue;
import static com.github.alkedr.matchers.reporting.keys.Keys.fieldByNameKey;
import static com.github.alkedr.matchers.reporting.keys.Keys.fieldKey;
import static java.util.Collections.emptyIterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FieldByNameKeyTest {
    private final Field inaccessibleField;
    private final Field inaccessibleField2;
    private final MyClass item = new MyClass();

    public FieldByNameKeyTest() throws NoSuchFieldException {
        inaccessibleField = MyClass.class.getDeclaredField("myInaccessibleField");
        inaccessibleField2 = MyClassWithTwoFields.class.getDeclaredField("myInaccessibleField");
    }

    @Test(expected = NullPointerException.class)
    public void nullFieldName() {
        fieldByNameKey(null);
    }

    @Test
    public void asStringTest() {
        assertEquals("field1", fieldByNameKey("field1").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(fieldByNameKey("field1").hashCode(), fieldByNameKey("field1").hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(fieldByNameKey("field1"), fieldByNameKey("field1"));
        assertNotEquals(fieldByNameKey("field1"), fieldByNameKey("field2"));
    }

    @Test
    public void extractFrom_nullItem() {
        assertEquals(
                missingSubValue(fieldByNameKey("myInaccessibleField"), emptyIterator()),
                fieldByNameKey("myInaccessibleField").extractFrom(null).createCheckResult(noOp())
        );
    }

    @Test
    public void extractFrom_inaccessibleField() {
        assertEquals(
                presentSubValue(fieldKey(inaccessibleField), 2, emptyIterator()),
                fieldByNameKey("myInaccessibleField").extractFrom(item).createCheckResult(noOp())
        );
    }

    @Test
    public void extractFrom_itemDoesNotHaveSuchField() {
        assertEquals(
                missingSubValue(fieldByNameKey("myInaccessibleField"), emptyIterator()),
                fieldByNameKey("myInaccessibleField").extractFrom(new Object()).createCheckResult(noOp())
        );
    }

    @Test
    public void extractFrom_itemHasTwoMatchingFields() {
        assertEquals(
                presentSubValue(fieldKey(inaccessibleField2), 3, emptyIterator()),
                fieldByNameKey("myInaccessibleField").extractFrom(new MyClassWithTwoFields()).createCheckResult(noOp())
        );
    }

    @Test
    public void extractFrom_missingItem() {
        assertEquals(
                missingSubValue(fieldByNameKey("myInaccessibleField"), emptyIterator()),
                fieldByNameKey("myInaccessibleField").extractFromMissingItem().createCheckResult(noOp())
        );
    }

    // TODO: static поле?
    // TODO: static поле + не-static поле с одинаковыми именами
    // TODO: 2 поля с одинаковыми именами, такие, что FieldUtils.getField бросает исключение

    private static class MyClass {
        private final int myInaccessibleField = 2;
    }

    private static class MyClassWithTwoFields extends MyClass {
        private final int myInaccessibleField = 3;
    }
}
