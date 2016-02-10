package com.github.alkedr.matchers.reporting.sub.value.extractors;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractors.renamed;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.renamedKey;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class RenamedExtractorTest {
    private final SubValuesExtractor.SubValuesListener<Object> listener = mock(SubValuesExtractor.SubValuesListener.class);
    private final Throwable throwable = new RuntimeException();
    private final Key key1 = mock(Key.class);
    private final Key key2 = mock(Key.class);
    private final Key key3 = mock(Key.class);
    private final SubValuesExtractor<Integer, Object> extractor = new SubValuesExtractor<Integer, Object>() {
        @Override
        public void run(Integer item, SubValuesListener<Object> subValuesListener) {
            subValuesListener.present(key1, item + 1);
            subValuesListener.absent(key2);
            subValuesListener.broken(key3, throwable);
        }

        @Override
        public void runForAbsentItem(SubValuesListener<Object> subValuesListener) {
            subValuesListener.broken(key1, throwable);
            subValuesListener.absent(key2);
            subValuesListener.present(key3, 1);
        }
    };


    @Test(expected = NullPointerException.class)
    public void nullKey() {
        renamed(null, "1");
    }

    @Test(expected = NullPointerException.class)
    public void nullName() {
        renamed(extractor, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyName() {
        renamed(extractor, "");
    }

    @Test
    public void run() {
        renamed(extractor, "1").run(1, listener);
        verify(listener).present(renamedKey(key1, "1"), 2);
        verify(listener).absent(renamedKey(key2, "1"));
        verify(listener).broken(eq(renamedKey(key3, "1")), same(throwable));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void runForAbsentItemTest() {
        renamed(extractor, "1").runForAbsentItem(listener);
        verify(listener).broken(eq(renamedKey(key1, "1")), same(throwable));
        verify(listener).absent(renamedKey(key2, "1"));
        verify(listener).present(renamedKey(key3, "1"), 1);
        verifyNoMoreInteractions(listener);
    }
}
