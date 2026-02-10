
package io.github.mjbarnette.pokemon.database.service;

import io.github.mjbarnette.pokemon.database.entity.Moves;
import io.github.mjbarnette.pokemon.database.entity.Pokeevolutions;
import io.github.mjbarnette.pokemon.database.entity.Pokemon;
import io.github.mjbarnette.pokemon.database.repository.PokemonRepository;
import io.github.mjbarnette.pokemon.database.utility.CsvUtility;
import io.github.mjbarnette.pokemon.database.value.EvolutionStage;
import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
public class PokemonService {
    
    private final PokemonRepository pokemonRepo;    
    
    public PokemonService(PokemonRepository pokemonRepo) {
        this.pokemonRepo = pokemonRepo;       
    }
    
    public List<Pokemon> getAllPokemon()
    {
        return Collections.unmodifiableList(pokemonRepo.findAll());
    }
    
    public Pokemon getPokemonByName(String name)
    {
        Pokemon pokemon = pokemonRepo.findByName(name)
                .orElseThrow(() -> new RuntimeException("Pokemon not found"));
        return pokemon;
    }
    
    public void updateEvolution(String currentPokemon, String nextPokemon)
    {
        Pokemon current = pokemonRepo.findByName(currentPokemon)
                .orElseThrow(() -> new RuntimeException("Current Pokemon not found"));
        Pokemon next = pokemonRepo.findByName(nextPokemon)
                .orElseThrow(() -> new RuntimeException("Pokemon not found"));
        current.addEvolutions(next);
        pokemonRepo.save(current);
    }
    
    public Pokemon createPokemon(String name, 
            PokemonTypes type, 
            int hitPoints, 
            PokemonTypes weakness, 
            int retreatCost, 
            EvolutionStage stage)
    {
        return pokemonRepo.findByName(name).orElseGet(() -> {
            Pokemon pokemon = new Pokemon(name, type, hitPoints, weakness, retreatCost, stage);
            log.info("New pokemon added {}", pokemon.getName());
            return pokemonRepo.save(pokemon);
        });
    }
    
    public void addImage(String name, String image)
    {
        Pokemon pokemon = pokemonRepo.findByName(name)
                .orElseThrow(() -> new RuntimeException("Pokemon not found"));
        pokemon.setImagefile(image);
       this.pokemonRepo.save(pokemon);
    }
    
    public void addNewMove(String pokemonName, String moveName, int damage, String description, Map<PokemonTypes, Integer> cost)
    {        
        Pokemon pokemon = pokemonRepo.findByName(pokemonName)
                .orElseThrow(() -> new RuntimeException("Pokemon not found"));       
            pokemon.addMoves(moveName, damage, description, cost);
            this.pokemonRepo.save(pokemon);        
    }
    
    public void loadCsvFiles(File pokemon, File moves, File evolutions) throws IOException
    {        
        List<Pokemon> list = CsvUtility.parseCsvFiles(pokemon, moves, evolutions);
        
        Set<String> existingNames = pokemonRepo.findAllNames();
        
        List<Pokemon> toSave = new ArrayList<>();
        for(Pokemon p : list)
        {
            if(!existingNames.contains(p.getName()))
            {
                toSave.add(p);
            }
            else
            {
                log.info("Skipping duplicate Pokemon: {}", p.getName());
            }
        }          
        pokemonRepo.saveAll(toSave);
    }
    
    @Transactional
    public void debugPokemon(String name) {
        Pokemon p = pokemonRepo.findByName(name)
                .orElseThrow();

        log.info("Pokemon: {}", p.getName());
        log.debug("Stats: {}", p.getStats());
        Set<Moves> moves = p.getMoves();
        log.debug("Moves size: {}", moves.size());
        for(Moves m : moves)
        {
            log.debug("Move: {} ", m);
        }
        Set<Pokeevolutions> evo = p.getEvolutions();
        if(evo.isEmpty())
        {
            log.warn("Evolution was not set");
        }
        else
        {
            for(Pokeevolutions e : evo)
            {
                log.debug("Evolutions: {}", e);
            }
        }
    }
}
