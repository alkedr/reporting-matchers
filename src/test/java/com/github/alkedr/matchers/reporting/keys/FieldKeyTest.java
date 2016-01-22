package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.noOp;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.missingSubValue;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.presentSubValue;
import static com.github.alkedr.matchers.reporting.keys.Keys.fieldKey;
import static java.util.Collections.emptyIterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

public class FieldKeyTest {
    private final Field inaccessibleField;
    private final Field staticField;
    private final MyClass item = new MyClass();

    public FieldKeyTest() throws NoSuchFieldException {
        inaccessibleField = MyClass.class.getDeclaredField("myInaccessibleField");
        staticField = MyClass.class.getDeclaredField("MY_STATIC_FIELD");
    }

    @Test(expected = NullPointerException.class)
    public void nullField() {
        fieldKey(null);
    }

    @Test
    public void asStringTest() {
        assertEquals("myInaccessibleField", fieldKey(inaccessibleField).asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(fieldKey(inaccessibleField).hashCode(), fieldKey(inaccessibleField).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(fieldKey(inaccessibleField), fieldKey(inaccessibleField));
        assertNotEquals(fieldKey(inaccessibleField), fieldKey(staticField));
    }

    @Test
    public void extractFrom_nullItem() {
        assertEquals(
                missingSubValue(fieldKey(inaccessibleField), emptyIterator()),
                fieldKey(inaccessibleField).extractFrom(null).createCheckResult(noOp())
        );
    }

    @Test
    public void extractFrom_inaccessibleField() {
        assertEquals(
                presentSubValue(fieldKey(inaccessibleField), 2, emptyIterator()),
                fieldKey(inaccessibleField).extractFrom(item).createCheckResult(noOp())
        );
    }

    @Test
    public void extractFrom_inaccessibleStaticFieldAndNullItem() {
        assertEquals(
                presentSubValue(fieldKey(staticField), 3, emptyIterator()),
                fieldKey(staticField).extractFrom(null).createCheckResult(noOp())
        );
    }

    @Test
    public void extractFrom_itemHasWrongClass() {
        ExtractableKey.Result.Broken actual = (ExtractableKey.Result.Broken) fieldKey(inaccessibleField).extractFrom(new Object());
        assertEquals(fieldKey(inaccessibleField), actual.key);
        assertSame(IllegalArgumentException.class, actual.throwable.getClass());   // TODO: missing?
    }

    @Test
    public void extractFrom_missingItem() {
        assertEquals(
                missingSubValue(fieldKey(inaccessibleField), emptyIterator()),
                fieldKey(inaccessibleField).extractFromMissingItem().createCheckResult(noOp())
        );
    }

    private static class MyClass {
        private final int myInaccessibleField = 2;
        private static final int MY_STATIC_FIELD = 3;
    }
}
