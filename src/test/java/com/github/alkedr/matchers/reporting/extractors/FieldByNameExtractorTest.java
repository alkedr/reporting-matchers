package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.FieldByNameKey;
import com.github.alkedr.matchers.reporting.keys.FieldKey;
import org.junit.After;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.extractors.Extractors.fieldByNameExtractor;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class FieldByNameExtractorTest {
    private final Extractor.ResultListener result = mock(Extractor.ResultListener.class);
    private final FieldByNameKey myInaccessibleFieldByNameKey;
    private final FieldKey myInaccessibleField1Key;
    private final FieldKey myInaccessibleField2Key;

    public FieldByNameExtractorTest() throws NoSuchFieldException {
        myInaccessibleFieldByNameKey = new FieldByNameKey("myInaccessibleField");
        myInaccessibleField1Key = new FieldKey(MyClass.class.getDeclaredField("myInaccessibleField"));
        myInaccessibleField2Key = new FieldKey(MyClassWithTwoFields.class.getDeclaredField("myInaccessibleField"));
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(result);
    }

    @Test
    public void extractFrom_nullItem() {
        fieldByNameExtractor(myInaccessibleFieldByNameKey).extractFrom(null, result);
        verify(result).missing(myInaccessibleFieldByNameKey);
    }

    @Test
    public void extractFrom_inaccessibleField() {
        fieldByNameExtractor(myInaccessibleFieldByNameKey).extractFrom(new MyClass(), result);
        verify(result).present(eq(myInaccessibleField1Key), eq(2));
    }

    @Test
    public void extractFrom_itemDoesNotHaveSuchField() {
        fieldByNameExtractor(myInaccessibleFieldByNameKey).extractFrom(new Object(), result);
        verify(result).missing(eq(myInaccessibleFieldByNameKey));
    }

    @Test
    public void extractFrom_itemHasTwoMatchingFields() {
        fieldByNameExtractor(myInaccessibleFieldByNameKey).extractFrom(new MyClassWithTwoFields(), result);
        verify(result).present(eq(myInaccessibleField2Key), eq(3));
    }

    @Test
    public void extractFrom_missingItem() {
        fieldByNameExtractor(myInaccessibleFieldByNameKey).extractFromMissingItem(result);
        verify(result).missing(myInaccessibleFieldByNameKey);
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
