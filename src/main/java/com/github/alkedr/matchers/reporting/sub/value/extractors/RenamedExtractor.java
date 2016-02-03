package com.github.alkedr.matchers.reporting.sub.value.extractors;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;
import org.apache.commons.lang3.Validate;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.renamedKey;

// не объединяется с непереименованным Key
// TODO: объединяться с непереименованным Key если названия совпадают?
class RenamedExtractor<T, S> implements SubValuesExtractor<T, S> {
    private final SubValuesExtractor<T, S> originalExtractor;
    private final String name;

    RenamedExtractor(SubValuesExtractor<T, S> originalExtractor, String name) {
        Validate.notNull(originalExtractor, "originalExtractor");
        Validate.notBlank(name, "name");
        this.originalExtractor = originalExtractor;
        this.name = name;
    }

    @Override
    public void run(T item, SubValuesListener<S> subValuesListener) {
        originalExtractor.run(item, new KeyRenamingSubValuesListener<>(subValuesListener, name));
    }

    @Override
    public void runForAbsentItem(SubValuesListener<S> subValuesListener) {
        originalExtractor.runForAbsentItem(new KeyRenamingSubValuesListener<>(subValuesListener, name));
    }


    private static class KeyRenamingSubValuesListener<S> implements SubValuesListener<S> {
        private final SubValuesListener<S> originalSubValuesListener;
        private final String name;

        KeyRenamingSubValuesListener(SubValuesListener<S> originalSubValuesListener, String name) {
            this.originalSubValuesListener = originalSubValuesListener;
            this.name = name;
        }

        @Override
        public void present(Key key, S value) {
            originalSubValuesListener.present(renamedKey(key, name), value);
        }

        @Override
        public void absent(Key key) {
            originalSubValuesListener.absent(renamedKey(key, name));
        }

        @Override
        public void broken(Key key, Throwable throwable) {
            originalSubValuesListener.broken(renamedKey(key, name), throwable);
        }
    }
}
