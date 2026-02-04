package io.github.mjbarnette.pokemon.database;

import io.github.mjbarnette.pokemon.database.entity.Moves;
import io.github.mjbarnette.pokemon.database.entity.Pokemon;
import io.github.mjbarnette.pokemon.database.service.PokemonService;
import io.github.mjbarnette.pokemon.database.value.EvolutionStage;
import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import java.util.HashMap;
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
        /*
        Pokemon poke1 = pokemonService.createPokemon("Skiddo", PokemonTypes.Grass, 70, PokemonTypes.Fire, 1, EvolutionStage.Basic);
        pokemonService.addImage("Skiddo", "Skiddo.jpg");        
        HashMap<PokemonTypes, Integer> cost = new HashMap<>();
        cost.put(PokemonTypes.Grass, 1);              
        pokemonService.addNewMove("Skiddo", "Synthesis", 0, "Search your deck for Grass Energy and attach it to 1 of your Pokemon. Then, shuffle your deck.", cost);
        cost = new HashMap<>();
        cost.put(PokemonTypes.Grass, 2);
        cost.put(PokemonTypes.Normal, 1);        
        pokemonService.addNewMove("Skiddo",  "Razor Leaf", 50, null, cost);
        
        Pokemon poke2 = pokemonService.createPokemon("Gogoat", PokemonTypes.Grass, 130, PokemonTypes.Fire, 2, EvolutionStage.Stage1);
        pokemonService.addImage("Gogoat", "gogoat.jpg");        
        cost = new HashMap<>();
        cost.put(PokemonTypes.Grass, 1);
        cost.put(PokemonTypes.Normal, 1);
        pokemonService.addNewMove("Gogoat", "Razor Leaf", 50, null, cost);
        cost = new HashMap<>();
        cost.put(PokemonTypes.Grass, 2);
        cost.put(PokemonTypes.Normal, 1);        
        pokemonService.addNewMove("Gogoat",  "Take Down", 160, "This Pokemon also does 30 damage to itself.", cost);
        
        pokemonService.updateEvolution("Skiddo", "Gogoat");
        */
        List<Pokemon> list = pokemonService.getAllPokemon();
        for(Pokemon p : list)
        {
            pokemonService.debugPokemon(p.getName());
        }
        
        
    }
    
}
