package com.github.alkedr.matchers.reporting.extractors;

import org.junit.Test;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.noOp;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.missingSubValue;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.presentSubValue;
import static com.github.alkedr.matchers.reporting.extractors.ExtractingMatcherExtractors.fieldByNameExtractor;
import static com.github.alkedr.matchers.reporting.keys.Keys.fieldByNameKey;
import static com.github.alkedr.matchers.reporting.keys.Keys.fieldKey;
import static java.util.Collections.emptyIterator;
import static org.junit.Assert.assertEquals;

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
        fieldByNameExtractor(null);
    }

    @Test
    public void extractFrom_nullItem() {
        assertEquals(
                missingSubValue(fieldByNameKey("myInaccessibleField"), emptyIterator()),
                fieldByNameExtractor("myInaccessibleField").extractFrom(null).createCheckResult(noOp())
        );
    }

    @Test
    public void extractFrom_inaccessibleField() {
        assertEquals(
                presentSubValue(fieldKey(inaccessibleField), 2, emptyIterator()),
                fieldByNameExtractor("myInaccessibleField").extractFrom(item).createCheckResult(noOp())
        );
    }

    @Test
    public void extractFrom_itemDoesNotHaveSuchField() {
        assertEquals(
                missingSubValue(fieldByNameKey("myInaccessibleField"), emptyIterator()),
                fieldByNameExtractor("myInaccessibleField").extractFrom(new Object()).createCheckResult(noOp())
        );
    }

    @Test
    public void extractFrom_itemHasTwoMatchingFields() {
        assertEquals(
                presentSubValue(fieldKey(inaccessibleField2), 3, emptyIterator()),
                fieldByNameExtractor("myInaccessibleField").extractFrom(new MyClassWithTwoFields()).createCheckResult(noOp())
        );
    }

    @Test
    public void extractFrom_missingItem() {
        assertEquals(
                missingSubValue(fieldByNameKey("myInaccessibleField"), emptyIterator()),
                fieldByNameExtractor("myInaccessibleField").extractFromMissingItem().createCheckResult(noOp())
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
