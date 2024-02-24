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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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

            // meu metodo tem parametros?
            if (controllerMethod.getParameterCount() > 0) {
                WebFrameworkLogger.log("WebFrameworkDispatcherServlet", "Metodo " + controllerMethod.getName() + " tem parametros!");
                Object arg;
                Parameter parameter = controllerMethod.getParameters()[0];
                if (parameter.getAnnotations()[0].annotationType().getName()
                        .equals("dev.ceccon.webframework.annotations.WebframeworkBody")) {

                    WebFrameworkLogger.log("", "Procurando parametro da requisicao do tipo " + parameter.getType().getName());
                    String body = readBytesFromRequest(req);

                    WebFrameworkLogger.log("", "Conteudo do parametro: " + body);

                    arg = gson.fromJson(body, parameter.getType());

                    WebFrameworkLogger.log("WebFrameworkDispatcherServlet",
                            "Invocar o metodo " + controllerMethod.getName() +
                                      " com o parametro do tipo " + parameter.getType().toString() +
                                      " para requisicao");
                    out.println(gson.toJson(controllerMethod.invoke(controller, arg)));
                }
            } else {
                WebFrameworkLogger.log("WebFrameworkDispatcherServlet", "Invocar o metodo " + controllerMethod.getName() + " para requisicao");

                out.println(gson.toJson(controllerMethod.invoke(controller)));
            }

            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readBytesFromRequest(HttpServletRequest req) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(req.getInputStream()));
        while((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
