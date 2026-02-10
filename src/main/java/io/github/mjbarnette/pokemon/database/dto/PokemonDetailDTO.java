package io.github.mjbarnette.pokemon.database.dto;

import io.github.mjbarnette.pokemon.database.entity.Pokemon;
import io.github.mjbarnette.pokemon.database.value.EvolutionStage;
import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class PokemonDetailDTO {

    private final String name;
    private final PokemonTypes type;
    private final int hitPoints;
    private final PokemonTypes weakness;
    private final int retreatCost;
    private final EvolutionStage stage;
    private final String imageFile;    
    private final Set<MoveDTO> moves;
    private final Set<EvolutionDTO> evolutions;
    
    public PokemonDetailDTO(String name,
           PokemonTypes type, 
           int hitPoints,
           PokemonTypes weakness,
           int retreatCost,
           EvolutionStage stage,
           String imageFile,
           Set<MoveDTO> moves,
           Set<EvolutionDTO> evolutions)
    {
        this.name = name;
        this.type = type;
        this.hitPoints = hitPoints;
        this.weakness = weakness;
        this.retreatCost = retreatCost;
        this.stage = stage;
        this.imageFile = imageFile; 
        if(moves == null)
        {
            this.moves = new HashSet<>();
        }
        else
        {
            this.moves = new HashSet<>(moves);
        }
        if(evolutions == null)
        {
            this.evolutions = new HashSet<>();
        }
        else
        {
            this.evolutions = new HashSet<>(evolutions);
        }
    }   
    
    public static PokemonDetailDTO fromEntity(Pokemon pokemon)
    {
        Set<MoveDTO> moveList = pokemon.getMoves()
                .stream()
                .map(m -> MoveDTO.fromEntity(m))
                .collect(Collectors.toSet());
        Set<EvolutionDTO> evoList = pokemon.getEvolutions()
                .stream()
                .map(e -> EvolutionDTO.fromEntity(e))
                .collect(Collectors.toSet());
        return new PokemonDetailDTO(pokemon.getName(),
                                    pokemon.getStats().getType(),
                                    pokemon.getStats().getHitPoints(),
                                    pokemon.getStats().getWeakness(),
                                    pokemon.getStats().getRetreatCost(),
                                    pokemon.getCurrentStage(),
                                    pokemon.getImagefile(),
                                    moveList, evoList);
    }    
   
    public Set<MoveDTO> getMoves()
    {
        return Collections.unmodifiableSet(moves);
    }
    
    public Set<EvolutionDTO> getEvolutions()
    {
        return Collections.unmodifiableSet(evolutions);
    }    
    
}

