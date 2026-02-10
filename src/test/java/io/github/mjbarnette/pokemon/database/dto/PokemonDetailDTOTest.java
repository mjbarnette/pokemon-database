
package io.github.mjbarnette.pokemon.database.dto;

import io.github.mjbarnette.pokemon.database.entity.Pokemon;
import io.github.mjbarnette.pokemon.database.service.PokemonService;
import io.github.mjbarnette.pokemon.database.value.EvolutionStage;
import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@Transactional
public class PokemonDetailDTOTest {
    
    @Autowired
    private PokemonService instance;
    
    public PokemonDetailDTOTest() {
    }
    
    /**
     * Test of fromEntity method, of class PokemonDetailDTO.
     */
    @Test
    public void testFromEntity() {
        log.info("fromEntity");
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
        PokemonDetailDTO result = PokemonDetailDTO.fromEntity(instance.getPokemonByName("Test1"));            
        assertEquals("Test1", result.getName());
        Set<MoveDTO> moves = result.getMoves();
        MoveDTO move = moves.stream().filter(m -> m.getName().equals("Move2")).findFirst().get();
        assertEquals(2, move.getCost(PokemonTypes.Steel));
        assertEquals(50, result.getHitPoints());
        assertEquals(PokemonTypes.Normal, result.getType());
        assertEquals(EvolutionStage.Basic, result.getStage());        
    }

    private PokemonDetailDTO getNewPokemonDetailDTO()
    {
        String name = "Test1";
        PokemonTypes type = PokemonTypes.Normal;
        int hitPoints = 50;
        PokemonTypes weakness = PokemonTypes.Dragon;
        int retreatCost = 1;
        EvolutionStage stage = EvolutionStage.Basic;
        String imageFile = "Test1.jpg";
        Set<MoveDTO> moves = new HashSet<>();
        Set<EvolutionDTO> evolutions = new HashSet<>();        
        HashMap<PokemonTypes, Integer> cost = new HashMap<>();
        cost.put(PokemonTypes.Normal, 1);              
        MoveDTO move1 = new MoveDTO("Move1", 0, "This is where description goes", cost);
        moves.add(move1);
        cost = new HashMap<>();
        cost.put(PokemonTypes.Steel, 2);
        cost.put(PokemonTypes.Normal, 1);        
        MoveDTO move2 = new MoveDTO("Move2", 50, null, cost);
        moves.add(move2);
        evolutions.add(new EvolutionDTO(stage, "Test2", EvolutionStage.Stage1));
        evolutions.add(new EvolutionDTO(stage, "Test3", EvolutionStage.Stage1));
        PokemonDetailDTO result = new PokemonDetailDTO(name, type, hitPoints, weakness, retreatCost, stage, imageFile, moves, evolutions);
  
        return result;
    }
    /**
     * Test PokemonDetailDTO creation and basic getter methods.
     */
    @Test
    public void testPokemonDetailDTOCreation()
    {
        log.info("Creating PokemonDetail");
        PokemonDetailDTO result = this.getNewPokemonDetailDTO();
        assertEquals("Test1", result.getName());
        assertEquals(50, result.getHitPoints());
        assertEquals(PokemonTypes.Dragon, result.getWeakness());
        assertEquals(PokemonTypes.Normal, result.getType());
    }

    /**
     * Test of getMoves method, of class PokemonDetailDTO.
     */
    @Test
    public void testGetMoves() 
    {
        log.info("testGetMoves");
       PokemonDetailDTO result = this.getNewPokemonDetailDTO();
       Set<MoveDTO> list = result.getMoves();
       log.info(list.iterator().next().getName());
       MoveDTO testMove = (list.stream()
               .filter(m -> m.getName().equals("Move2"))
               .findFirst()
               .orElseThrow(() -> new AssertionError("Move2 not found")));
       log.info(testMove.getName());
       assertEquals(2, testMove.getCost(PokemonTypes.Steel));
       assertEquals(50, testMove.getDamage());
    }

    /**
     * Test of getEvolutions method, of class PokemonDetailDTO.
     */
    @Test
    public void testGetEvolutions() {
        PokemonDetailDTO result = this.getNewPokemonDetailDTO();
        Set<EvolutionDTO> list = result.getEvolutions();
        assertTrue(list.stream().anyMatch(m -> m.getNextPokemonName().equals("Test2")));
        assertTrue(list.stream().anyMatch(m -> m.getNextPokemonName().equals("Test3")));        
    }
    
}
