package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.MethodKey;
import org.junit.After;
import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.extractors.Extractors.methodExtractor;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MethodExtractorTest {
    private final Extractor.ResultListener result = mock(Extractor.ResultListener.class);
    private final MethodKey inaccessibleMethodKey;
    private final MethodKey inaccessibleStaticMethodKey;
    private final MethodKey throwingMethodKey;
    private final Method returnArgMethod;

    public MethodExtractorTest() throws NoSuchMethodException {
        returnArgMethod = MyClass.class.getDeclaredMethod("returnArg", int.class);
        inaccessibleMethodKey = new MethodKey(returnArgMethod, 1);
        inaccessibleStaticMethodKey = new MethodKey(MyClass.class.getDeclaredMethod("returnArgStatic", int.class), 2);
        throwingMethodKey = new MethodKey(MyClass.class.getDeclaredMethod("throwingMethod"));
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(result);
    }

    @Test
    public void nullItem() {
        methodExtractor(inaccessibleMethodKey).extractFrom(null, result);
        verify(result).missing(inaccessibleMethodKey);
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        methodExtractor(inaccessibleMethodKey).extractFrom(new MyClass(), result);
        verify(result).present(inaccessibleMethodKey, 1);
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        methodExtractor(inaccessibleStaticMethodKey).extractFrom(new MyClass(), result);
        verify(result).present(inaccessibleStaticMethodKey, 2);
    }

    @Test
    public void inaccessibleStaticMethodWithArguments_nullItem() {
        methodExtractor(inaccessibleStaticMethodKey).extractFrom(null, result);
        verify(result).present(inaccessibleStaticMethodKey, 2);
    }

    @Test
    public void itemHasWrongClass() {
        methodExtractor(inaccessibleMethodKey).extractFrom(new Object(), result);
        verify(result).broken(same(inaccessibleMethodKey), isA(IllegalArgumentException.class));
    }

    @Test
    public void wrongParameterCount() {
        MethodKey keyWithWrongNumberOfParameters = new MethodKey(returnArgMethod);
        methodExtractor(keyWithWrongNumberOfParameters).extractFrom(new MyClass(), result);
        verify(result).broken(same(keyWithWrongNumberOfParameters), isA(IllegalArgumentException.class));
    }

    @Test
    public void wrongParameterType() {
        MethodKey keyWithWrongParameterType = new MethodKey(returnArgMethod, "1");
        methodExtractor(keyWithWrongParameterType).extractFrom(new MyClass(), result);
        verify(result).broken(same(keyWithWrongParameterType), isA(IllegalArgumentException.class));
    }

    @Test
    public void throwingMethod() {
        methodExtractor(throwingMethodKey).extractFrom(new MyClass(), result);
        verify(result).broken(same(throwingMethodKey), isA(RuntimeException.class));
    }

    @Test
    public void extractFromMissingItem() {
        methodExtractor(inaccessibleMethodKey).extractFromMissingItem(result);
        verify(result).missing(inaccessibleMethodKey);
    }

    @Test
    public void inaccessibleStaticMethodWithArguments_missingItem() {
        methodExtractor(inaccessibleStaticMethodKey).extractFromMissingItem(result);
        verify(result).present(inaccessibleStaticMethodKey, 2);
    }


    private static class MyClass {
        private int returnArg(int arg) {
            return arg;
        }

        private static int returnArgStatic(int arg) {
            return arg;
        }

        private void throwingMethod() {
            throw new RuntimeException();
        }
    }
}
