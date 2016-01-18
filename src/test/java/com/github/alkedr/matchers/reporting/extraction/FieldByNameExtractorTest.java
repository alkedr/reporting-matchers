package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.keys.FieldByNameKey;
import com.github.alkedr.matchers.reporting.keys.FieldKey;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class FieldByNameExtractorTest {
    private final Field inaccessibleField;
    private final Field inaccessibleField2;
    private final MyClass item = new MyClass();

    public FieldByNameExtractorTest() throws NoSuchFieldException {
        inaccessibleField = MyClass.class.getDeclaredField("myInaccessibleField");
        inaccessibleField2 = MyClassWithTwoFields.class.getDeclaredField("myInaccessibleField");
    }


    @Test(expected = NullPointerException.class)
    public void nullFieldName() {
        new FieldByNameExtractor(null);
    }

    @Test
    public void extractFrom_nullItem() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new FieldByNameKey("myInaccessibleField"), ReportingMatcher.Value.missing()),
                new FieldByNameExtractor("myInaccessibleField").extractFrom(null)
        );
    }

    @Test
    public void extractFrom_inaccessibleField() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new FieldKey(inaccessibleField), ReportingMatcher.Value.present(2)),
                new FieldByNameExtractor("myInaccessibleField").extractFrom(item)
        );
    }

    @Test
    public void extractFrom_itemDoesNotHaveSuchField() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new FieldByNameKey("myInaccessibleField"), ReportingMatcher.Value.missing()),
                new FieldByNameExtractor("myInaccessibleField").extractFrom(new Object())
        );
    }

    @Test
    public void extractFrom_itemHasTwoMatchingFields() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new FieldKey(inaccessibleField2), ReportingMatcher.Value.present(3)),
                new FieldByNameExtractor("myInaccessibleField").extractFrom(new MyClassWithTwoFields())
        );
    }

    @Test
    public void extractFrom_missingItem() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new FieldByNameKey("myInaccessibleField"), ReportingMatcher.Value.missing()),
                new FieldByNameExtractor("myInaccessibleField").extractFromMissingItem()
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
