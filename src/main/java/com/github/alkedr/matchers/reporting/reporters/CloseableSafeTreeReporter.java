package com.github.alkedr.matchers.reporting.reporters;

import java.io.Closeable;

public interface CloseableSafeTreeReporter extends SafeTreeReporter, Closeable {
    @Override
    void close();
}
