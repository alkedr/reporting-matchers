package com.github.alkedr.matchers.reporting.keys;

public interface ExtractableKey extends Key {
    void extractFrom(Object item, ResultListener result);
    void extractFromMissingItem(ResultListener result);

    // Можно извлекать несколько значений!
    // Можно сделать IteratorMatcher реализацией ResultListener'а, а ElementChecker'ы - плагинами для этой реализации
    // Или лучше всё-таки сделать здесь исключения, а несколько значений извлекать IteratorMatcher'ом?
    interface ResultListener {
        void present(Key key, Object value);
        void missing(Key key);
        void broken(Key key, Throwable throwable);
    }
}
