package dev.ceccon.webframework.explorer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ClassExplorer {

    public static List<String> retrieveAllClasses(Class<?> sourceClass) {
        return packageExplorer(sourceClass.getPackageName());
    }

    private static List<String> packageExplorer(String packageName) {
        ArrayList<String> classNames = new ArrayList<>();

        try {
            // Dado a pasta onde tenho os pacotes do projeto com getResourceAsStream, defino raiz como package
            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("\\.", "/"));
            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(stream));
            String line;
            while((line = reader.readLine()) != null) {
                if(line.endsWith(".class")) {
                    String className = packageName + "." + line.substring(0, line.lastIndexOf(".class"));
                    classNames.add(className);
                } else {
                    // Recursividade
                    classNames.addAll(packageExplorer(packageName + "." + line));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classNames;
    }
}
