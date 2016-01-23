package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.MethodByNameKey;
import com.github.alkedr.matchers.reporting.keys.MethodKey;
import org.junit.After;
import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.extractors.Extractors.methodByNameExtractor;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MethodByNameExtractorTest {
    private final Extractor.ResultListener result = mock(Extractor.ResultListener.class);
    private final Method returnArgMethod;
    private final Method returnArgStaticMethod;

    public MethodByNameExtractorTest() throws NoSuchMethodException {
        returnArgMethod = MyClass.class.getDeclaredMethod("returnArg", int.class);
        returnArgStaticMethod = MyClass.class.getDeclaredMethod("returnArgStatic", int.class);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(result);
    }

    @Test
    public void nullItem() {
        MethodByNameKey returnArgKey = new MethodByNameKey("returnArg", 1);
        methodByNameExtractor(returnArgKey).extractFrom(null, result);
        verify(result).missing(returnArgKey);
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        MethodByNameKey returnArgKey = new MethodByNameKey("returnArg", 1);
        methodByNameExtractor(returnArgKey).extractFrom(new MyClass(), result);
        verify(result).present(new MethodKey(returnArgMethod, 1), 1);
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        methodByNameExtractor(new MethodByNameKey("returnArgStatic", 2)).extractFrom(new MyClass(), result);
        verify(result).present(new MethodKey(returnArgStaticMethod, 2), 2);
    }

    @Test
    public void noSuchMethod() {
        MethodByNameKey key = new MethodByNameKey("returnArg", 1);
        methodByNameExtractor(key).extractFrom(new Object(), result);
        verify(result).broken(eq(key), isA(NoSuchMethodException.class));
    }

    @Test
    public void wrongParameterCount() {
        MethodByNameKey key = new MethodByNameKey("returnArg");
        methodByNameExtractor(key).extractFrom(new MyClass(), result);
        verify(result).broken(eq(key), isA(NoSuchMethodException.class));
    }

    @Test
    public void wrongParameterType() {
        MethodByNameKey key = new MethodByNameKey("returnArg", "1");
        methodByNameExtractor(key).extractFrom(new MyClass(), result);
        verify(result).broken(eq(key), isA(NoSuchMethodException.class));
    }

    @Test
    public void extractFromMissingItem() {
        MethodByNameKey returnArgKey = new MethodByNameKey("returnArg", 1);
        methodByNameExtractor(returnArgKey).extractFromMissingItem(result);
        verify(result).missing(returnArgKey);
    }


    public static class MyClass {
        public int returnArg(int arg) {
            return arg;
        }

        public static int returnArgStatic(int arg) {
            return arg;
        }
    }
}
