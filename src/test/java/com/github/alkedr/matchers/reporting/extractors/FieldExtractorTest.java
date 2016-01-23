package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.FieldKey;
import org.junit.After;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.extractors.Extractors.fieldExtractor;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class FieldExtractorTest {
    private final Extractor.ResultListener result = mock(Extractor.ResultListener.class);
    private final FieldKey inaccessibleFieldKey;
    private final FieldKey staticFieldKey;

    public FieldExtractorTest() throws NoSuchFieldException {
        inaccessibleFieldKey = new FieldKey(MyClass.class.getDeclaredField("myInaccessibleField"));
        staticFieldKey = new FieldKey(MyClass.class.getDeclaredField("MY_STATIC_FIELD"));
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(result);
    }

    @Test
    public void extractFrom_nullItem() {
        fieldExtractor(inaccessibleFieldKey).extractFrom(null, result);
        verify(result).missing(inaccessibleFieldKey);
    }

    @Test
    public void extractFrom_inaccessibleField() {
        fieldExtractor(inaccessibleFieldKey).extractFrom(new MyClass(), result);
        verify(result).present(inaccessibleFieldKey, 2);
    }

    @Test
    public void extractFrom_inaccessibleStaticField_nullItem() {
        fieldExtractor(staticFieldKey).extractFrom(null, result);
        verify(result).present(staticFieldKey, 3);
    }

    @Test
    public void extractFrom_inaccessibleStaticField_missingItem() {
        fieldExtractor(staticFieldKey).extractFromMissingItem(result);
        verify(result).present(staticFieldKey, 3);
    }

    @Test
    public void extractFrom_itemHasWrongClass() {
        fieldExtractor(inaccessibleFieldKey).extractFrom(new Object(), result);
        verify(result).broken(eq(inaccessibleFieldKey), isA(IllegalArgumentException.class)); // TODO: missing?
    }

    @Test
    public void extractFrom_missingItem() {
        fieldExtractor(inaccessibleFieldKey).extractFromMissingItem(result);
        verify(result).missing(inaccessibleFieldKey);
    }

    private static class MyClass {
        private final int myInaccessibleField = 2;
        private static final int MY_STATIC_FIELD = 3;
    }
}
