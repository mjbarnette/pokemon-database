package io.github.mjbarnette.pokemon.database;


import io.github.mjbarnette.pokemon.database.controller.PokemonController;
import io.github.mjbarnette.pokemon.database.dto.PokemonSummaryDTO;
import io.github.mjbarnette.pokemon.database.entity.Pokemon;
import io.github.mjbarnette.pokemon.database.service.PokemonService;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final PokemonService pokemonService;

    public DataLoader(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }
    
    public void run(String... args) throws Exception {
        
        PokemonController test = new PokemonController(pokemonService);
        
        //List<PokemonSummaryDTO> list = test.getAllPokemon();
        /*
        for(PokemonSummaryDTO p : list)
        {
            System.out.println(p);
        }
*/
        
    }
    
}
