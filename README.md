# reporting-matchers

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.alkedr/reporting-matchers/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.alkedr/reporting-matchers)
[![Build Status](https://travis-ci.org/alkedr/reporting-matchers.svg?branch=master)](https://travis-ci.org/alkedr/reporting-matchers)

# Примеры кода

## Создание матчера

```java
static ReportingMatcher<? super User> isCorrectUser() {
    return merge(
            field("id", 123),   // проверки для полей и методов
            field("login", "login"),
            field("password", "drowssap"),
            getter("getNames"
                    , field("first", startsWith("qwe"), endsWith("rty"))
                    , field("middle", equalTo("123456"))
                    , field("last", is("ytrewq"))
            ),
            method(invocation("getArray")
                    , arrayElement(0, 1)
                    , array(containsInAnyOrder(1, 2, 3))
            ),
            displayAll(getters())   // добавляет *все* геттеры в отчёт, даже непроверенные
    );
}
```

## Использование матчера

Если подробный отчёт не нужен, то можно воспользоваться обычным `assertThat`.

```java
assertThat(myUser, isCorrectUser);
```

Если нужен подробный отчёт, то нужно написать отдельный метод, который будет запускать матчер, генерировать отчёт и сохранять/выводить его куда-нибудь.

```java
public static <T> void reportingAssertThat(T item, ReportingMatcher<T> reportingMatcher) {
    StringBuilder htmlReportStringBuilder = new StringBuilder();
    MatchesFlagRecordingSimpleTreeReporter matchesFlagRecordingReporter = Reporters.matchesFlagRecordingReporter();
    try (CloseableSimpleTreeReporter htmlReporter = Reporters.htmlReporter(htmlReportStringBuilder)) {
        reportingMatcher.run(
                item,
                Reporters.simpleTreeReporterToSafeTreeReporter(
                        Reporters.compositeSimpleTreeReporter(
                                matchesFlagRecordingReporter,
                                htmlReporter
                        )
                )
        );
    }

    // htmlReportStringBuilder содержит подробный отчёт, его нужно куда-то сохранить

    if (!matchesFlagRecordingReporter.getMatchesFlag()) {
        throw new AssertionError(...);
    }
}

reportingAssertThat(myUser, isCorrectUser);
```

В автотестах маркета этот метод называется `ru.yandex.autotests.market.common.steps.AssertSteps.reportingAssertThat`.

# Публичные классы, которые интересны всем, кто использует эту библиотеку
- `com.github.alkedr.matchers.reporting.ReportingMatcher` - интерфейс, расширяющий `org.hamcrest.Matcher`, добавляет методы, которые запускают проверки и строят отчёт.
- `com.github.alkedr.matchers.reporting.ReportingMatchers` - аналог org.hamcrest.Matchers, содержит static factory method'ы для всех матчеров.
- `com.github.alkedr.matchers.reporting.reporters.Reporters` - содержит static factory method'ы для всех репортеров.
- `com.github.alkedr.matchers.reporting.sub.value.extractors.SubValueCheckers` - содержит static factory method'ы для объектов, которые нужно передавать в static factory method'ы матчеров для массивов, `Iterable`'ов и `Iterator`'ов.
