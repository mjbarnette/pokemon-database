package io.github.mjbarnette.pokemon.database;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonDatabaseApplication {

    public static void main(String[] args) {
        //SpringApplication.run(PokemonDatabaseApplication.class, args);
        SpringApplication app = new SpringApplication(PokemonDatabaseApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }
}
