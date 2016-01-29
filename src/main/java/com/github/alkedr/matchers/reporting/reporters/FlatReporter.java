package com.github.alkedr.matchers.reporting.reporters;

public interface FlatReporter {
    void correctlyPresent();
    void correctlyAbsent();
    void incorrectlyPresent();
    void incorrectlyAbsent();

    void passedCheck(String description);
    void failedCheck(String expected, String actual);
    void checkForAbsentItem(String description);
    void brokenCheck(String description, Throwable throwable);
    // TODO: passedEqualToMatcher, failedEqualToMatcher?

    // TODO: отдельный метод для ошибок, связанных с тем, что в run() передали item неправильного типа?

    // TODO: "..." на случай если отчёт получается слишком большой?
    // TODO: Обёртка для SimpleTreeReporter, которая ограничивает размер?
}
