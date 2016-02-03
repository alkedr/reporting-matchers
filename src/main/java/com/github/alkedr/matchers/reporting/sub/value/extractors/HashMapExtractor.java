package com.github.alkedr.matchers.reporting.sub.value.extractors;

import java.util.Map;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.hashMapKey;

// не подходит для TreeMap, IdentityHashMap и пр.
// подходит только для HashMap (использует key.equals() и key.hashCode())
// TODO: иметь возможность настраивать способ преобразования ключа в строку?
class HashMapExtractor<K, V> implements SubValuesExtractor<Map<K, V>, V> {
    private final K key;

    HashMapExtractor(K key) {
        this.key = key;
    }

    @Override
    public void run(Map<K, V> item, SubValuesListener<V> subValuesListener) {
        try {
            if (item == null || !item.containsKey(key)) {
                subValuesListener.absent(hashMapKey(key));
            } else {
                subValuesListener.present(hashMapKey(key), item.get(key));
            }
        } catch (ClassCastException e) {
            subValuesListener.broken(hashMapKey(key), e);
        }
    }

    @Override
    public void runForAbsentItem(SubValuesListener<V> subValuesListener) {
        subValuesListener.absent(hashMapKey(key));
    }
}
