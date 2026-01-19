/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package io.github.mjbarnette.pokemon.databse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;


/**
 *
 * @author mjbar
 */
@TestMethodOrder(OrderAnnotation.class)
public class JdbcPokemonDaoTest {
    
     private JdbcPokemonDao instance;
    
    public JdbcPokemonDaoTest() throws IOException {
        Properties props = new Properties();
        try (InputStream in =
             getClass().getClassLoader().getResourceAsStream("db-test.properties")) {

            props.load(in);
        }
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        instance = new JdbcPokemonDao(url, user, password);
        
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of save method, of class JdbcPokemonDao.
     */
    @Test
    @Order(1)
    public void testSave() throws Exception {
        System.out.println("save");
        Pokemon koraidon = new Pokemon("Koraidon", "Koraidon.png", PokemonTypes.Fighting, 130, EvolutionStage.Basic, PokemonTypes.Psychic, 2);
        instance.save(koraidon);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of findByName method, of class JdbcPokemonDao.
     */
    @Test
    @Order(2)
    public void testFindByName() throws Exception {
        System.out.println("findByName: Koraidon");        
        Pokemon result = instance.findByName("Koraidon");
        assertNotNull(result);
        assertEquals("Koraidon", result.getName());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getAllPokemon method, of class JdbcPokemonDao.
     */
    @Test
    @Order(3)
    public void testGetAllPokemon() throws Exception {
        System.out.println("getAllPokemon");
        SortOrder orderBy = SortOrder.pokemon_name;        
        int expResult = 9;
        List<Pokemon> result = instance.getAllPokemon(orderBy);
        assertEquals(expResult, result.size());
        // TODO change test later to test more accurately.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getAllPokemonOfType method, of class JdbcPokemonDao.
     */
    @Test
    @Order(4)
    public void testGetAllPokemonOfType() throws Exception {
        System.out.println("getAllPokemonOfType");
        PokemonTypes type = PokemonTypes.Fighting;       
        int expResult = 4;
        List<Pokemon> result = instance.getAllPokemonOfType(type);
        assertEquals(expResult, result.size());
        // TODO change test later to test more accurately.
        ///fail("The test case is a prototype.");
    }

    /**
     * Test of getEvolution method, of class JdbcPokemonDao.
     */
    @Test
    @Order(7)
    public void testGetEvolution() throws Exception {
        System.out.println("getEvolution");
        String name = "Meditite";       
        String expResult1 = "Meditite";
        String expResult2 = "Medicham";
        List<Pokemon> result = instance.getEvolution(name);
        assertEquals(expResult1, result.get(0).getName());
        assertEquals(expResult2, result.get(1).getName());
        instance.deletePokemon("Meditite");        
        instance.deletePokemon("Medicham");
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of deletePokemon method, of class JdbcPokemonDao.
     */
    @Test
    @Order(6)
    public void testDeletePokemon() throws Exception {
        System.out.println("Delete Koraidon");
        String name = "Koraidon";        
        instance.deletePokemon(name);
        Pokemon result = instance.findByName("Koraidon");        
        assertEquals(null, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setEvolution method, of class JdbcPokemonDao.
     */
    @Test
    @Order(5)
    public void testSetEvolution() throws Exception {
        System.out.println("setEvolution");         
        Pokemon current = new Pokemon("Meditite", "Meditite.png", PokemonTypes.Fighting, 60, EvolutionStage.Basic, PokemonTypes.Psychic, 1);
        instance.save(current);
        Pokemon nextStage = new Pokemon("Medicham", "Medicham.png", PokemonTypes.Fighting, 90, EvolutionStage.Stage1, PokemonTypes.Psychic, 1);
        instance.save(nextStage);        
        instance.setEvolution(current, nextStage);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
