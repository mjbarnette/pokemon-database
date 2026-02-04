

package io.github.mjbarnette.pokemon.databse;

import java.io.IOException;
import org.flywaydb.core.Flyway;

public class Main {
    public static void main(String[] args) throws IOException {
        // Run migrations
        Flyway flyway = Flyway.configure().dataSource(
                    "jdbc:postgresql://localhost:5432/Pokemon-Testing", 
                    "postgres", 
                    "Dragon01")
                    .locations("classpath:db/migration")
                    .load();
        
        flyway.migrate();
        
        // Now use your DAO
       
    }
}

