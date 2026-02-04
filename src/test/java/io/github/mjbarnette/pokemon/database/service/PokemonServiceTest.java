package io.github.mjbarnette.pokemon.database.service;

import io.github.mjbarnette.pokemon.database.entity.Moves;
import io.github.mjbarnette.pokemon.database.entity.Pokeevolutions;
import io.github.mjbarnette.pokemon.database.entity.Pokemon;
import io.github.mjbarnette.pokemon.database.value.EvolutionStage;
import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@Transactional
public class PokemonServiceTest {
    
    @Autowired
    private PokemonService instance;
    
    public PokemonServiceTest() {
        
    }   

    /**
     * Test of getAllPokemon method, of class PokemonService.
     */
    @Test
    public void testGetAllPokemon() {
        int startSize = instance.getAllPokemon().size();
        Pokemon poke1 = instance.createPokemon("Test1", PokemonTypes.Normal, 50, PokemonTypes.Fairy, 1, EvolutionStage.Basic);
        log.info("Pokemon add {}", poke1.getName());
        Pokemon poke2 = instance.createPokemon("Test2", PokemonTypes.Normal, 100, PokemonTypes.Fairy, 2, EvolutionStage.Stage1);
        log.info("Pokemon add {}", poke2.getName());
        Pokemon poke3 = instance.createPokemon("Test3", PokemonTypes.Normal, 150, PokemonTypes.Fairy, 3, EvolutionStage.Stage2);                     
        log.info("Pokemon add {}", poke3.getName());
        List<Pokemon> result = instance.getAllPokemon();
        assertEquals(startSize + 3, result.size());
        assertTrue(result.stream().anyMatch(m -> m.getName().equals("Test3")));
    }

    /**
     * Test of getPokemonByName method, of class PokemonService.
     */
    @Test
    public void testGetPokemonByName() {
        log.info("Test getPokemonByName");
        Pokemon poke1 = instance.createPokemon("Test1", PokemonTypes.Normal, 50, PokemonTypes.Fairy, 1, EvolutionStage.Basic);
        log.info("Pokemon add {}", poke1.getName());
        Pokemon poke2 = instance.createPokemon("Test2", PokemonTypes.Normal, 100, PokemonTypes.Fairy, 2, EvolutionStage.Stage1);
        log.info("Pokemon add {}", poke2.getName());
        Pokemon poke3 = instance.createPokemon("Test3", PokemonTypes.Normal, 150, PokemonTypes.Fairy, 3, EvolutionStage.Stage2);                     
        log.info("Pokemon add {}", poke3.getName());
        String name = "Test1"; 
        Pokemon result = instance.getPokemonByName(name);
        log.info("{} hitpoints test", name);        
        assertEquals(name, result.getName());
        assertEquals(50, result.getStats().getHitPoints());        
        name = "Test3"; 
        result = instance.getPokemonByName(name);
        log.info("{} stage test", name);
        assertEquals(EvolutionStage.Stage2, result.getCurrentStage());              
    }

    /**
     * Test of updateEvolution method, of class PokemonService.
     */
    @Test
    public void testUpdateEvolution() {
        log.info("Test updateEvolution");
        Pokemon poke1 = instance.createPokemon("Test1", PokemonTypes.Normal, 50, PokemonTypes.Fairy, 1, EvolutionStage.Basic);
        log.info("Pokemon add {}", poke1.getName());
        Pokemon poke2 = instance.createPokemon("Test2", PokemonTypes.Normal, 100, PokemonTypes.Fairy, 2, EvolutionStage.Stage1);
        log.info("Pokemon add {}", poke2.getName());
        Pokemon poke3 = instance.createPokemon("Test3", PokemonTypes.Normal, 150, PokemonTypes.Fairy, 3, EvolutionStage.Stage2);
        log.info("Pokemon add {}", poke3.getName());
        Pokemon poke4 = instance.createPokemon("Test4", PokemonTypes.Normal, 172, PokemonTypes.Fairy, 4, EvolutionStage.Stage2);        
        instance.updateEvolution("Test1", "Test2");
        instance.updateEvolution("Test2", "Test3");
        instance.updateEvolution("Test2", "Test4");
        log.info("updateEvolution for {} and {} ", poke1.getName(), poke2.getName());
        log.info("updateEvolution for {} and {} ", poke2.getName(), poke3.getName());
        assertEquals(1, instance.getPokemonByName("Test1").getEvolutions().size());
        assertEquals(2, instance.getPokemonByName("Test2").getEvolutions().size());
        String currentPokemon = "Test1";
        String nextPokemon = "Test2";        
        Pokemon result = instance.getPokemonByName(currentPokemon);
        assertEquals(nextPokemon, result.getEvolutions().iterator().next().getNextPokemon().getName());
        Set<Pokeevolutions> evoList = instance.getPokemonByName(nextPokemon).getEvolutions();
        assertTrue(evoList.stream().anyMatch(m -> m.getNextPokemon().getName().equals("Test3")));
        assertTrue(evoList.stream().anyMatch(m -> m.getNextPokemon().getName().equals("Test4")));           
    }

    /**
     * Test of createPokemon method, of class PokemonService.
     */
    @Test
    public void testCreatePokemon() {        
        log.info("Test createPokemon");
        String name = "Test1";
        PokemonTypes type = PokemonTypes.Normal;
        int hitPoints = 50;
        PokemonTypes weakness = PokemonTypes.Psychic;
        int retreatCost = 1;
        EvolutionStage stage = EvolutionStage.Basic; 
        Pokemon result = instance.createPokemon(name, type, hitPoints, weakness, retreatCost, stage);
        assertEquals(name, result.getName());
        assertEquals(hitPoints, result.getStats().getHitPoints());        
    }

    /**
     * Test of addImage method, of class PokemonService.
     */
    @Test
    public void testAddImage() {
        log.info("Test addImage");
        Pokemon poke1 = instance.createPokemon("Test1", PokemonTypes.Normal, 50, PokemonTypes.Fairy, 1, EvolutionStage.Basic);
        Pokemon poke2 = instance.createPokemon("Test2", PokemonTypes.Normal, 100, PokemonTypes.Fairy, 2, EvolutionStage.Stage1);        
        log.info("Pokemon add {}", poke1.getName());
        String imageFile = "Test1.jpg";
        instance.addImage("Test1", imageFile); 
        Pokemon result = instance.getPokemonByName("Test1");
        assertEquals(imageFile, result.getImagefile()); 
        result = instance.getPokemonByName("Test2");
        assertEquals(null, result.getImagefile());         
    }

    /**
     * Test of addNewMove method, of class PokemonService.
     */
    @Test
    public void testAddNewMove() {
        log.info("Test addNewMove");
        Pokemon poke1 = instance.createPokemon("Test1", PokemonTypes.Normal, 50, PokemonTypes.Fairy, 1, EvolutionStage.Basic);
        HashMap<PokemonTypes, Integer> cost = new HashMap<>();
        cost.put(PokemonTypes.Normal, 1);              
        instance.addNewMove("Test1", "Move1", 0, "This is where description goes", cost);
        cost = new HashMap<>();
        cost.put(PokemonTypes.Steel, 2);
        cost.put(PokemonTypes.Normal, 1);        
        instance.addNewMove("Test1",  "Move2", 50, null, cost);
        Pokemon result = instance.getPokemonByName("Test1");
        Set<Moves> list = result.getMoves();
        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(m -> m.getName().equals("Move1")));
        assertTrue(list.stream().anyMatch(m -> m.getDamage() == 50));
        
    }

    /**
     * Test of debugPokemon method, of class PokemonService.
     */
    @Test
    public void testDebugPokemon() {
        log.info("debugPokemon");
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
        Pokemon result2 = instance.getPokemonByName("Test2");
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result1.getStats());
        assertFalse(result2.getMoves().isEmpty());
        assertFalse(result1.getEvolutions().isEmpty());
    }
}
