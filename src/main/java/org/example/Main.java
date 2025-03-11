package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Huvudklass för Spring Boot-applikationen.
 * Denna klass är den primära startpunkten för applikationen. När applikationen körs,
 * startar Spring Boot och alla konfigurationer definieras av de annoterade klasserna.
 */
@SpringBootApplication
public class Main {

    /**
     * Startar Spring Boot-applikationen.
     * Denna metod är programmet huvudsakliga startpunkt och används för att starta
     * Spring Boot-applikationen. Den kör applikationen genom att använda
     * {@link SpringApplication#run(Class, String[])}.
     *
     * @param args Kommando-rad argument som kan användas för att konfigurera applikationen.
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
