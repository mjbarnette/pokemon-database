package io.github.mjbarnette.pokemon.database.controller;


import io.github.mjbarnette.pokemon.database.entity.Pokemon;
import io.github.mjbarnette.pokemon.database.service.PokemonDomainException;
import io.github.mjbarnette.pokemon.database.service.PokemonService;
import io.github.mjbarnette.pokemon.database.value.EvolutionStage;
import io.github.mjbarnette.pokemon.database.value.PokemonErrorCode;
import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;



@Slf4j
@WebMvcTest(PokemonController.class)
public class PokemonControllerTest {
    
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private PokemonService pokemonService;
    
    public PokemonControllerTest() 
    {
        
    }        
    
   @Test
    void createMove_success_returnsUpdatedPokemon() throws Exception {
        
        Pokemon pokemon = new Pokemon(
                "Pikachu",
                PokemonTypes.Electric,
                60,
                PokemonTypes.Fighting,
                1,
                EvolutionStage.Basic
        );

        HashMap<PokemonTypes, Integer> cost = new HashMap<>();
        cost.put(PokemonTypes.Electric, 1);
        pokemon.addMoves("Thunder Shock", 30, "Zap!", cost);
        
        doNothing().when(pokemonService)
                .addNewMove(eq("Pikachu"),
                            eq("Thunder Shock"),
                            eq(30),
                            eq("Zap!"),
                            any());

        when(pokemonService.getPokemonByName("Pikachu"))
                .thenReturn(pokemon);

        String jsonBody = """
            {
              "name": "Thunder Shock",
              "damage": 30,
              "description": "Zap!",
              "energyCosts": {
                "Electric": 1
              }
            }
            """;
       
        mockMvc.perform(post("/pokemon/Pikachu/moves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pikachu"))
                .andExpect(jsonPath("$.moves[0].name").value("Thunder Shock"))
                .andExpect(jsonPath("$.moves[0].damage").value(30));
       
        verify(pokemonService).addNewMove(
                eq("Pikachu"),
                eq("Thunder Shock"),
                eq(30),
                eq("Zap!"),
                any()
        );
    }
    
    @Test
    void createMove_duplicateMove_returnsBadRequest() throws Exception {

        doThrow(new PokemonDomainException(
                PokemonErrorCode.DUPLICATE_MOVE, "Move Already Exists: Thunder Shock for Pikachu"))
                .when(pokemonService)
                .addNewMove(eq("Pikachu"),
                            eq("Thunder Shock"),
                            eq(30),
                            eq("Zap!"),
                            any());

        String jsonBody = """
            {
              "name": "Thunder Shock",
              "damage": 30,
              "description": "Zap!",
              "energyCosts": {
                "Electric": 1
              }
            }
            """;
       
        mockMvc.perform(post("/pokemon/Pikachu/moves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("DUPLICATE_MOVE"))
                .andExpect(jsonPath("$.message")
                        .value("Move Already Exists: Thunder Shock for Pikachu"));
    }

    
    @Test
    void createEvolution_success_returnUpdatedPokemon() throws Exception
    {
        Pokemon pokemon = new Pokemon(
                "Pikachu",
                PokemonTypes.Electric,
                60,
                PokemonTypes.Fighting,
                1,
                EvolutionStage.Basic
        );
        
         Pokemon pokemon2 = new Pokemon(
                "Raichu",
                PokemonTypes.Electric,
                120,
                PokemonTypes.Fighting,
                2,
                EvolutionStage.Stage1
        );        
        pokemon.addEvolutions(pokemon2);
        
        doNothing().when(pokemonService).updateEvolution("Pikachu", "Raichu");
                
        when(pokemonService.getPokemonByName("Pikachu"))
                .thenReturn(pokemon);
         
        String jsonBody = """
            {            
              "currentStage": "Basic",
              "nextPokemonName": "Raichu",
              "nextStage": "Stage1"
            }
            """; 
        
        mockMvc.perform(post("/pokemon/Pikachu/evolutions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pikachu"))
                .andExpect(jsonPath("$.evolutions[0].currentStage").value("Basic"))
                .andExpect(jsonPath("$.evolutions[0].nextPokemonName").value("Raichu"))
                .andExpect(jsonPath("$.evolutions[0].nextStage").value("Stage1"));        
    }        
    
    @Test
    void updateEvolution_duplicateChain_returnBadRequest() throws Exception
    {
        doThrow(new PokemonDomainException(
                PokemonErrorCode.DUPLICATE_EVOLUTION, "Evolution already Exists: Pikachu -> Raichu"))
                .when(pokemonService)
                .updateEvolution("Pikachu", "Raichu");
        
         String jsonBody = """
            {            
              "currentStage": "Basic",
              "nextPokemonName": "Raichu",
              "nextStage": "Stage1"
            }
            """; 
         
        mockMvc.perform(post("/pokemon/Pikachu/evolutions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("DUPLICATE_EVOLUTION"))
                .andExpect(jsonPath("$.message")
                        .value("Evolution already Exists: Pikachu -> Raichu"));
        
        verify(pokemonService, never()).getPokemonByName(any());
    }
    
    @Test
    void getPokemon_returnsPokemon_whenFound() throws Exception {
        Pokemon pokemon = new Pokemon("Pikachu", PokemonTypes.Bug, 60, PokemonTypes.Fairy, 1, EvolutionStage.Basic);
        
        when(pokemonService.getPokemonByName("Pikachu")).thenReturn(pokemon);

        mockMvc.perform(MockMvcRequestBuilders.get("/pokemon/Pikachu"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name")
                        .value("Pikachu")).andExpect(jsonPath("$.hitPoints")
                                .value(60));
    }

    @Test
    void getPokemon_returns404_whenNotFound() throws Exception {

        when(pokemonService.getPokemonByName("MissingNo"))
                .thenThrow(new PokemonDomainException(
                        PokemonErrorCode.POKEMON_NOT_FOUND,"Pokemon not found: MissingNo"));

         mockMvc.perform(get("/pokemon/MissingNo"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode")
                        .value("POKEMON_NOT_FOUND"))
                .andExpect(jsonPath("$.message")
                        .value("Pokemon not found: MissingNo"));
       
    }   
    
}
