package com.github.alkedr.matchers.reporting.sub.value.keys;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

enum MethodNameUtils {
    ;

    // TODO: что делать с очень большими toString()?
    static String createNameForRegularMethodInvocation(String methodName, Object... arguments) {
        StringBuilder sb = new StringBuilder();
        sb.append(methodName).append('(');
        if (arguments.length > 0) {
            sb.append(arguments[0]);
            for (int i = 1; i < arguments.length; i++) {
                sb.append(", ").append(arguments[i]);   // TODO: брать строки и символы в кавычки?
            }
        }
        sb.append(')');
        return sb.toString();
    }

    @Deprecated
    static String createNameForGetterMethodInvocation(String name) {
        if (name == null) {
            return "";
        }
        if (name.length() > 3 && name.startsWith("get") && isUpperCase(name.charAt(3))) {
            return toLowerCase(name.charAt(3)) + name.substring(4);
        }
        if (name.length() > 2 && name.startsWith("is") && isUpperCase(name.charAt(2))) {
            return toLowerCase(name.charAt(2)) + name.substring(3);
        }
        return name;
    }
}
