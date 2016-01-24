package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.function.Consumer;

/**
 * Reporter - объект, который каким-то образом обрабатывает информацию о проверках от ReportingMatcher'а.
 *
 * TODO: описать структуру отчёта
 * Отчёт имеет древовидную структуру. Каждый узел - пара ключ-значение. Ключ - это
 *
 * TODO: пример кода как создать Reporter и запустить на нём ReportingMatcher
 */
// TODO: написать в доках интерфейсов как их реализовывать
// TODO: сделать отдельный репортер с beginNode и endNode? Это упростит репортеры. Как его назвать?
public interface Reporter {
    void presentNode(Key key, Object value, Consumer<Reporter> contents);
    void missingNode(Key key, Consumer<Reporter> contents);
    void brokenNode(Key key, Throwable throwable, Consumer<Reporter> contents);

    void correctlyPresent();
    void correctlyMissing();
    void incorrectlyPresent();
    void incorrectlyMissing();

    void passedCheck(String description);
    void failedCheck(String expected, String actual);
    void checkForMissingItem(String description);
    void brokenCheck(String description, Throwable throwable);

    // TODO: passedEqualToMatcher, failedEqualToMatcher?
}
