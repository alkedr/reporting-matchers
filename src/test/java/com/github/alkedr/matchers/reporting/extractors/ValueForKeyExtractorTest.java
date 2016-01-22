package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.Keys;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.extractors.ExtractingMatcherExtractors.valueForKeyExtractor;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ValueForKeyExtractorTest {
    @Test
    public void nullItem() {
        assertReflectionEquals(
                new Extractor.Result.Missing(Keys.hashMapKey("1")),
                valueForKeyExtractor("1").extractFrom(null)
        );
    }

    @Test
    public void itemIsNotMap() {
        Extractor.Result.Broken actual = (Extractor.Result.Broken) valueForKeyExtractor("1").extractFrom(new Object());
        assertEquals(Keys.hashMapKey("1"), actual.key);
        assertSame(ClassCastException.class, actual.throwable.getClass());
    }

    @Test
    public void keyIsMissing() {
        assertReflectionEquals(
                new Extractor.Result.Missing(Keys.hashMapKey("1")),
                valueForKeyExtractor("1").extractFrom(singletonMap("2", "q"))
        );
    }

    @Test
    public void keyIsPresent() {
        assertReflectionEquals(
                new Extractor.Result.Present(Keys.hashMapKey("1"), "q"),
                valueForKeyExtractor("1").extractFrom(singletonMap("1", "q"))
        );
    }

    @Test
    public void keyIsPresentButValueIsNull() {
        assertReflectionEquals(
                new Extractor.Result.Present(Keys.hashMapKey("1"), null),
                valueForKeyExtractor("1").extractFrom(singletonMap("1", null))
        );
    }

    @Test
    public void extractFrom_missingItem() {
        assertReflectionEquals(
                new Extractor.Result.Missing(Keys.hashMapKey("1")),
                valueForKeyExtractor("1").extractFromMissingItem()
        );
    }
}
