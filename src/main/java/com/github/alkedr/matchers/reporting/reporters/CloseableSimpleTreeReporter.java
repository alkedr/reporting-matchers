package com.github.alkedr.matchers.reporting.reporters;

import java.io.Closeable;

public interface CloseableSimpleTreeReporter extends SimpleTreeReporter, Closeable {
    @Override
    void close();
}
