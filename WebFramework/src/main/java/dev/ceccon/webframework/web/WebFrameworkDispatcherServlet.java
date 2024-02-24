package dev.ceccon.webframework.web;

import com.google.gson.Gson;
import dev.ceccon.webframework.datastructures.ControllerInstances;
import dev.ceccon.webframework.datastructures.ControllerMap;
import dev.ceccon.webframework.datastructures.RequestControllerData;
import dev.ceccon.webframework.util.WebFrameworkLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

public class WebFrameworkDispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.service(req, resp);

        // Ignorar o favicon:
        if (req.getRequestURL().toString().endsWith("/favicon.ico")) return;

        PrintWriter out = new PrintWriter(resp.getWriter());
        Gson gson = new Gson();
        String url = req.getRequestURI();
        String httpMethod = req.getMethod().toUpperCase(); // GET, POST ...
        String key = httpMethod + url;

        // busco a informacao da classe, metodo, parametros... da minha req
        RequestControllerData data = ControllerMap.values.get(key);

        WebFrameworkLogger.log("WebFrameworkDispatcherServlet", "URL: " + url + " (" + httpMethod +
                ") - Handler " + data.getControllerClass() + "." + data.getControllerMethod());

        // verificar se existe uma instancia da classes correspondente, caso nao, criar uma
        Object controller;
        WebFrameworkLogger.log("WebFrameworkDispatcherServlet", "Procurar instancia da controladora");
        try {
            controller = ControllerInstances.instance.get(data.controllerClass);
            if (controller == null) {
                WebFrameworkLogger.log("WebFrameworkDispatcherServlet", "Criar nova instancia da controladora");
                controller = Class.forName(data.controllerClass).getDeclaredConstructor()
                        .newInstance(); // HelloController controller = new HelloController();
                ControllerInstances.instance.put(data.controllerClass, controller);
            }

            // Precisamos extrair o metodo desta classe - ou seja o metodo que vai atender
            //  a requisicao. Vamos executar esse metodo e escrever a saida dele.
            Method controllerMethod = null;
            for (Method method : controller.getClass().getMethods()) {
                if (method.getName().equals(data.controllerMethod)) {
                    controllerMethod = method;
                    break;
                }
            }

            WebFrameworkLogger.log("WebFrameworkDispatcherServlet", "Invocar o metodo " + controllerMethod.getName() + " para requisicao");

            out.println(gson.toJson(controllerMethod.invoke(controller)));
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
