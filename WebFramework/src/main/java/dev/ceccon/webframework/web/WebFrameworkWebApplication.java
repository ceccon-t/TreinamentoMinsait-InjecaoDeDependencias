package dev.ceccon.webframework.web;

import dev.ceccon.webframework.explorer.ClassExplorer;
import dev.ceccon.webframework.util.WebFrameworkLogger;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
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
            List<String> allClasses = ClassExplorer.retrieveAllClasses(sourceClass);
            allClasses.forEach(c -> WebFrameworkLogger.log("Embedded Web Controller", "Class found: " + c));

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
}
