package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.keys.FieldKey;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class FieldExtractorTest {
    private final Field inaccessibleField;
    private final Field staticField;
    private final MyClass item = new MyClass();

    public FieldExtractorTest() throws NoSuchFieldException {
        inaccessibleField = MyClass.class.getDeclaredField("myInaccessibleField");
        staticField = MyClass.class.getDeclaredField("MY_STATIC_FIELD");
    }


    @Test(expected = NullPointerException.class)
    public void nullField() {
        new FieldExtractor(null);
    }

    @Test
    public void extractFrom_nullItem() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new FieldKey(inaccessibleField), ReportingMatcher.Value.missing()),
                new FieldExtractor(inaccessibleField).extractFrom(null)
        );
    }

    @Test
    public void extractFrom_inaccessibleField() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new FieldKey(inaccessibleField), ReportingMatcher.Value.present(2)),
                new FieldExtractor(inaccessibleField).extractFrom(item)
        );
    }

    @Test
    public void extractFrom_inaccessibleStaticFieldAndNullItem() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new FieldKey(staticField), ReportingMatcher.Value.present(3)),
                new FieldExtractor(staticField).extractFrom(null)
        );
    }

    @Test
    public void extractFrom_itemHasWrongClass() {
        ExtractingMatcher.KeyValue actual = new FieldExtractor(inaccessibleField).extractFrom(new Object());
        assertEquals(new FieldKey(inaccessibleField), actual.key);
        assertEquals(ReportingMatcher.PresenceStatus.MISSING, actual.value.presenceStatus());
        assertNull(actual.value.get());
        assertSame(IllegalArgumentException.class, actual.value.extractionThrowable().getClass());   // TODO: missing?
    }

    @Test
    public void extractFrom_missingItem() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new FieldKey(inaccessibleField), ReportingMatcher.Value.missing()),
                new FieldExtractor(inaccessibleField).extractFromMissingItem()
        );
    }

    private static class MyClass {
        private final int myInaccessibleField = 2;
        private static final int MY_STATIC_FIELD = 3;
    }
}
