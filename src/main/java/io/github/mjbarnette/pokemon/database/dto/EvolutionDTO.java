package io.github.mjbarnette.pokemon.database.dto;

import io.github.mjbarnette.pokemon.database.entity.Pokeevolutions;
import io.github.mjbarnette.pokemon.database.value.EvolutionStage;


public class EvolutionDTO {

    private EvolutionStage currentStage;
    private String nextPokemonName;
    private EvolutionStage nextStage;
    
    public EvolutionDTO(EvolutionStage currentStage, 
                        String nextPokemonName, 
                        EvolutionStage nextStage)
    {
        this.currentStage = currentStage;
        this.nextPokemonName = nextPokemonName;
        this.nextStage = nextStage;
    }    
    
    public static EvolutionDTO fromEntity(Pokeevolutions evo)
    {
        if(evo.getNextPokemon() == null)
        {
            return new EvolutionDTO(evo.getCurrentStage(), null, null);
        }
        return new EvolutionDTO(evo.getCurrentStage(), 
                                evo.getNextPokemon().getName(), 
                                evo.getNextStage());
    }
    
    public EvolutionStage getCurrentStage()
    {
        return currentStage;
    }
    
    public String getNextPokemonName()
    {
        return nextPokemonName;
    }
    
    public EvolutionStage getNextStage()
    {
        return nextStage;
    }
}
