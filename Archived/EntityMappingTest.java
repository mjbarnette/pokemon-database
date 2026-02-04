

package io.github.mjbarnette.pokemon.database;

import io.github.mjbarnette.pokemon.database.entity.Pokemon;
import io.github.mjbarnette.pokemon.database.value.EvolutionStage;
import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = PokemonDatabaseApplication.class)
@Transactional
public class EntityMappingTest {
    
    @PersistenceContext
    private EntityManager em;
    
    @Test
    void pokemonPersistsAndLoads() {
        
        Pokemon p = new Pokemon("Test", PokemonTypes.Grass, 220, PokemonTypes.Fire, 3, EvolutionStage.Basic);        

        em.persist(p);
        em.flush();
        em.clear();

        Pokemon loaded = em.find(Pokemon.class, p.getId());
        assertEquals("Test", loaded.getName());

    }

}
