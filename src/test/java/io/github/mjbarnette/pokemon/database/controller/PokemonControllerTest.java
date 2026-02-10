package io.github.mjbarnette.pokemon.database.controller;

import io.github.mjbarnette.pokemon.database.dto.CreateEvolutionDTO;
import io.github.mjbarnette.pokemon.database.dto.CreateMoveDTO;
import io.github.mjbarnette.pokemon.database.dto.CreatePokemonDTO;
import io.github.mjbarnette.pokemon.database.dto.EvolutionDTO;
import io.github.mjbarnette.pokemon.database.dto.PokemonDetailDTO;
import io.github.mjbarnette.pokemon.database.dto.PokemonSummaryDTO;
import io.github.mjbarnette.pokemon.database.entity.Pokemon;
import io.github.mjbarnette.pokemon.database.service.PokemonService;
import io.github.mjbarnette.pokemon.database.value.EvolutionStage;
import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@Transactional
public class PokemonControllerTest {
    
    @Autowired
    private PokemonService instance;
    
    public PokemonControllerTest() 
    {
        
    }     

    private void loadTestPokemon()
    {
        Pokemon poke1 = instance.createPokemon("Test1", 
                PokemonTypes.Normal, 
                50, 
                PokemonTypes.Fairy, 
                1, 
                EvolutionStage.Basic);
        HashMap<PokemonTypes, Integer> cost = new HashMap<>();
        cost.put(PokemonTypes.Normal, 1);              
        instance.addNewMove("Test1", "Move1", 0, "This is where description goes", cost);
        cost = new HashMap<>();
        cost.put(PokemonTypes.Steel, 2);
        cost.put(PokemonTypes.Normal, 1);        
        instance.addNewMove("Test1",  "Move2", 50, null, cost);
        log.info("Pokemon add {}", poke1.getName());
        Pokemon poke2 = instance.createPokemon("Test2", 
                PokemonTypes.Normal, 
                100, 
                PokemonTypes.Fairy, 
                2, 
                EvolutionStage.Stage1);
                cost = new HashMap<>();
        cost.put(PokemonTypes.Normal, 2);              
        instance.addNewMove("Test2", "Move1", 50, "This is where description goes", cost);
        cost = new HashMap<>();
        cost.put(PokemonTypes.Steel, 3);        
        cost.put(PokemonTypes.Normal, 2);        
        instance.addNewMove("Test2",  "Move2", 100, null, cost);
        log.info("Pokemon add {}", poke2.getName());
        Pokemon poke3 = instance.createPokemon("Test3", 
                PokemonTypes.Normal, 
                150, 
                PokemonTypes.Fairy, 
                3, 
                EvolutionStage.Stage2);
        log.info("Pokemon add {}", poke3.getName());
        Pokemon poke4 = instance.createPokemon("Test4", 
                PokemonTypes.Normal, 
                172, 
                PokemonTypes.Fairy, 
                4, 
                EvolutionStage.Stage2);
        log.info("Pokemon add {}", poke4.getName());        
        instance.updateEvolution("Test1", "Test2");
        instance.updateEvolution("Test2", "Test3");
        instance.updateEvolution("Test2", "Test4");
    }
    
    /**
     * Test of getAllPokemon method, of class PokemonController.
     */
    @Test
    public void testGetAllPokemon() {
        log.info("getAllPokemon");
        this.loadTestPokemon();
        PokemonController test = new PokemonController(instance);
        List<PokemonSummaryDTO> expResult = test.getAllPokemon(); 
        assertTrue(expResult.stream().anyMatch(m -> m.getName().equals("Skiddo")));  
        assertTrue(expResult.stream().anyMatch(m -> m.getName().equals("Test3"))); 
        assertTrue(expResult.stream().anyMatch(m -> m.getName().equals("Test1")));   
    }

    /**
     * Test of getPokemon method, of class PokemonController.
     */
    @Test
    public void testGetPokemon() {
        System.out.println("getPokemon");
        this.loadTestPokemon();
        PokemonController test = new PokemonController(instance);
        PokemonDetailDTO result = test.getPokemon("Test1");
        assertEquals(50, result.getHitPoints());
        Set<EvolutionDTO> list = result.getEvolutions();
        assertTrue(list.stream().anyMatch(m -> m.getNextPokemonName().equals("Test2")));
        result = test.getPokemon("Test2");
        assertEquals(100, result.getHitPoints());
        list = result.getEvolutions();
        assertTrue(list.stream().anyMatch(m -> m.getNextPokemonName().equals("Test3"))); 
        assertTrue(list.stream().anyMatch(m -> m.getNextPokemonName().equals("Test4")));
    }

    /**
     * Test of createPokemon method, of class PokemonController.
     */
    @Test
    public void testCreatePokemon() {
        System.out.println("createPokemon");        
        CreatePokemonDTO pokemonTest = CreatePokemonDTO.builder()
                .name("Test1")
                .type(PokemonTypes.Normal)
                .hitPoints(50)
                .weakness(PokemonTypes.Fairy)
                .retreatCost(1)
                .stage(EvolutionStage.Basic)
                .imageFile("Test1.jpg")
                .build();
        PokemonController test = new PokemonController(instance);
        PokemonSummaryDTO result = test.createPokemon(pokemonTest);        
        assertEquals("Test1", result.getName());
        assertEquals("Test1.jpg", result.getImageFile());
        assertEquals(PokemonTypes.Normal, result.getType());
        
    }

    /**
     * Test of createMove method, of class PokemonController.
     */
    @Test
    public void testCreateMove() {
        System.out.println("createMove");
        CreatePokemonDTO pokemonTest = CreatePokemonDTO.builder()
               .name("Test1")
               .type(PokemonTypes.Normal)
               .hitPoints(50)
               .weakness(PokemonTypes.Fairy)
               .retreatCost(1)
               .stage(EvolutionStage.Basic)
               .imageFile("Test1.jpg")
               .build();
        PokemonController test = new PokemonController(instance);        
        test.createPokemon(pokemonTest);
        HashMap<PokemonTypes, Integer> cost = new HashMap<>();        
        cost = new HashMap<>();
        cost.put(PokemonTypes.Steel, 2);
        cost.put(PokemonTypes.Normal, 1);             
        CreateMoveDTO move = new CreateMoveDTO("Move1", 50, "This is description", cost);        
        PokemonDetailDTO result = test.createMove(pokemonTest.getName(), move);
        final CreateMoveDTO moveTest = move;
        Exception exception = assertThrows(RuntimeException.class, ()-> {
            test.createMove("Test5", moveTest);            
                });
        String expectedMessage = "Pokemon not found";
        assertTrue(exception.getMessage().contains(expectedMessage));
        assertTrue(result.getMoves().stream().anyMatch(m -> m.getDamage() == 50));
        assertTrue(result.getMoves().stream().anyMatch(m -> m.getCost(PokemonTypes.Steel) == 2));
        cost = new HashMap<>();
        cost.put(PokemonTypes.Steel, 3);
        cost.put(PokemonTypes.Normal, 2);        
        move = new CreateMoveDTO("Move2", 75, null, cost);
        result = test.createMove(pokemonTest.getName(), move);
        assertTrue(result.getMoves().stream().anyMatch(m -> m.getName().equals("Move1")));
        assertTrue(result.getMoves().stream().anyMatch(m -> m.getName().equals("Move2")));
        assertTrue(result.getMoves().stream().anyMatch(m -> m.getDamage() == 75));
        assertTrue(result.getMoves().stream().anyMatch(m -> m.getCost(PokemonTypes.Steel) == 2));
        assertTrue(result.getMoves().stream().anyMatch(m -> m.getDamage() == 50));
        assertTrue(result.getMoves().stream().anyMatch(m -> m.getCost(PokemonTypes.Steel) == 3));
    }

    /**
     * Test of createEvolution method, of class PokemonController.
     */
    @Test
    public void testCreateEvolution() {        
        System.out.println("addEvolutionChain");
        CreatePokemonDTO pokemonTest = CreatePokemonDTO.builder()
               .name("Test1")
               .type(PokemonTypes.Normal)
               .hitPoints(50)
               .weakness(PokemonTypes.Fairy)
               .retreatCost(1)
               .stage(EvolutionStage.Basic)
               .imageFile("Test1.jpg")
               .build();
        PokemonController test = new PokemonController(instance);
        PokemonSummaryDTO sample1 = test.createPokemon(pokemonTest);
        pokemonTest = CreatePokemonDTO.builder()
               .name("Test2")
               .type(PokemonTypes.Normal)
               .hitPoints(75)
               .weakness(PokemonTypes.Fairy)
               .retreatCost(2)
               .stage(EvolutionStage.Stage1)
               .imageFile("Test2.jpg")
               .build();
        PokemonSummaryDTO sample2 = test.createPokemon(pokemonTest);
        pokemonTest = CreatePokemonDTO.builder()
               .name("Test3")
               .type(PokemonTypes.Fire)
               .hitPoints(80)
               .weakness(PokemonTypes.Fairy)
               .retreatCost(2)
               .stage(EvolutionStage.Stage1)
               .imageFile("Test3.jpg")
               .build();
        PokemonSummaryDTO sample3 = test.createPokemon(pokemonTest); 
        CreateEvolutionDTO evo = CreateEvolutionDTO.builder()
                .currentStage(sample1.getStage())
                .nextPokemonName(sample2.getName())
                .nextStage(sample2.getStage())
                .build();
        PokemonDetailDTO result = test.createEvolution(sample1.getName(), evo);
        evo = CreateEvolutionDTO.builder()
               .currentStage(sample1.getStage())
               .nextPokemonName(sample3.getName())
               .nextStage(sample3.getStage())
               .build();
        result = test.createEvolution(sample1.getName(), evo);
        assertTrue(result.getEvolutions().stream().anyMatch(m -> m.getNextPokemonName().equals("Test2")));
        assertTrue(result.getEvolutions().stream().anyMatch(m -> m.getNextPokemonName().equals("Test3")));  

    }
    
}
