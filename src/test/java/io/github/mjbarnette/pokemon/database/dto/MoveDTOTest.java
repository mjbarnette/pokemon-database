/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package io.github.mjbarnette.pokemon.database.dto;

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
public class MoveDTOTest {
    
    @Autowired
    private PokemonService instance;
    
    public MoveDTOTest() {
    }
    
   
    /**
     * Test of fromEntity method, of class MoveDTO.
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
        Pokemon pokemon = instance.getPokemonByName("Test1");
        Set<MoveDTO> list = pokemon.getMoves()
                .stream()
                .map(e -> MoveDTO.fromEntity(e))
                .collect(Collectors.toSet());   
        MoveDTO result = list.stream()
                .filter(m -> m.getName().equals("Move2"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Move2 not found"));       
        assertEquals(2, result.getCost(PokemonTypes.Steel));
        assertEquals(50, result.getDamage());
       
    }

    /**
     * Tests creating a MoveDTO object
     */
    @Test
    public void testMoveDTOCreation()
    {
        log.info("Testing MoveDTO Creation");
        HashMap<PokemonTypes, Integer> cost = new HashMap<>();
        cost.put(PokemonTypes.Steel, 2);
        cost.put(PokemonTypes.Normal, 1);
        MoveDTO result = new MoveDTO("Move2", 50, "This is a description", cost);
        assertEquals("Move2", result.getName());
        Set<PokemonTypes> types = result.getEnergyTypes();
        assertTrue(types.stream().anyMatch(m -> m.equals(PokemonTypes.Steel)));
        assertEquals(1, result.getCost(PokemonTypes.Normal));
        assertEquals(50, result.getDamage());
    }
    
}
