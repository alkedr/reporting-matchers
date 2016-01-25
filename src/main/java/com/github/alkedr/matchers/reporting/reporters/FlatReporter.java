package com.github.alkedr.matchers.reporting.reporters;

public interface FlatReporter {
    void correctlyPresent();
    void correctlyMissing();
    void incorrectlyPresent();
    void incorrectlyMissing();

    void passedCheck(String description);
    void failedCheck(String expected, String actual);
    void checkForMissingItem(String description);
    void brokenCheck(String description, Throwable throwable);
    // TODO: passedEqualToMatcher, failedEqualToMatcher?

    // TODO: "..." на случай если отчёт получается слишком большой?
    // TODO: Обёртка для SimpleTreeReporter, которая ограничивает размер?
}
