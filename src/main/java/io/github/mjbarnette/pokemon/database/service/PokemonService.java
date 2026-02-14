
package io.github.mjbarnette.pokemon.database.service;

import io.github.mjbarnette.pokemon.database.entity.Moves;
import io.github.mjbarnette.pokemon.database.entity.Pokeevolutions;
import io.github.mjbarnette.pokemon.database.entity.Pokemon;
import io.github.mjbarnette.pokemon.database.repository.MovesRepository;
import io.github.mjbarnette.pokemon.database.repository.PokeevolutionsRepository;
import io.github.mjbarnette.pokemon.database.repository.PokemonRepository;
import io.github.mjbarnette.pokemon.database.utility.CsvParseResult;
import io.github.mjbarnette.pokemon.database.utility.CsvUtility;
import io.github.mjbarnette.pokemon.database.value.CsvErrorCodes;
import io.github.mjbarnette.pokemon.database.value.EvolutionStage;
import io.github.mjbarnette.pokemon.database.value.PokemonErrorCode;
import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    private final MovesRepository moveRepo;
    private final PokeevolutionsRepository evoRepo;
    
    public PokemonService(PokemonRepository pokemonRepo, 
            MovesRepository moveRepo,
            PokeevolutionsRepository evoRepo) {
        this.pokemonRepo = pokemonRepo;
        this.moveRepo = moveRepo;
        this.evoRepo = evoRepo;
    }
    
    public List<Pokemon> getAllPokemon()
    {        
        return Collections.unmodifiableList(pokemonRepo.findAll());
    }
    
    public Pokemon getPokemonByName(String name)
    {
        Pokemon pokemon = pokemonRepo.findByName(name)
                .orElseThrow(() -> 
                        new PokemonDomainException(
                                PokemonErrorCode.POKEMON_NOT_FOUND, 
                                "Pokmeon not found: " + name));
        return pokemon;
    }
    
    public void updateEvolution(String currentPokemon, String nextPokemon)
    {
        Pokemon current = pokemonRepo.findByName(currentPokemon)
                .orElseThrow(() -> 
                        new PokemonDomainException(
                                PokemonErrorCode.CURRENT_POKEMON_NOT_FOUND, 
                                "Current Pokmeon not found: " + currentPokemon));               
        Pokemon next = pokemonRepo.findByName(nextPokemon).
                orElseThrow(() -> 
                        new PokemonDomainException(
                                PokemonErrorCode.NEXT_POKEMON_NOT_FOUND, 
                                "Next Pokmeon not found: " + nextPokemon));       
        Set<Pokeevolutions> evo = evoRepo.findByPokemonName(current.getName());
        if(evo.stream().anyMatch(m -> m.getNextPokemon() != null && m.getNextPokemon().getName().equals(nextPokemon)))
        {
            throw new PokemonDomainException(
                            PokemonErrorCode.DUPLICATE_EVOLUTION, 
                    "Evolution already Exists: " + currentPokemon + " -> " + nextPokemon);
        }        
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
            return pokemonRepo.save(pokemon);
        });
    }
    
    public void addImage(String name, String image)
    {
        Pokemon pokemon = pokemonRepo.findByName(name)
                 .orElseThrow(() -> 
                        new PokemonDomainException(
                                PokemonErrorCode.POKEMON_NOT_FOUND, 
                                "Pokmeon not found: " + name));
        pokemon.setImagefile(image);
        this.pokemonRepo.save(pokemon);
    }
    
    public void addNewMove(String pokemonName, String moveName, int damage, String description, Map<PokemonTypes, Integer> cost)
    {        
        Pokemon pokemon = pokemonRepo.findByName(pokemonName)
                .orElseThrow(() -> 
                        new PokemonDomainException(
                                PokemonErrorCode.POKEMON_NOT_FOUND, 
                                "Pokmeon not found: " + pokemonName));;       
        Moves move = moveRepo.findByNameAndPokemon_Name(moveName, pokemonName).orElse(null);            
        if(move != null)
        {
            throw new PokemonDomainException(
                            PokemonErrorCode.DUPLICATE_MOVE, 
                    "Move already Exists: " + moveName + " for " + pokemonName);
        }
        pokemon.addMoves(moveName, damage, description, cost);
        this.pokemonRepo.save(pokemon);                    
       
    }
    
    public Map<CsvErrorCodes, List<String>> loadCsvFiles(File pokemon, File moves, File evolutions) throws IOException
    {        
        CsvParseResult result = CsvUtility.parseCsvFiles(pokemon, moves, evolutions);        
        Set<String> existingNames = pokemonRepo.findAllNames();        
        List<Pokemon> toSave = new ArrayList<>();  
        for(Pokemon p : result.getPokemonList())
        {
            if(!existingNames.contains(p.getName()))
            {
                toSave.add(p);
            }
            else
            {
                result.addErrorLog(CsvErrorCodes.DUPLICATE_POKEMON_IN_TABLE, p.getName() + "," 
                        + p.getStats().getHitPoints() + ","
                        + p.getStats().getType());
            }
        }          
        pokemonRepo.saveAll(toSave);
        return result.getErrorLog();
    }   
}
