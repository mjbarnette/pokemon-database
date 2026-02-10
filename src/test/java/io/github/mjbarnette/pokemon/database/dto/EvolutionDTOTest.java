
package io.github.mjbarnette.pokemon.database.dto;

import io.github.mjbarnette.pokemon.database.entity.Pokeevolutions;
import io.github.mjbarnette.pokemon.database.entity.Pokemon;
import io.github.mjbarnette.pokemon.database.service.PokemonService;
import io.github.mjbarnette.pokemon.database.value.EvolutionStage;
import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@Transactional
public class EvolutionDTOTest {
    
    @Autowired
    private PokemonService instance;
    
    public EvolutionDTOTest() {
    }
    
    
    /**
     * Test of fromEntity method, of class EvolutionDTO.
     */
    @Test
    public void testFromEntity() {
        log.info("testFromEntity");
        log.info("Create Pokemon #1");
        Pokemon poke1 = instance.createPokemon("Test1", PokemonTypes.Normal, 50, PokemonTypes.Fairy, 1, EvolutionStage.Basic);
        HashMap<PokemonTypes, Integer> cost = new HashMap<>();
        cost.put(PokemonTypes.Normal, 1);              
        instance.addNewMove("Test1", "Move1", 0, "This is where description goes", cost);
        cost = new HashMap<>();
        cost.put(PokemonTypes.Steel, 2);
        cost.put(PokemonTypes.Normal, 1);        
        instance.addNewMove("Test1",  "Move2", 50, null, cost);
        instance.addImage("Test1", "Test1.jpg");
        
        log.info("Create Pokemon #2");
        Pokemon poke2 = instance.createPokemon("Test2", PokemonTypes.Normal, 100, PokemonTypes.Fairy, 2, EvolutionStage.Stage1);        
        cost = new HashMap<>();
        cost.put(PokemonTypes.Normal, 2);              
        instance.addNewMove("Test2", "Move1", 50, "This is where description goes", cost);
        cost = new HashMap<>();
        cost.put(PokemonTypes.Steel, 3);        
        cost.put(PokemonTypes.Normal, 2);        
        instance.addNewMove("Test2",  "Move2", 100, null, cost);
        instance.addImage("Test2", "Test2.jpg");        
        instance.updateEvolution("Test1", "Test2");
        Pokemon result1 = instance.getPokemonByName("Test1");
        Set<Pokeevolutions> evo = result1.getEvolutions();
        Set<EvolutionDTO> list = result1.getEvolutions()
                .stream()
                .map(e -> EvolutionDTO.fromEntity(e))
                .collect(Collectors.toSet());
        assertTrue(list.stream().anyMatch(m -> m.getNextPokemonName().equals("Test2")));        
    }
    
    @Test
    public void testEvolutionDTOCreation()
    {
        EvolutionDTO test = new EvolutionDTO(EvolutionStage.Basic, "Test2", EvolutionStage.Stage1);
        assertEquals(EvolutionStage.Basic, test.getCurrentStage());
        assertEquals("Test2", test.getNextPokemonName());
        assertEquals(EvolutionStage.Stage1, test.getNextStage());
        
    }
    
}
