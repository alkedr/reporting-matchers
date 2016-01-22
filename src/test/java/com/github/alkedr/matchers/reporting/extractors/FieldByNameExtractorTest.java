package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.Keys;
import org.junit.Test;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.extractors.ExtractingMatcherExtractors.fieldByNameExtractor;
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
        fieldByNameExtractor(null);
    }

    @Test
    public void extractFrom_nullItem() {
        assertReflectionEquals(
                new Extractor.Result.Missing(Keys.fieldByNameKey("myInaccessibleField")),
                fieldByNameExtractor("myInaccessibleField").extractFrom(null)
        );
    }

    @Test
    public void extractFrom_inaccessibleField() {
        assertReflectionEquals(
                new Extractor.Result.Present(Keys.fieldKey(inaccessibleField), 2),
                fieldByNameExtractor("myInaccessibleField").extractFrom(item)
        );
    }

    @Test
    public void extractFrom_itemDoesNotHaveSuchField() {
        assertReflectionEquals(
                new Extractor.Result.Missing(Keys.fieldByNameKey("myInaccessibleField")),
                fieldByNameExtractor("myInaccessibleField").extractFrom(new Object())
        );
    }

    @Test
    public void extractFrom_itemHasTwoMatchingFields() {
        assertReflectionEquals(
                new Extractor.Result.Present(Keys.fieldKey(inaccessibleField2), 3),
                fieldByNameExtractor("myInaccessibleField").extractFrom(new MyClassWithTwoFields())
        );
    }

    @Test
    public void extractFrom_missingItem() {
        assertReflectionEquals(
                new Extractor.Result.Missing(Keys.fieldByNameKey("myInaccessibleField")),
                fieldByNameExtractor("myInaccessibleField").extractFromMissingItem()
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
