package io.github.mjbarnette.pokemon.database.repository;


import io.github.mjbarnette.pokemon.database.entity.Pokeevolutions;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PokeevolutionsRepository extends JpaRepository<Pokeevolutions, Long> {
    
    Set<Pokeevolutions> findByPokemonName(String name);
}
