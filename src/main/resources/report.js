//"use strict";
// use strict иногда выдаёт ошибку "SyntaxError: octal literals and octal escape sequences are deprecated"

var hidePassed = false;
var hideFailed = false;
var hideBroken = false;
var hideUnchecked = false;

// TODO: убрать дублирование кода PresentNode AbsentNode BrokenNode

function PresentNode(name, value, checks) {
    this.hasPassed = checks.some(function(check){return check.hasPassed;});
    this.hasFailed = checks.some(function(check){return check.hasFailed;});
    this.hasBroken = checks.some(function(check){return check.hasBroken;});
    this.isFolded = false;

    this.createDomElement = function() {
        var result = document.createElement("div");
        result.appendChild(createNameDiv(name, this));
        result.appendChild(createValueDiv(value));
        if (!this.isFolded) {
            result.appendChild(createChecksDiv(checks));
        }
        return result;
    }

    this.recursivelySetIsFolded = function(newValue) {
        this.isFolded = newValue;
        checks.forEach(function(check){check.recursivelySetIsFolded(newValue)});
    }

    function createNameDiv(name, node) {
        var result = document.createElement("div");
        var foldingSymbol = checks.length > 0 ? (node.isFolded ? "▶ " : "▼ ") : "";
        result.appendChild(document.createTextNode(foldingSymbol + name));
        result.classList.add('key');
        result.onclick = function(element) {
            node.isFolded = !node.isFolded;
            displayChecks();  // TODO: перерисовывать только изменившуюся часть
        };
        return result;
    }

    function createValueDiv(value) {
        var result = document.createElement("div");
        result.appendChild(document.createTextNode(value));
        result.classList.add('value');
        return result;
    }

    function createChecksDiv(checks) {
        var result = document.createElement("div");
        result.classList.add('checks');
        result.appendChild(createChecksDomFragment(checks));
        return result;
    }
}


function AbsentNode(name, checks) {
    this.hasPassed = checks.some(function(check){return check.hasPassed;});
    this.hasFailed = checks.some(function(check){return check.hasFailed;});
    this.hasBroken = checks.some(function(check){return check.hasBroken;});
    this.isFolded = false;

    this.createDomElement = function() {
        var result = document.createElement("div");
        result.appendChild(createNameDiv(name, this));
        result.appendChild(createValueDiv("(missing)"));
        if (!this.isFolded) {
            result.appendChild(createChecksDiv(checks));
        }
        return result;
    }

    this.recursivelySetIsFolded = function(newValue) {
        this.isFolded = newValue;
        checks.forEach(function(check){check.recursivelySetIsFolded(newValue)});
    }

    function createNameDiv(name, node) {
        var result = document.createElement("div");
        var foldingSymbol = checks.length > 0 ? (node.isFolded ? "▶ " : "▼ ") : "";
        result.appendChild(document.createTextNode(foldingSymbol + name));
        result.classList.add('key');
        result.onclick = function(element) {
            node.isFolded = !node.isFolded;
            displayChecks();  // TODO: перерисовывать только изменившуюся часть
        };
        return result;
    }

    function createValueDiv(value) {
        var result = document.createElement("div");
        result.appendChild(document.createTextNode(value));
        result.classList.add('value');
        return result;
    }

    function createChecksDiv(checks) {
        var result = document.createElement("div");
        result.classList.add('checks');
        result.appendChild(createChecksDomFragment(checks));
        return result;
    }
}


function BrokenNode(name, stacktrace, checks) {
    this.hasPassed = checks.some(function(check){return check.hasPassed;});
    this.hasFailed = checks.some(function(check){return check.hasFailed;});
    this.hasBroken = true;
    this.isFolded = false;

    this.createDomElement = function() {
        var result = document.createElement("div");
        result.appendChild(createNameDiv(name, this));
        result.appendChild(createValueDiv("(broken)"));
        if (!this.isFolded) {
            result.appendChild(createChecksDiv(checks));
        }  // TODO: stacktrace
        return result;
    }

    this.recursivelySetIsFolded = function(newValue) {
        this.isFolded = newValue;
        checks.forEach(function(check){check.recursivelySetIsFolded(newValue)});
    }

    function createNameDiv(name, node) {
        var result = document.createElement("div");
        var foldingSymbol = checks.length > 0 ? (node.isFolded ? "▶ " : "▼ ") : "";
        result.appendChild(document.createTextNode(foldingSymbol + name));
        result.classList.add('key');
        result.onclick = function(element) {
            node.isFolded = !node.isFolded;
            displayChecks();  // TODO: перерисовывать только изменившуюся часть
        };
        return result;
    }

    function createValueDiv(value) {
        var result = document.createElement("div");
        result.appendChild(document.createTextNode(value));
        result.classList.add('value');
        return result;
    }

    function createChecksDiv(checks) {
        var result = document.createElement("div");
        result.classList.add('checks');
        result.appendChild(createChecksDomFragment(checks));
        return result;
    }
}


function CorrectlyPresent() {
    this.hasPassed = true;
    this.hasFailed = false;
    this.hasBroken = false;

    this.createDomElement = function() {
        var mainDiv = document.createElement("div");
        mainDiv.appendChild(document.createTextNode("is present"));
        mainDiv.classList.add('passed');
        return mainDiv;
    }

    this.recursivelySetIsFolded = function(newValue) {}
}


function CorrectlyAbsent() {
    this.hasPassed = true;
    this.hasFailed = false;
    this.hasBroken = false;

    this.createDomElement = function() {
        var mainDiv = document.createElement("div");
        mainDiv.appendChild(document.createTextNode("is missing"));
        mainDiv.classList.add('passed');
        return mainDiv;
    }

    this.recursivelySetIsFolded = function(newValue) {}
}


function IncorrectlyPresent() {
    this.hasPassed = false;
    this.hasFailed = true;
    this.hasBroken = false;

    this.createDomElement = function() {
        var mainDiv = document.createElement("div");
        mainDiv.appendChild(document.createTextNode("is present"));
        mainDiv.classList.add('failed');
        return mainDiv;
    }

    this.recursivelySetIsFolded = function(newValue) {}
}


function IncorrectlyAbsent() {
    this.hasPassed = false;
    this.hasFailed = true;
    this.hasBroken = false;

    this.createDomElement = function() {
        var mainDiv = document.createElement("div");
        mainDiv.appendChild(document.createTextNode("is missing"));
        mainDiv.classList.add('failed');
        return mainDiv;
    }

    this.recursivelySetIsFolded = function(newValue) {}
}


function PassedCheck(description) {
    this.hasPassed = true;
    this.hasFailed = false;
    this.hasBroken = false;

    this.createDomElement = function() {
        var mainDiv = document.createElement("div");
        mainDiv.appendChild(document.createTextNode(description));
        mainDiv.classList.add('passed');
        return mainDiv;
    }

    this.recursivelySetIsFolded = function(newValue) {}
}


function FailedCheck(expected, actual) {
    this.hasPassed = false;
    this.hasFailed = true;
    this.hasBroken = false;

    this.createDomElement = function() {
        var mainDiv = document.createElement("div");
        mainDiv.appendChild(document.createTextNode("Expected: " + expected + "\\n     but: " + actual));
        mainDiv.classList.add('failed');
        return mainDiv;
    }

    this.recursivelySetIsFolded = function(newValue) {}
}


function CheckForAbsentItem(description) {
    this.hasPassed = false;
    this.hasFailed = true;
    this.hasBroken = false;

    this.createDomElement = function() {
        var mainDiv = document.createElement("div");
        mainDiv.appendChild(document.createTextNode(description));
        mainDiv.classList.add('failed');
        return mainDiv;
    }

    this.recursivelySetIsFolded = function(newValue) {}
}


function BrokenCheck(expected, stacktrace) {
    this.hasPassed = false;
    this.hasFailed = false;
    this.hasBroken = true;

    this.createDomElement = function() {
        var mainDiv = document.createElement("div");
        mainDiv.appendChild(document.createTextNode('Expected: ' + expected + "\\n     but: exception was thrown:\\n" + stacktrace));
        mainDiv.classList.add('broken');
        return mainDiv;
    }

    this.recursivelySetIsFolded = function(newValue) {}
}


function presentNode(name, value, checks) {
    return new PresentNode(name, value, checks);
}

function absentNode(name, checks) {
    return new AbsentNode(name, checks);
}

function brokenNode(name, stacktrace, checks) {
    return new BrokenNode(name, stacktrace, checks);
}

function correctlyPresent() {
    return new CorrectlyPresent();
}

function correctlyAbsent() {
    return new CorrectlyAbsent();
}

function incorrectlyPresent() {
    return new IncorrectlyPresent();
}

function incorrectlyAbsent() {
    return new IncorrectlyAbsent();
}

function passedCheck(description) {
    return new PassedCheck(description);
}

function failedCheck(expected, actual) {
    return new FailedCheck(expected, actual);
}

function checkForAbsentItem(description) {
    return new CheckForAbsentItem(description);
}

function brokenCheck(expected, stacktrace) {
    return new BrokenCheck(expected, stacktrace);
}


function shouldShow(check) {
    if (check == undefined) return false;
    if (hidePassed && check.hasPassed && !check.hasFailed && !check.hasBroken) return false;
    if (hideFailed && !check.hasPassed && check.hasFailed && !check.hasBroken) return false;
    if (hideBroken && !check.hasPassed && !check.hasFailed && check.hasBroken) return false;
    if (hideUnchecked && !check.hasPassed && !check.hasFailed && !check.hasBroken) return false;
    return true;
}

function createChecksDomFragment(checks) {
    checks = checks.filter(shouldShow);
    console.log(checks);
    return checks
        .map(function(check){return check.createDomElement()})
        .reduce(function(documentFragment, element){documentFragment.appendChild(element); return documentFragment;}, document.createDocumentFragment());
}


function displayChecks() {
    document.getElementById("checks").innerHTML = '';
    document.getElementById("checks").appendChild(createChecksDomFragment(checks));
}


function foldAll() {
    checks.forEach(function(check){check.recursivelySetIsFolded(true)});
    displayChecks();
}

function unfoldAll() {
    checks.forEach(function(check){check.recursivelySetIsFolded(false)});
    displayChecks();
}

function setHidePassed(newValue) {
    hidePassed = newValue;
    displayChecks();
}

function setHideFailed(newValue) {
    hideFailed = newValue;
    displayChecks();
}

function setHideBroken(newValue) {
    hideBroken = newValue;
    displayChecks();
}

function setHideUnchecked(newValue) {
    hideUnchecked = newValue;
    displayChecks();
}
