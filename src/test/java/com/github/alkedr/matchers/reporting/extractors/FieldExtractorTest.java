package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.Keys;
import org.junit.Test;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.extractors.ExtractingMatcherExtractors.fieldExtractor;
import static org.junit.Assert.assertEquals;
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
        fieldExtractor(null);
    }

    @Test
    public void extractFrom_nullItem() {
        assertReflectionEquals(
                new Extractor.Result.Missing(Keys.fieldKey(inaccessibleField)),
                fieldExtractor(inaccessibleField).extractFrom(null)
        );
    }

    @Test
    public void extractFrom_inaccessibleField() {
        assertReflectionEquals(
                new Extractor.Result.Present(Keys.fieldKey(inaccessibleField), 2),
                fieldExtractor(inaccessibleField).extractFrom(item)
        );
    }

    @Test
    public void extractFrom_inaccessibleStaticFieldAndNullItem() {
        assertReflectionEquals(
                new Extractor.Result.Present(Keys.fieldKey(staticField), 3),
                fieldExtractor(staticField).extractFrom(null)
        );
    }

    @Test
    public void extractFrom_itemHasWrongClass() {
        Extractor.Result.Broken actual = (Extractor.Result.Broken) fieldExtractor(inaccessibleField).extractFrom(new Object());
        assertEquals(Keys.fieldKey(inaccessibleField), actual.key);
        assertSame(IllegalArgumentException.class, actual.throwable.getClass());   // TODO: missing?
    }

    @Test
    public void extractFrom_missingItem() {
        assertReflectionEquals(
                new Extractor.Result.Missing(Keys.fieldKey(inaccessibleField)),
                fieldExtractor(inaccessibleField).extractFromMissingItem()
        );
    }

    private static class MyClass {
        private final int myInaccessibleField = 2;
        private static final int MY_STATIC_FIELD = 3;
    }
}
