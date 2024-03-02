package dev.ceccon.webframework.web;

import dev.ceccon.webframework.annotations.WebframeworkGetMethod;
import dev.ceccon.webframework.annotations.WebframeworkPostMethod;
import dev.ceccon.webframework.datastructures.ControllerMap;
import dev.ceccon.webframework.datastructures.MethodParam;
import dev.ceccon.webframework.datastructures.RequestControllerData;
import dev.ceccon.webframework.datastructures.ServiceImplementationMap;
import dev.ceccon.webframework.explorer.ClassExplorer;
import dev.ceccon.webframework.util.WebFrameworkLogger;
import dev.ceccon.webframework.util.WebFrameworkUtil;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;

public class WebFrameworkWebApplication {

    public static void run(Class<?> sourceClass) {
        // Desligando todos os logs do Apache Tomcat
        java.util.logging.Logger.getLogger("org.apache").setLevel(Level.OFF);
        long ini, fim;

        WebFrameworkLogger.showBanner();

        try {

            // Class explorer
            // Começar a criar um metodo de extracao de metadados:
            /*
            List<String> allClasses = ClassExplorer.retrieveAllClasses(sourceClass);
            allClasses.forEach(c -> WebFrameworkLogger.log("Embedded Web Controller", "Class found: " + c));
            */
            extractMetadata(sourceClass);

            ini = System.currentTimeMillis();

            WebFrameworkLogger.log("Embedded Web Container", "Iniciando WebFrameworkWebApplication");

            Tomcat tomcat = new Tomcat();
            Connector connector = new Connector();
            connector.setPort(8080);
            tomcat.setConnector(connector);
            WebFrameworkLogger.log("Embedded Web Container", "Iniciando na porta 8080");

            // Contexto olhando a raiz da aplicacao

            // Procurando classes na raiz da app
            Context context = tomcat.addContext("", new File(".").getAbsolutePath());
            Tomcat.addServlet(context, "WebFrameworkDispatcherServlet", new WebFrameworkDispatcherServlet());

            // Tudo que digitar na URL vai cair nesse ponto:
            context.addServletMappingDecoded("/*", "WebFrameworkDispatcherServlet");

            fim = System.currentTimeMillis();
            WebFrameworkLogger.log("Embedded Web Container", "Tomcat iniciado em "
            + (double)(fim - ini) / 1000
            + "s");

            // Start:
            tomcat.start();
            tomcat.getServer().await();;
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }

    private static void extractMetadata(Class<?> sourceClass) {
        try {
            List<String> allClasses = ClassExplorer.retrieveAllClasses(sourceClass);

            for (String classe: allClasses) {
                // Recuperar as anotações da classe
                Annotation annotations[] = Class.forName(classe).getAnnotations();
                for (Annotation classAnnotation : annotations) {
                    if (classAnnotation.annotationType().getName().equals("dev.ceccon.webframework.annotations.WebframeworkController")) {
                        WebFrameworkLogger.log("Metada Explorer", "Found a controller: " + classe);
                        extractMethods(classe);
                    } else if (classAnnotation.annotationType().getName().equals("dev.ceccon.webframework.annotations.WebframeworkService")) {
                        WebFrameworkLogger.log("Metada Explorer", "Found a service implementation: " + classe);
                        for (Class<?> interfaceWeb : Class.forName(classe).getInterfaces()) {
                            WebFrameworkLogger.log("Metada Explorer", "  Class implements: " + interfaceWeb.getName());
                            ServiceImplementationMap.implementations.put(interfaceWeb.getName(), classe);
                        }
                    }
                }
            }

            for (RequestControllerData item : ControllerMap.values.values()) {
                WebFrameworkLogger.log("",
                        item.getHttpMethod() + ":" + item.getUrl()
                                    + " [" + item.getControllerClass() + "." + item.getControllerMethod() + "]"
                                    + ((item.getParameter().length() > 0) ? " - Expected parameter " + item.getParameter() : "")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void extractMethods(String className) throws Exception {
        String httpMethod = "";
        String path = "";
        String parameter = "";

        // Recuperar todos os metodos da classe
        for (Method method : Class.forName(className).getDeclaredMethods()) {
            parameter = "";
            //WebFrameworkLogger.log(" - ", method.getName());
            for (Annotation annotation : method.getAnnotations()) {
                if (annotation.annotationType().getName().equals("dev.ceccon.webframework.annotations.WebframeworkGetMethod")) {
                    httpMethod = "GET";
                    path = ((WebframeworkGetMethod) annotation).value();

                    // verificar se existe parametro no path
                    MethodParam methodParam = WebFrameworkUtil.convertURI2MethodParam(path);
                    if (methodParam != null) {
                        path = methodParam.getMethod();
                        if (methodParam.getParam() != null) {
                            parameter = methodParam.getParam();
                        }
                    }
                } else if (annotation.annotationType().getName().equals("dev.ceccon.webframework.annotations.WebframeworkPostMethod")) {
                    httpMethod = "POST";
                    path = ((WebframeworkPostMethod) annotation).value();

                    // verificar se existe parametro no path
                    MethodParam methodParam = WebFrameworkUtil.convertURI2MethodParam(path);
                    if (methodParam != null) {
                        path = methodParam.getMethod();
                        if (methodParam.getParam() != null) {
                            parameter = methodParam.getParam();
                        }
                    }
                }

                WebFrameworkLogger.log(" - CHAVE: ", httpMethod + path);
                RequestControllerData reqData = new RequestControllerData(httpMethod, path, className, method.getName(), parameter);
                ControllerMap.values.put(httpMethod + path, reqData);
            }
        }
    }
}
