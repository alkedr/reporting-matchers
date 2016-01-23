package com.github.alkedr.matchers.reporting.keys;

class MethodNameUtils {
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
}
