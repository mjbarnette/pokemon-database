package io.github.mjbarnette.pokemon.database.controller;

import io.github.mjbarnette.pokemon.database.dto.CreateEvolutionDTO;
import io.github.mjbarnette.pokemon.database.dto.CreateMoveDTO;
import io.github.mjbarnette.pokemon.database.dto.CreatePokemonDTO;
import io.github.mjbarnette.pokemon.database.dto.PokemonDetailDTO;
import io.github.mjbarnette.pokemon.database.dto.PokemonSummaryDTO;
import io.github.mjbarnette.pokemon.database.service.PokemonService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pokemon")
public class PokemonController {

    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/")
    public List<PokemonSummaryDTO> getAllPokemon() {
        return pokemonService.getAllPokemon()
                .stream()
                .map(PokemonSummaryDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    @GetMapping("/{name}")
    public PokemonDetailDTO getPokemon(@PathVariable String name)
    {
        return PokemonDetailDTO.fromEntity(pokemonService.getPokemonByName(name));
    }
    
    @PostMapping("/")    
    public PokemonSummaryDTO createPokemon(@RequestBody CreatePokemonDTO pokemon)
    {
        pokemonService.createPokemon(pokemon.getName(),
                pokemon.getType(), 
                pokemon.getHitPoints(), 
                pokemon.getWeakness(), 
                pokemon.getRetreatCost(), 
                pokemon.getStage());
        pokemonService.addImage(pokemon.getName(), pokemon.getImageFile());
        return PokemonSummaryDTO.fromEntity(pokemonService.getPokemonByName(pokemon.getName()));
    }
    
    @PostMapping("/{name}/moves")   
    public PokemonDetailDTO createMove(@PathVariable String name, @RequestBody CreateMoveDTO move)
    {
        pokemonService.addNewMove(name, move.getName(), move.getDamage(), move.getDescription(), move.getEnergyCosts());
        return PokemonDetailDTO.fromEntity(pokemonService.getPokemonByName(name));
    }   
   
    
    @PostMapping("/{name}/evolutions")
    public PokemonDetailDTO createEvolution(@PathVariable String name, @RequestBody CreateEvolutionDTO evo)
    {
        pokemonService.updateEvolution(name, evo.getNextPokemonName());
        return PokemonDetailDTO.fromEntity(pokemonService.getPokemonByName(name));
    }
}
