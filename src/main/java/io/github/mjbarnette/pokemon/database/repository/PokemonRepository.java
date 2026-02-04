
package io.github.mjbarnette.pokemon.database.repository;

import io.github.mjbarnette.pokemon.database.entity.Pokemon;
import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
    
    Optional<Pokemon> findByName(String name);
    
    Optional<Pokemon> findByNameIgnoreCase(String name);
    
    Set<Pokemon> findByStatsType(PokemonTypes type);
    
}
