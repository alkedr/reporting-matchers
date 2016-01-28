package com.github.alkedr.matchers.reporting.sub.value.keys;

import com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractor;
import org.apache.commons.lang3.Validate;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.renamedKey;

// не объединяется с непереименованным Key
// TODO: объединяться с непереименованным Key если названия совпадают?
class RenamedKey implements ExtractableKey {
    private final Key originalKey;
    private final String name;

    RenamedKey(Key originalKey, String name) {
        Validate.notNull(originalKey, "originalKey");
        Validate.notBlank(name, "name");
        this.originalKey = originalKey;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenamedKey that = (RenamedKey) o;
        return originalKey.equals(that.originalKey) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = originalKey.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String asString() {
        return name;
    }

    @Override
    public void run(Object item, SubValuesListener subValuesListener) {
        if (originalKey instanceof SubValuesExtractor) {
            ((SubValuesExtractor<Object>) originalKey).run(item, new KeyRenamingSubValuesListener(subValuesListener, name));
        } else {
            throw new RuntimeException();  // FIXME
        }
    }

    @Override
    public void runForMissingItem(SubValuesListener subValuesListener) {
        if (originalKey instanceof SubValuesExtractor) {
            ((SubValuesExtractor<Object>) originalKey).runForMissingItem(new KeyRenamingSubValuesListener(subValuesListener, name));
        } else {
            throw new RuntimeException();  // FIXME
        }
    }


    private static class KeyRenamingSubValuesListener implements SubValuesListener {
        private final SubValuesListener subValuesListener;
        private final String name;

        KeyRenamingSubValuesListener(SubValuesListener subValuesListener, String name) {
            this.subValuesListener = subValuesListener;
            this.name = name;
        }

        @Override
        public void present(Key key, Object value) {
            subValuesListener.present(renamedKey(key, name), value);
        }

        @Override
        public void missing(Key key) {
            subValuesListener.missing(renamedKey(key, name));
        }

        @Override
        public void broken(Key key, Throwable throwable) {
            subValuesListener.broken(renamedKey(key, name), throwable);
        }
    }
}
