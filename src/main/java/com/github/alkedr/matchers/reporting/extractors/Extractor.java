package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.Key;

public interface Extractor {
    void extractFrom(Object item, ResultListener result);
    void extractFromMissingItem(ResultListener result);

    // Можно извлекать несколько значений!
    interface ResultListener {
        void present(Key key, Object value);
        void missing(Key key);
        void broken(Key key, Throwable throwable);
    }
}
