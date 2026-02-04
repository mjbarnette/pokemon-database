
package io.github.mjbarnette.pokemon.database.repository;

import io.github.mjbarnette.pokemon.database.entity.Pokemon;
import io.github.mjbarnette.pokemon.database.entity.Pokestats;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PokestatsRepository extends JpaRepository<Pokestats, Long> {  
    
}
