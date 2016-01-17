package com.github.alkedr.matchers.reporting.extraction;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

// TODO: тесты
public enum MethodKind {
    REGULAR_METHOD {
        @Override
        public String invocationToString(String methodName, Object... arguments) {
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
    },

    GETTER_METHOD {
        @Override
        public String invocationToString(String methodName, Object... arguments) {
            if (methodName == null) {
                return "";
            }
            if (methodName.length() > 3 && methodName.startsWith("get") && isUpperCase(methodName.charAt(3))) {
                return toLowerCase(methodName.charAt(3)) + methodName.substring(4);
            }
            if (methodName.length() > 2 && methodName.startsWith("is") && isUpperCase(methodName.charAt(2))) {
                return toLowerCase(methodName.charAt(2)) + methodName.substring(3);
            }
            return methodName;
        }
    },

    ;

    public abstract String invocationToString(String methodName, Object... arguments);
}
