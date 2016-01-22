package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.check.results.CheckResult;
import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.Iterator;

// объединяется не всё со всем, а:
//   - begin()'ы между собой
//   - element()'ы для каждого элемента между собой
//   - end()'ы между собой
// Плохо что begin()'ы не объединяются с end()'ами. Точно ли нужны begin()'ы?
public interface ElementChecker {
    Iterator<CheckResult> begin();
    Iterator<CheckResult> element(Key key, Object value);   // TODO: Object item?
    Iterator<CheckResult> end();
}
