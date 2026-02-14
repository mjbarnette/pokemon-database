package io.github.mjbarnette.pokemon.database.service;

import io.github.mjbarnette.pokemon.database.entity.Pokemon;
import io.github.mjbarnette.pokemon.database.value.CsvErrorCodes;
import io.github.mjbarnette.pokemon.database.value.EvolutionStage;
import io.github.mjbarnette.pokemon.database.value.PokemonErrorCode;
import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
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
     * This test is designed to load multiple Pokemon in with a complete evolution chain.
     * Do all individual methods work together to enter a complete Pokemon set?
     */
    @Test
    public void test_Load_Multiple_Complete_Pokemon_With_Evolution_Chain()
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
        instance.addNewMove("Test3", "Move1", 50, "This is where description goes", cost);
        log.info("Pokemon add {}", poke3.getName());
        
        Pokemon poke4 = instance.createPokemon("Test4", 
                PokemonTypes.Normal, 
                172, 
                PokemonTypes.Fairy, 
                4, 
                EvolutionStage.Stage2);
        instance.addNewMove("Test4",  "Move2", 100, null, cost);
        log.info("Pokemon add {}", poke4.getName());        
        instance.updateEvolution("Test1", "Test2");
        instance.updateEvolution("Test2", "Test3");
        instance.updateEvolution("Test2", "Test4");
        Pokemon result = instance.getPokemonByName("Test2");
        assertTrue(result.getEvolutions().stream().anyMatch(m -> m.getNextPokemon().getName().equals("Test3")));
        assertTrue(result.getEvolutions().stream().anyMatch(m -> m.getNextPokemon().getName().equals("Test4")));
        assertEquals(2, result.getMoves().size());
    }
    
    private Pokemon loadSinglePokemonTest()
    {
        Pokemon pokemon = instance.createPokemon("Test1", 
                PokemonTypes.Normal, 
                50, 
                PokemonTypes.Fairy, 
                1, 
                EvolutionStage.Basic);
        return pokemon;
    }
    
    
    /**
     * Test of getAllPokemon method, of class PokemonService.
     */
    @Test
    @DisplayName("Creating objects out of All Pokemon")
    public void testGetAllPokemon() {
       this.loadSinglePokemonTest();
       List<Pokemon> list = instance.getAllPokemon();
       //Tests that a non-null list is returned.
       assertNotNull(list);       
       //Tests that at least 1 Pokemon is present.
       assertTrue(list.size() > 0);
    }

    /**
     * Test of getPokemonByName method, of class PokemonService.
     */
    @Test    
    public void testGetPokemonByName() {       
        loadSinglePokemonTest();
        //Tests that Pokemon Test1 was added to table.
        Pokemon pokemon = instance.getPokemonByName("Test1");
        assertEquals(50, pokemon.getStats().getHitPoints());
        assertEquals(EvolutionStage.Basic, pokemon.getCurrentStage()); 
        //Tests that correct exception and message was thrown when not found.
        PokemonDomainException exception = assertThrows(PokemonDomainException.class, () -> instance.getPokemonByName("Test2"));
        assertEquals(PokemonErrorCode.POKEMON_NOT_FOUND, exception.status());
    }

    /**
     * Test of updateEvolution method, of class PokemonService.
     */
    @Test
    public void testUpdateEvolution() {
        this.loadSinglePokemonTest();
        Pokemon poke2 = instance.createPokemon("Test2", 
                PokemonTypes.Normal, 
                100, 
                PokemonTypes.Fairy, 
                2, 
                EvolutionStage.Stage1);
        //Test that evolution from Test1 to Test2 works
        instance.updateEvolution("Test1", "Test2");
        Pokemon result = instance.getPokemonByName("Test1");
        assertTrue(result.getEvolutions().stream().anyMatch(m -> m.getNextPokemon().getName().equals("Test2")));
        //Tests that duplicate Evolutions can not be added and correct message is returned.
        PokemonDomainException exception = assertThrows(PokemonDomainException.class, () -> instance.updateEvolution("Test1", "Test2"));
        assertEquals(PokemonErrorCode.DUPLICATE_EVOLUTION, exception.status());
    }

    /**
     * Test of createPokemon method, of class PokemonService.
     * Pokemon is added to Table and Stats are created.
     */
    @Test
    public void testCreatePokemon() {        
        //Tests pokemon was created and returned.
        Pokemon poke1 = this.loadSinglePokemonTest();
        assertEquals("Test1", poke1.getName());
        assertEquals(50, poke1.getStats().getHitPoints());
        
        //Tests pokemon duplicate was not created and original data was preserved.
        Pokemon poke2 = instance.createPokemon("Test1", 
                PokemonTypes.Dragon, 
                100, 
                PokemonTypes.Bug, 
                2, 
                EvolutionStage.Stage1);
        assertNotEquals(100, poke2.getStats().getHitPoints());   
        
    }

    /**
     * Test of addImage method, of class PokemonService.
     */
    @Test
    public void testAddImage() {
        this.loadSinglePokemonTest();
        instance.addImage("Test1", "Test1.jpg");
        //Checks that image was added
        assertEquals("Test1.jpg", instance.getPokemonByName("Test1").getImagefile());
        //Checks that image can be updated
        instance.addImage("Test1", "Test1New.png");
        assertEquals("Test1New.png", instance.getPokemonByName("Test1").getImagefile());        
    }

    /**
     * Test of addNewMove method, of class PokemonService.
     */
    @Test
    public void testAddNewMove() {
        this.loadSinglePokemonTest();
        HashMap<PokemonTypes, Integer> cost = new HashMap<>();
        cost.put(PokemonTypes.Normal, 1);
        //Adding a single move
        instance.addNewMove("Test1", "Move1", 0, "This is where description goes", cost);        
        assertTrue(instance.getPokemonByName("Test1").getMoves().stream().anyMatch(m -> m.getName().equals("Move1")));
        
        //Checking duplicate moves aren't added and data is presevered.
        PokemonDomainException exception = assertThrows(PokemonDomainException.class, () -> instance.addNewMove("Test1", "Move1", 45, "This is where description goes", cost));
        assertEquals(PokemonErrorCode.DUPLICATE_MOVE, exception.status());        
    }
    
    @Test
    public void testLoadCsvFiles() throws IOException
    {
        File pokemon = new File("src/test/java/io/github/mjbarnette/pokemon/database/testfiles/Pokemon.csv");        
        File moves = new File("src/test/java/io/github/mjbarnette/pokemon/database/testfiles/Moves.csv");
        File evolutions = new File("src/test/java/io/github/mjbarnette/pokemon/database/testfiles/Evolutions.csv");
        Map<CsvErrorCodes, List<String>> errorLog = instance.loadCsvFiles(pokemon, moves, evolutions);
        Pokemon poke1 = instance.getPokemonByName("Pikachu");
        assertEquals(60, poke1.getStats().getHitPoints());
        assertEquals("Pikachu.jpg", poke1.getImagefile());
        assertFalse(poke1.getMoves().isEmpty());
        assertTrue(errorLog.get(CsvErrorCodes.DUPLICATE_POKEMON_IN_TABLE).stream().anyMatch(m -> m.contains("Venusaur V")));
        assertTrue(errorLog.get(CsvErrorCodes.DUPLICATE_POKEMON_IN_FILE).stream().anyMatch(m -> m.contains("Pikachu")));
        assertTrue(errorLog.get(CsvErrorCodes.NO_MATCH_EVOLUTION).stream().anyMatch(m -> m.contains("WrongOne")));
        assertTrue(errorLog.get(CsvErrorCodes.NO_MATCH_MOVE).stream().anyMatch(m -> m.contains("WrongOne")));
    }
}
