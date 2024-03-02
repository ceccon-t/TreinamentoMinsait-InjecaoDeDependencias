package dev.ceccon.webframework.util;

import dev.ceccon.webframework.datastructures.MethodParam;
import jdk.jshell.spi.ExecutionControl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebFrameworkUtil {

    public static MethodParam convertURI2MethodParam(String requestURI) {
        Pattern pattern = Pattern.compile("([^/]+)(?:/([^/]+))?");
        Matcher matcher = pattern.matcher(requestURI);

        if (matcher.find()) {
            String method = matcher.group(1);
            String param  = matcher.group(2);

            MethodParam methodParam = new MethodParam();
            methodParam.setMethod("/" + method);
            if (param != null) {
                methodParam.setParam(param);
            }
            return methodParam;
        } else {
            WebFrameworkLogger.log("WebFrameworkUtil", "A URI não corresponde ao padrão esperado.");
        }
        return null;
    }

    public static Object convert2Type(String value, Class<?> type) {
        if (value == null) {
            value = "";
            WebFrameworkLogger.log("WebFrameworkUtil", "Parametro passado eh nulo");
        }

        try {

            if (type.isAssignableFrom(String.class)) {
                return value;
            } else if (type.isAssignableFrom(Integer.class) || type.getName().equals("int")) {
                if (isNumeric(value)) return Integer.parseInt(value);
                return 0;
            } else if (type.isAssignableFrom(Double.class) || type.getName().equals("double")) {
                if (isNumeric(value)) return Double.parseDouble(value);
                return 0.0;
            }

        } catch (Exception e) {
            WebFrameworkLogger.log("WebFrameworkUtil", "Erro ao converter parametro em objeto");

            if (type.isAssignableFrom(String.class)) return "";
            else if (type.isAssignableFrom(Integer.class) || type.getName().equals("int")) return 0;
            else if (type.isAssignableFrom(Double.class) || type.getName().equals("double")) return 0.0;

            return null;
        }

        return null;
    }

    private static boolean isNumeric(String value) {
        if (value == null) return false;

        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
