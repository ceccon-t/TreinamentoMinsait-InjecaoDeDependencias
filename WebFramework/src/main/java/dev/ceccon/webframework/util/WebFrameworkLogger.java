package dev.ceccon.webframework.util;

import jakarta.annotation.Resources;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class WebFrameworkLogger {

    // cores:
    private static final String VERDE = "\u001B[32m";
    private static final String AMARELO = "\u001B[33m";
    private static final String BRANCO = "\u001B[37m";
    private static final String RESET = "\u001B[0m";

    public static DateTimeFormatter WEBFRAMEWORKDATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void showBanner() {
        System.out.println(AMARELO);

        try {
            Files.readAllLines( Paths.get("src/main/resources/banner.txt")).forEach(l -> System.out.println(l));
        } catch (IOException e) {
            System.out.println("========================");
            System.out.println("===== WEBFRAMEWORK =====");
            System.out.println("========================");
        }
        System.out.println(RESET);
    }

    public static void log(String modulo, String mensagem) {
        String date = LocalDateTime.now().format(WEBFRAMEWORKDATE);
        System.out.printf(VERDE + "%15s " + AMARELO + "[%-30s]: " + BRANCO + "%s\n" + RESET, date, modulo, mensagem);
    }

}
