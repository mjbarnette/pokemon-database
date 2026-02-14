
package io.github.mjbarnette.pokemon.database.repository;

import io.github.mjbarnette.pokemon.database.entity.Moves;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MovesRepository extends JpaRepository<Moves, Long> {
    
    Optional<Moves> findByNameAndPokemon_Name(String moveName, String pokemonName);
    
    Set<Moves> findByPokemonName(String name);
    
}
